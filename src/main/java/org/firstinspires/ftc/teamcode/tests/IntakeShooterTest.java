package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.subsystems.intake.Intake;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Flywheel;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Turret;
import org.firstinspires.ftc.teamcode.util.VelPIDController;

@Config
@TeleOp(name = "Intake Shooter Test")
public class IntakeShooterTest extends OpMode {
    private Intake intake = new Intake();
    private Flywheel flywheel = new Flywheel();
    private Turret turret = new Turret();

    public static double target = 0.0;
    // Fixed turret angle (deg, 0 = forward). No aiming, it just holds this
    public static double turretAngle = 0.0;
    public VelPIDController frontPID = new VelPIDController(0.0019, 0.06, 0.0004, 0);
    public VelPIDController backPID = new VelPIDController(0.0019, 0.06, 0.0004, 0);

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        Scheduler.reset();
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

        telemetry.addData("vel", flywheel.backFlywheel.getVelocity());
        telemetry.addData("Turret Angle", turret.getAngle());
        telemetry.update();

        flywheel.update();
        turret.update();
        intake.update();
        Scheduler.execute();
    }
}