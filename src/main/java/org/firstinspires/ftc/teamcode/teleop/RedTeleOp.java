package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.subystems.intake.IntakeSubsystem;

public class RedTeleOp extends OpMode {
    private IntakeSubsystem intake = new IntakeSubsystem();

    @Override
    public void init() {
        intake.init(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad1.rightTriggerWasPressed()) {
            intake.setState(IntakeSubsystem.IntakeState.FRONT);
        } else if (gamepad1.leftTriggerWasPressed()) {
            intake.setState(IntakeSubsystem.IntakeState.BACK);
        } else if (!gamepad1.right_trigger_pressed && !gamepad1.left_trigger_pressed) {
            intake.setState(IntakeSubsystem.IntakeState.STOPPED);
        }

        if (gamepad1.rightBumperWasPressed()) {
            intake.setState(IntakeSubsystem.IntakeState.TRANSFER);
        }

        intake.update();
    }
}
