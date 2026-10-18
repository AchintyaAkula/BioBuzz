package org.firstinspires.ftc.teamcode.tests.servo;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@TeleOp(name = "ServoTestAdjustTwo", group = "tests")
public class ServoTestAdjustTwo extends OpMode {

    public static String SERVO_NAME_ONE = "servo";
    public static String SERVO_NAME_TWO = "servo2";
    public static double POSITION_ONE = 0.5;
    public static double POSITION_TWO = 0.5;

    public static double MAXPOS_ONE = 1;
    public static double MINPOS_ONE = 0;

    public static double MAXPOS_TWO = 1;
    public static double MINPOS_TWO = 0;


    public Servo servo;
    public Servo servo2;

    public void init() {
        servo = hardwareMap.get(Servo.class, SERVO_NAME_ONE);
        servo2 = hardwareMap.get(Servo.class, SERVO_NAME_TWO);
    }

    public void loop() {

        if (POSITION_ONE <= MAXPOS_ONE && POSITION_ONE >= MINPOS_ONE) {
            servo.setPosition(POSITION_ONE);
        } else if (POSITION_ONE > MAXPOS_ONE) {
            POSITION_ONE = MAXPOS_ONE;
        } else if (POSITION_ONE < MINPOS_ONE) {
            POSITION_ONE = MINPOS_ONE;
        }

        if (POSITION_TWO <= MAXPOS_TWO && POSITION_TWO >= MINPOS_TWO) {
            servo2.setPosition(POSITION_TWO);
        } else if (POSITION_TWO > MAXPOS_TWO) {
            POSITION_TWO = MAXPOS_TWO;
        } else if (POSITION_TWO < MINPOS_TWO) {
            POSITION_TWO = MINPOS_TWO;
        }

    }
}
