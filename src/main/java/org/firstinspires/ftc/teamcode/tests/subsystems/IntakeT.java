package org.firstinspires.ftc.teamcode.tests.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Configuration;

@Config
@TeleOp(name = "Intake thingy")
public class IntakeT extends OpMode {
    public DcMotorEx motor1;
    public DcMotorEx motor2;

    public Servo transfer;

    public DcMotor fw1;
    public DcMotor fw2;

    public static double fwPower = 0.0;

    public static double passivePower = 0.0;
    public static double fastPower = 0.0;
    public static double slowPower = 0.0;

    @Override
    public void init() {
        motor1 = Configuration.FRONT_INTAKE_MOTOR.createMotor(hardwareMap);
        motor2 = Configuration.BACK_INTAKE_MOTOR.createMotor(hardwareMap);

        fw1 = Configuration.FRONT_FLYWHEEL_MOTOR.createMotor(hardwareMap);
        fw2 = Configuration.BACK_FLYWHEEL_MOTOR.createMotor(hardwareMap);

        transfer = Configuration.TRANSFER_SERVO.createServo(hardwareMap);
    }

    @Override
    public void loop() {


        if (gamepad1.left_trigger > 0.0) {
            motor1.setPower(fastPower);
            motor2.setPower(passivePower);
        } else if (gamepad1.right_trigger > 0.0) {
            motor1.setPower(passivePower);
            motor2.setPower(fastPower);
        } else if (gamepad1.dpad_up) {
            if (motor1.getPower() == fastPower) {
                motor2.setPower(slowPower);
            } else {
                motor2.setPower(fastPower);
                motor1.setPower(slowPower);
            }
        } else {
            motor1.setPower(0.0);
            motor2.setPower(0.0);
        }

        if (gamepad1.right_bumper) {
            fw1.setPower(fwPower);
            fw2.setPower(fwPower);
        } else {
            fw1.setPower(0);
            fw2.setPower(0);
        }

        if (gamepad1.dpadDownWasPressed()) {
            transfer.setPosition(transfer.getPosition() == 0.35 ? 0.9 : 0.35);
        }

        telemetry.addData("Intake 1 stall", motor1.getCurrent(CurrentUnit.AMPS));
        telemetry.addData("Intake 2 stall", motor2.getCurrent(CurrentUnit.AMPS));
        telemetry.update();
    }
}
