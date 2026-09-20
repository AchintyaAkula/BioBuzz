package org.firstinspires.ftc.teamcode.vision

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.vision.processors.BallProcessor
import org.firstinspires.ftc.vision.VisionPortal
import org.opencv.core.Scalar

@TeleOp(name = "Ball Detection Test", group = "Camera Test")
class BallDetectionTest : OpMode() {
    val ballProcessor by lazy { BallProcessor(Scalar(40.0,20.0,60.0), Scalar(75.0,100.0,100.0)) }
    lateinit var portal: VisionPortal

    override fun init() {
        portal = VisionPortal.Builder()
            .setCamera(hardwareMap.get(WebcamName::class.java, "Webcam 0"))
            .addProcessor(ballProcessor)
            .build()

        telemetry.addLine("Initializing BallDetectionTest, open camera view to check detection")
        telemetry.update()
    }

    override fun loop() {
        val rect = ballProcessor.targetRect

        if (rect != null && runCatching { ballProcessor.width }.isSuccess) {
            telemetry.addData("X: ", rect.let { it.x + it.width / 2 - ballProcessor.width / 2 })
            telemetry.addData("Y: ", rect.let { it.y + it.height / 2 - ballProcessor.height / 2 })
            telemetry.addData("Area: ", rect.area())
        } else {
            telemetry.addLine("Undetected")
        }
        telemetry.update()
    }

    override fun stop() {
        if (!::portal.isInitialized) return
        portal.close()
    }
}