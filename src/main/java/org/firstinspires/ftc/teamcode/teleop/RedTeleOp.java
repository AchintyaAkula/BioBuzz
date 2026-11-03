package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Configuration;
import org.firstinspires.ftc.teamcode.subystems.intake.IntakeSubsystem;

@Config
@TeleOp(name = "BIG TEST")
public class RedTeleOp extends OpMode {
    private IntakeSubsystem intake = new IntakeSubsystem();
    //private Follower follower;

    private DcMotorEx fwf;
    private DcMotorEx fwb;

    public static double target = 0.0;

    public static double p = 0.0;
    public static double s = 0.0;
    public static double v = 0.0;

    @Override
    public void init() {
        intake.init(hardwareMap);
        //follower = Constants.create(hardwareMap);

        fwf = Configuration.FRONT_FLYWHEEL_MOTOR.createMotor(hardwareMap);
        fwb = Configuration.BACK_FLYWHEEL_MOTOR.createMotor(hardwareMap);
    }

    @Override
    public void loop() {
        /*follower.manual(
                new DrivePowers(
                        -gamepad1.left_stick_y,
                        gamepad1.left_stick_y,
                        gamepad1.right_stick_x

                )
        );
        follower.update();*/

        if (gamepad1.right_bumper) {
            intake.setState(IntakeSubsystem.IntakeState.TRANSFER);
        } else if (gamepad1.right_trigger_pressed) {
            intake.setState(IntakeSubsystem.IntakeState.FRONT);
        } else if (gamepad1.left_trigger_pressed) {
            intake.setState(IntakeSubsystem.IntakeState.BACK);
        } else {
            intake.setState(IntakeSubsystem.IntakeState.STOPPED);
        }

        if (gamepad1.left_bumper) {
            double pow = calculate_psv(fwf.getVelocity(), target, p, s, v);
            fwf.setPower(pow);
            double pow2 = calculate_psv(fwb.getVelocity(), target, p, s, v);
            fwb.setPower(pow2);
        } else {
            fwf.setPower(0.0);
            fwb.setPower(0.0);
        }

        if (gamepad1.dpadUpWasPressed()) {
            target += 10.0;
        }
        if (gamepad1.dpadDownWasPressed()) {
            target -= 10.0;
        }

        intake.update();
    }

    public double calculate_psv(double actual, double target, double p, double s, double v) {
        double error = target - actual;
        double pow = p * error
                + s * Math.signum(target)
                + v * target;

        return pow;
    }
}
