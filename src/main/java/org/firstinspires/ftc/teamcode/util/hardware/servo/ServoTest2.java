package org.firstinspires.ftc.teamcode.util.hardware.servo;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Utility;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Configuration;

@Config
@Utility(name = "Servo Test - 2", description = "Tests 2 Servo(s)")
public class ServoTest2 extends OpMode {
    public Servo servo_1;
    public static Configuration.ServoConfig cfg_1;
    public static double position_1;

    public Servo servo_2;
    public static Configuration.ServoConfig cfg_2;
    public static double position_2;

    @Override
    public void init() {
        servo_1 = cfg_1.createServo(hardwareMap);
        servo_2 = cfg_2.createServo(hardwareMap);
    }

    @Override
    public void loop() {
        servo_1.setPosition(position_1);
        servo_2.setPosition(position_2);
    }
}
