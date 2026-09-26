package org.firstinspires.ftc.teamcode.subsystems

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.pedropathing.api.PoseFactory
import com.pedropathing.math.Pose
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration
import org.firstinspires.ftc.teamcode.OpMode
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.VisionProcessor
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.Rect
import org.opencv.core.Scalar
import org.opencv.core.Size
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan
import org.opencv.imgproc.Imgproc as cv
import kotlin.properties.Delegates

class Vision {
    companion object {
        const val H_FOV = 0.0
        const val V_FOV = 0.0

        const val CAMERA_HEIGHT = 0.0
        const val CAMERA_PITCH = 0.0

        val upper: Scalar = Scalar(0.0, 0.0, 0.0)
        val lower: Scalar = Scalar(0.0,0.0,0.0)
    }

    val detector: BallDetector = BallDetector(upper, lower)
    val portal: VisionPortal = VisionPortal.Builder()
        .setCamera(OpMode.hardwareMap["Webcam 0"] as WebcamName)
        .addProcessor(detector)
        .build()

    fun getTargetPose(robotPose: Pose): Pose? {
        val target: Rect? = detector.targetRect
        if (target == null || runCatching { detector.width }.isFailure) return null

        val relX = target.run { x + (width / 2) - (detector.width / 2) }
        val relY = target.run { y + (height / 2) - (detector.height / 2) }

        val tx = atan(relX / (detector.width / 2) * tan(H_FOV / 2))
        val ty = atan(relY / (detector.height / 2) * tan(V_FOV / 2)) + CAMERA_PITCH

        val r = CAMERA_HEIGHT / tan(ty)
        val theta = (PI / 2) - tx

        val offset = PoseFactory.radians().of(r * cos(theta), r * sin(theta), -tx)
        return robotPose.plus(offset)
    }
}

class BallDetector(
    private val upper: Scalar,
    private val lower: Scalar,
    private val tolerance: Double = 15.0
) : VisionProcessor {

    private val imgHSV = Mat()
    private val imgBinary = Mat()
    private val imgFinal = Mat()
    private val emptyMat = Mat()

    var width: Int by Delegates.notNull()
    var height: Int by Delegates.notNull()

    private val contours: ArrayList<MatOfPoint> = ArrayList()

    @Volatile
    var largestContour: MatOfPoint? = null
        private set
    @Volatile
    var targetRect: Rect? = null
        private set

    override fun processFrame(frame: Mat, captureTimeNanos: Long): Any? {
        cv.cvtColor(frame, imgHSV, cv.COLOR_RGB2HSV)
        Core.inRange(imgHSV, lower, upper, imgBinary)

        val kernel = cv.getStructuringElement(cv.MORPH_RECT, Size(tolerance, tolerance))
        cv.morphologyEx(imgBinary, imgFinal, cv.MORPH_CLOSE, kernel)
        kernel.release()

        updateContours()

        return targetRect
    }

    private fun updateContours() {
        contours.clear()
        largestContour = null
        targetRect = null

        cv.findContours(imgFinal, contours, emptyMat, cv.RETR_EXTERNAL, cv.CHAIN_APPROX_SIMPLE)

        if (contours.isEmpty()) { return }

        var currMaxArea = 0.0
        var maxIndex = -1

        for (i in contours.indices) {
            val c = contours[i]
            val currArea = cv.contourArea(c)

            if (currArea > currMaxArea) {
                maxIndex = i
                currMaxArea = currArea
            }
        }

        if (maxIndex == -1) { return }

        largestContour = contours[maxIndex]
        targetRect = cv.boundingRect(largestContour)
    }

    override fun onDrawFrame(
        canvas: Canvas,
        onscreenWidth: Int,
        onscreenHeight: Int,
        scaleBmpPxToCanvasPx: Float,
        scaleCanvasDensity: Float,
        userContext: Any?
    ) {
        val rect = userContext as? Rect ?: return

        val paint: Paint = Paint().apply {
            color = Color.YELLOW
            style = Paint.Style.STROKE
            strokeWidth = 4f * scaleCanvasDensity
        }

        canvas.drawRect(
            rect.x * scaleBmpPxToCanvasPx,
            rect.y * scaleBmpPxToCanvasPx,
            (rect.x + rect.width) * scaleBmpPxToCanvasPx,
            (rect.y + rect.height) * scaleBmpPxToCanvasPx,
            paint
        )
    }

    override fun init(width: Int, height: Int, calibration: CameraCalibration?) {
        this.width = width
        this.height = height
    }
}