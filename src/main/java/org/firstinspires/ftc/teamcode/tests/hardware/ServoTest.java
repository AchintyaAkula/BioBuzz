package org.firstinspires.ftc.teamcode.tests.hardware;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Utility;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@Utility(name = "Servo Test", description = "Tests one servo")
public class ServoTest extends OpMode {
    public static String SERVO_NAME = "test";
    public static double POSITION = 0.5;

    public Servo servo;

    public void init() {
        servo = hardwareMap.get(Servo.class, SERVO_NAME);
    }

    public void loop() {
        servo.setPosition(POSITION);
    }
}
