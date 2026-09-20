package org.firstinspires.ftc.teamcode.vision.processors

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration
import org.firstinspires.ftc.vision.VisionProcessor
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.Rect
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc as cv

class BallProcessor(
    private val upper: Scalar,
    private val lower: Scalar,
    private val tolerance: Double = 15.0
) : VisionProcessor {

    private val imgHSV = Mat()
    private val imgBinary = Mat()
    private val imgFinal = Mat()
    private val emptyMat = Mat()

    private val contours: ArrayList<MatOfPoint> = ArrayList()

    @Volatile
    var largestContour: MatOfPoint? = null
        private set
    @Volatile
    var targetRect: Rect? = null
        private set

    override fun init(
        width: Int,
        height: Int,
        calibration: CameraCalibration?
    ) {  }

    override fun processFrame(frame: Mat, captureTimeNanos: Long): Any? {
        cv.cvtColor(frame, imgHSV, cv.COLOR_RGB2HSV)
        Core.inRange(imgHSV, lower, upper, imgBinary)

        val kernel = cv.getStructuringElement(cv.MORPH_RECT, Size(tolerance, tolerance))
        cv.morphologyEx(imgBinary, imgFinal, cv.MORPH_CLOSE, kernel)
        kernel.release()

        updateContours()

        return targetRect
    }

    fun updateContours() {
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
}