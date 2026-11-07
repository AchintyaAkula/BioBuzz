package org.firstinspires.ftc.teamcode.teleop;

import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.subsystems.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.intake.Intake;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.util.PoseStorage;

public abstract class AllianceTeleOp extends OpMode {
    private final Drivetrain drivetrain = new Drivetrain();
    private final Intake intake = new Intake();
    private final Shooter shooter = new Shooter();

    private final boolean isRed;
    DcMotor frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor;
    protected AllianceTeleOp(boolean isRed) {
        this.isRed = isRed;
    }

    @Override
    public void init() {
        Scheduler.reset();
         frontLeftMotor = hardwareMap.dcMotor.get("fl");
         backLeftMotor = hardwareMap.dcMotor.get("fr");
         frontRightMotor = hardwareMap.dcMotor.get("bl");
         backRightMotor = hardwareMap.dcMotor.get("br");
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
        // Remember to reverse the right-side motors (or left-side depending on your build/chassis)
        double y = -gamepad1.left_stick_y; // Remember, Y stick is reversed
        double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
        double rx = gamepad1.right_stick_x;

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all powers maintain the same ratio, but no more than 1
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower  = (y + x + rx) / denominator;
        double backLeftPower   = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower  = (y + x - rx) / denominator;

        frontLeftMotor.setPower(frontLeftPower);
        backLeftMotor.setPower(backLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backRightMotor.setPower(backRightPower);



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

        if (gamepad1.leftBumperWasPressed()) {
            shooter.on();
        } else if (gamepad1.leftBumperWasReleased()) {
            shooter.off();
        }

        drivetrain.update();
        shooter.update(drivetrain.pose(), isRed);
        intake.update();
        Scheduler.execute();

        telemetry.addData("Pose", drivetrain.pose());
        shooter.addTelemetry(telemetry);
        telemetry.update();
    }
}
