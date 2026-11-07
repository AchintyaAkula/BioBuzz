package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.subsystems.intake.Intake;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Flywheel;
import org.firstinspires.ftc.teamcode.util.VelPIDController;

@Config
@TeleOp(name = "Intake Shooter Test")
public class IntakeShooterTest extends OpMode {
    private Intake intake = new Intake();
    private Flywheel flywheel = new Flywheel();

    public static double target = 0.0;
    public VelPIDController frontPID = new VelPIDController(0.0, 0.06, 0.0004, 0);
    public VelPIDController backPID = new VelPIDController(0.0, 0.06, 0.0004, 0);


    @Override
    public void init() {
        Scheduler.reset();
        intake.init(hardwareMap);
        flywheel.init(hardwareMap);
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
        intake.update();
        Scheduler.execute();
    }
}
