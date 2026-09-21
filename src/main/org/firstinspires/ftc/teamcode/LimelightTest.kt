package org.firstinspires.ftc.teamcode

import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name = "Limelight Dynamic Path Test", group = "Camera Test")
class LimelightTest : OpMode() {
    companion object {
        const val CAMERA_HEIGHT = 0.0
        const val CAMERA_PITCH = 0.0
    }
    
    val ll: Limelight3A by lazy { hardwareMap["ll"] as Limelight3A }
  
    override fun init() {
        ll.pipelineSwitch(5)
    }

    override fun start() {
        ll.start()
    }
    override fun loop() {
        val res = ll.latestResult
        if (res.isValid() && res != null) return

        val ty = CAMERA_PITCH - res.getTy()

        val r = CAMERA_HEIGHT / tan(Math.toRadians(ty))
        val theta = Math.toRadians(90 - res.getTx())        
        
        telemetry.addData("tx", res.getTx())
        telemetry.addData("ty", ty)
        telemetry.addData("x", r * cos(theta))
        telemetry.addData("y", r * sin(theta))
        telemetry.update()
    }
}
