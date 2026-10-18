package org.firstinspires.ftc.teamcode.tests.servo;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@TeleOp(name = "ServoTest", group = "tests")
public class ServoTestTwo extends OpMode {

    public static String SERVO_NAME_ONE = "servo";
    public static String SERVO_NAME_TWO = "servo2";
    public static double POSITION_ONE = 0.5;
    public static double POSITION_TWO = 0.5;


    public Servo servo;
    public Servo servo2;

    public void init() {
        servo = hardwareMap.get(Servo.class, SERVO_NAME_ONE);
        servo = hardwareMap.get(Servo.class, SERVO_NAME_TWO);
    }

    public void loop() {
        servo.setPosition(POSITION_ONE);
        servo2.setPosition(POSITION_TWO);
    }
}
