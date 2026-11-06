package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.intake.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.shooter.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.util.Alliance;

@Config
@TeleOp(name = "Red TeleOp")
public class RedTeleOp extends OpMode {
    private final IntakeSubsystem intake = new IntakeSubsystem();
    private final ShooterSubsystem shooter = new ShooterSubsystem();
    private Follower follower;

    @Override
    public void init() {
        intake.init(hardwareMap);
        shooter.init(hardwareMap, Alliance.RED);
        follower = Constants.create(hardwareMap);
    }

    @Override
    public void loop() {
        follower.manual(new DrivePowers(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x));

        follower.update();

        if (gamepad1.right_bumper) {
            intake.setState(IntakeSubsystem.IntakeState.TRANSFER);
        } else if (gamepad1.right_trigger_pressed) {
            intake.setState(IntakeSubsystem.IntakeState.FRONT);
        } else if (gamepad1.left_trigger_pressed) {
            intake.setState(IntakeSubsystem.IntakeState.BACK);
        } else {
            intake.setState(IntakeSubsystem.IntakeState.STOPPED);
        }

        intake.update();
        shooter.update(follower.pose());
    }
}
