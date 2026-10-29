package org.firstinspires.ftc.teamcode.tests.camera

import com.pedropathing.api.Paths
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.pedro.Constants
import org.firstinspires.ftc.teamcode.subsystems.camera.Limelight

@TeleOp
class LimelightSubsystemTest : OpMode() {
    val ll by lazy { Limelight() }
    val f by lazy { Constants.create(hardwareMap) }

    override fun init() {
        ll.init()
    }

    override fun loop() {
        f.update()
        if (gamepad1.a) f.follow(Paths.line(f.pose(), (ll.getTargetPose(f.pose()) ?: f.pose())).tangent())
        if (gamepad1.dpad_up) ll.incrThreshold()
        if (gamepad1.dpad_down) ll.decrThreshold()

        ll.update()
    }
}