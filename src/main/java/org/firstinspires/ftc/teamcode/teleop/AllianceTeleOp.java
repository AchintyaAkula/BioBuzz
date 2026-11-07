package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.intake.Intake;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.util.PoseStorage;

public abstract class AllianceTeleOp extends OpMode {
    private final Drivetrain drivetrain = new Drivetrain();
    private final Intake intake = new Intake();
    private final Shooter shooter = new Shooter();

    private final boolean isRed;

    protected AllianceTeleOp(boolean isRed) {
        this.isRed = isRed;
    }

    @Override
    public void init() {
        drivetrain.init(hardwareMap);
        intake.init(hardwareMap);
        shooter.init(hardwareMap);
    }

    @Override
    public void start() {
        // Pinpoint resets during init, so a pose set there can be lost
        drivetrain.setPose(PoseStorage.take());
    }

    @Override
    public void loop() {
        drivetrain.drive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);

        if (gamepad1.rightBumperWasPressed()) {
            intake.startTransfer();
        } else if (!intake.isTransferring()) {
            if (gamepad1.b) {
                intake.eject();
            } else if (gamepad1.right_trigger_pressed) {
                intake.frontIntake();
            } else if (gamepad1.left_trigger_pressed) {
                intake.backIntake();
            } else {
                intake.stop();
            }
        }

        if (gamepad1.leftBumperWasPressed()) {
            shooter.on();
        } else if (gamepad1.leftBumperWasReleased()) {
            shooter.off();
        }

        drivetrain.update();
        shooter.update(drivetrain.pose(), isRed);
        intake.update();

        telemetry.addData("Pose", drivetrain.pose());
        shooter.addTelemetry(telemetry);
        telemetry.update();
    }
}
