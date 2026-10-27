package org.firstinspires.ftc.teamcode.tests.subsystems

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.teamcode.subsystems.IntakeState
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem

class IntakeTest : OpMode() {
    val intake by lazy { IntakeSubsystem() }

    override fun init() {}

    override fun loop() {
        if (gamepad1.right_trigger > 0.0) { intake.state = IntakeState.FRONT }
        else if (gamepad1.left_trigger > 0.0) { intake.state = IntakeState.REAR }
        else { intake.state = IntakeState.STOPPED }

        if (gamepad1.rightBumperWasPressed()) { intake.state = IntakeState.TRANSFER }
    }
}