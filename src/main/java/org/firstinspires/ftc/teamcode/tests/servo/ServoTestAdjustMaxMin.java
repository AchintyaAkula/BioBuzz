package org.firstinspires.ftc.teamcode.tests.servo;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@TeleOp(name = "ServoTestAdjustMaxMin", group = "tests")
public class ServoTestAdjustMaxMin extends OpMode {

    public static String SERVO_NAME = "servo";
    public static double POSITION = 0.5;
    public static double MAXPOS = 1;
    public static double MINPOS = 0;

    public Servo servo;

    public void init() {
        servo = hardwareMap.get(Servo.class, SERVO_NAME);
    }

    public void loop() {

        if (POSITION <= MAXPOS && POSITION >= MINPOS) {
            servo.setPosition(POSITION);
        } else if (POSITION > MAXPOS) {
            POSITION = MAXPOS;
        } else if (POSITION < MINPOS) {
            POSITION = MINPOS;
        }

    }
}
