package org.firstinspires.ftc.teamcode.tests.motor;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

@Config
@TeleOp(name = "MotorTest", group = "tests")
public class MotorTest extends OpMode {

    public static String MOTOR_NAME = "motor"; // placeholder change in ftcdash or slothdash or wtv it is
    public static double SPEED = 0.5; // default

    public DcMotor dcmotor;

    public void init() {
        dcmotor = hardwareMap.get(DcMotor.class, MOTOR_NAME);
    }

    public void loop() {
        dcmotor.setPower(SPEED);
    }
}
