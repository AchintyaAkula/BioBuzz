package org.firstinspires.ftc.teamcode.tests.servo;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

@Config
@TeleOp(name = "CRServoTest", group = "tests")
public class CRServoTest extends OpMode {

    public static String SERVO_NAME = "servo";
    public static double SPEED = 0.5;

    public CRServo servo;

    public void init() {
        servo = hardwareMap.get(CRServo.class, SERVO_NAME);
    }

    public void loop() {
        servo.setPower(SPEED);
    }
}
