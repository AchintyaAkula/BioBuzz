package org.firstinspires.ftc.teamcode.tests;

import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.intake.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Flywheel;

@TeleOp(name = "Intake Shooter Test")
public class IntakeShooterTest extends OpMode {
    private IntakeSubsystem intake = new IntakeSubsystem();
    private Flywheel flywheel = new Flywheel();

    @Override
    public void init() {
        Scheduler.reset();
        intake.init(hardwareMap);
        flywheel.init(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad1.aWasPressed()) {
            flywheel.powerSwitch();
        }

        if (gamepad1.dpadUpWasPressed()) {
            flywheel.incrPow(0.1);
        } else if (gamepad1.dpadDownWasPressed()) {
            flywheel.incrPow(-0.1);
        }

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
