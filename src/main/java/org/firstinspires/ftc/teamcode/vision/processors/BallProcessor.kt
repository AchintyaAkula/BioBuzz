package org.firstinspires.ftc.teamcode.vision.processors

import android.graphics.Canvas
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
    ) {}

    override fun processFrame(frame: Mat?, captureTimeNanos: Long) {
        cv.cvtColor(frame, imgHSV, cv.COLOR_RGB2HSV)
        Core.inRange(imgHSV, upper, lower, imgBinary)

        val kernel = cv.getStructuringElement(cv.MORPH_RECT, Size(tolerance, tolerance))
        cv.morphologyEx(imgBinary, imgFinal, cv.MORPH_CLOSE, kernel)
        kernel.release()
    }

    fun updateContours() {
        contours.clear()
        largestContour = null

        cv.findContours(imgFinal, contours, Mat(), cv.RETR_EXTERNAL, cv.CHAIN_APPROX_SIMPLE)

        var currMaxArea: Double = 0.0
        var currMaxIndex: Int = 0
        for (i in contours.indices) {
            val c = contours[i]
            val currArea = cv.contourArea(c)

            if (currArea > currMaxArea) {
                currMaxIndex = i
                currMaxArea = currArea
            }
        }
        largestContour = contours[currMaxIndex]
        targetRect = cv.boundingRect(largestContour!!)
    }

    override fun onDrawFrame(
        canvas: Canvas?,
        onscreenWidth: Int,
        onscreenHeight: Int,
        scaleBmpPxToCanvasPx: Float,
        scaleCanvasDensity: Float,
        userContext: Any?
    ) {}

}