package org.firstinspires.ftc.teamcode

import com.pedropathing.api.Paths
import com.pedropathing.api.PoseFactory
import com.pedropathing.follower.Follower
import com.pedropathing.math.Pose
import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.pedro.Constants
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

@TeleOp(name = "Limelight Dynamic Path Test", group = "Camera Test")
class LimelightTest : OpMode() {
    companion object {
        const val CAMERA_HEIGHT = 8.0
        const val CAMERA_PITCH = 5.0
    }

    val ll: Limelight3A by lazy { hardwareMap["ll"] as Limelight3A }
    val f: Follower by lazy { Constants.create(hardwareMap) }

    override fun init() {
        ll.pipelineSwitch(3)
        ll.pipelineSwitch(5)
        ll.start()
        f.setPose(Pose.zero().withHeading(Math.toRadians(90.0)))
    }

    override fun loop() {
        f.update()
        val res = ll.latestResult
        if (res.isValid() && res != null) return

        val ty = -CAMERA_PITCH - res.ty

        val r = CAMERA_HEIGHT / tan(Math.toRadians(ty))
        val theta = Math.toRadians(90 - res.getTx())

        if (gamepad1.xWasPressed()) {
            val target = PoseFactory.radians().of(
                r * cos(theta),
                r * sin(theta),
                0.0,
            )
            val path = Paths.line(f.pose(), target).tangent()
            f.follow(path)
        }
        if (gamepad1.yWasPressed()) {
            f.follow(Paths.line(f.pose(), f.pose() + PoseFactory.radians().of(0.0, 10.0, 0.0)).tangent())
        }

        telemetry.addData("pos", "x: ${f.pose().x()}, y: ${f.pose().y()}, heading: ${f.pose().heading()}")
        telemetry.addData("tx", res.getTx())
        telemetry.addData("ty", res.ty)
        telemetry.addData("x", r * cos(theta) + f.pose().x())
        telemetry.addData("y", r * sin(theta) + f.pose().y())
        telemetry.update()
    }
}
