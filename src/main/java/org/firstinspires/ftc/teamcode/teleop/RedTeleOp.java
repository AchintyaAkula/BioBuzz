package org.firstinspires.ftc.teamcode.teleop;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subystems.intake.IntakeSubsystem;

@TeleOp(name = "Red TeleOp")
public class RedTeleOp extends OpMode {
    private IntakeSubsystem intake = new IntakeSubsystem();
    private Follower follower;

    @Override
    public void init() {
        intake.init(hardwareMap);
        follower = Constants.create(hardwareMap);
    }

    @Override
    public void loop() {
        follower.manual(
                new DrivePowers(
                        -gamepad1.left_stick_y,
                        gamepad1.left_stick_y,
                        gamepad1.right_stick_x
                )
        );
        follower.update();

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
