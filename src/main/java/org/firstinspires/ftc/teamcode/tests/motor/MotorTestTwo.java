package org.firstinspires.ftc.teamcode.tests.motor;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@Config
@TeleOp(name = "MotorTest", group = "tests")
public class MotorTestTwo extends OpMode {

    public static String MOTOR_NAME_ONE = "motor"; // placeholder change in ftcdash or slothdash or wtv it is
    public static double SPEED_ONE = 0.5; // default

    public static String MOTOR_NAME_TWO = "motor2";

    public static double SPEED_TWO = 0.5;
    public DcMotor dcmotor;
    public DcMotor dcmotor2;

    public void init() {
        dcmotor = hardwareMap.get(DcMotor.class, MOTOR_NAME_ONE);
        dcmotor2 = hardwareMap.get(DcMotor.class, MOTOR_NAME_TWO);
    }

    public void loop() {
        dcmotor.setPower(SPEED_ONE);
        dcmotor.setPower(SPEED_TWO);
    }
}
