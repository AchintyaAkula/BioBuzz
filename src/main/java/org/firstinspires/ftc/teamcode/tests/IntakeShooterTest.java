package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.subsystems.intake.Intake;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Flywheel;

@Config
@TeleOp(name = "Intake Shooter Test")
public class IntakeShooterTest extends OpMode {
    private Intake intake = new Intake();
    private Flywheel flywheel = new Flywheel();

    public static double pow = 0.0;

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
            pow += 0.05;
        } else if (gamepad1.dpadDownWasPressed()) {
            pow -= 0.05;
        }
        flywheel.setPower(pow);

        if (gamepad1.rightBumperWasPressed()) {
            intake.transfer().schedule();
        } else if (gamepad1.right_trigger_pressed) {
            intake.frontIntake().schedule();
        } else if (gamepad1.left_trigger_pressed) {
            intake.backIntake().schedule();
        } else {
            intake.stop().schedule();
        }

        flywheel.update();
        intake.update();
        Scheduler.execute();
    }
}
