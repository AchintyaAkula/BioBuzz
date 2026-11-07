package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.intake.Intake;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.util.PoseStorage;

public abstract class AllianceTeleOp extends OpMode {
    private static final double FLYWHEEL_VELOCITY = 283.0; // rad/s

    private final Drivetrain drivetrain = new Drivetrain();
    private final Intake intake = new Intake();
    private final Shooter shooter = new Shooter();
    private final ElapsedTime loopTimer = new ElapsedTime();

    private final boolean isRed;

    protected AllianceTeleOp(boolean isRed) {
        this.isRed = isRed;
    }

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        Scheduler.reset();
        for (LynxModule hub : hardwareMap.getAll(LynxModule.class)) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
        drivetrain.init(hardwareMap);
        intake.init(hardwareMap);
        shooter.init(hardwareMap);
    }

    @Override
    public void start() {
        // Pinpoint resets during init, so a pose set there can be lost
        drivetrain.setPose(PoseStorage.take());
        shooter.on();
    }

    @Override
    public void loop() {
        drivetrain.drive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);

        if (gamepad1.rightBumperWasPressed()) {
            intake.transfer().schedule();
        } else if (intake.state != Intake.IntakeState.TRANSFER) {
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

        drivetrain.update();
        shooter.update(drivetrain.pose(), isRed, FLYWHEEL_VELOCITY);
        intake.update();
        Scheduler.execute();

        telemetry.addData("Pose", drivetrain.pose());
        shooter.addTelemetry(telemetry);
        telemetry.addData("loop ms", loopTimer.milliseconds());
        loopTimer.reset();
        telemetry.update();
    }
}
