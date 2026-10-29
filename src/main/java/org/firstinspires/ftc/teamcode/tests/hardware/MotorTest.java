package org.firstinspires.ftc.teamcode.tests.hardware;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Utility;
import com.qualcomm.robotcore.hardware.DcMotor;

@Config
@Utility(name = "Motor Test", description = "Tests one motor")
public class MotorTest extends OpMode {

    public static String MOTOR_NAME = "motor";
    public static double SPEED = 0.0;

    public DcMotor dcmotor;

    public void init() {
        dcmotor = hardwareMap.get(DcMotor.class, MOTOR_NAME);
    }

    public void loop() {
        dcmotor.setPower(SPEED);
    }
}
