package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.intake.Intake;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Flywheel;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Turret;

@Config
@TeleOp(name = "Intake Shooter Test")
public class IntakeShooterTest extends OpMode {
    private Intake intake = new Intake();
    private Flywheel flywheel = new Flywheel();
    private Turret turret = new Turret();
    private final ElapsedTime loopTimer = new ElapsedTime();

    // rad/s
    public static double target = 0.0;
    // Fixed turret angle (deg, 0 = forward). No aiming, it just holds this
    public static double turretAngle = 0.0;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        Scheduler.reset();
        for (LynxModule hub : hardwareMap.getAll(LynxModule.class)) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
        intake.init(hardwareMap);
        flywheel.init(hardwareMap);
        turret.init(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad1.aWasPressed()) {
            flywheel.togglePower();
        }

        if (gamepad1.dpadUpWasPressed()) {
            target += 10;
        } else if (gamepad1.dpadDownWasPressed()) {
            target -= 10;
        }
        flywheel.setTarget(target);

        if (gamepad1.dpadRightWasPressed()) {
            turretAngle += 5;
        } else if (gamepad1.dpadLeftWasPressed()) {
            turretAngle -= 5;
        }
        turret.setAngle(turretAngle);

        if (gamepad1.rightBumperWasPressed()) {
            intake.transfer().schedule();
        } else if (intake.state == Intake.IntakeState.TRANSFER) {

        } else if (gamepad1.left_bumper) {
            intake.eject();
        } else if (gamepad1.right_trigger_pressed) {
            intake.frontIntake();
        } else if (gamepad1.left_trigger_pressed) {
            intake.backIntake();
        } else {
            intake.stop();
        }

        flywheel.update();
        turret.update();
        intake.update();
        Scheduler.execute();

        telemetry.addData("target", flywheel.getTarget());
        telemetry.addData("front vel", flywheel.getFrontVelocity());
        telemetry.addData("back vel", flywheel.getBackVelocity());
        telemetry.addData("front error", flywheel.getTarget() - flywheel.getFrontVelocity());
        telemetry.addData("back error", flywheel.getTarget() - flywheel.getBackVelocity());
        telemetry.addData("Turret Angle", turret.getAngle());
        telemetry.addData("Turret Servo Target", turret.getTargetPosition());
        telemetry.addData("loop ms", loopTimer.milliseconds());
        loopTimer.reset();
        telemetry.update();
    }
}
