package org.firstinspires.ftc.teamcode.util.hardware.servo;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Utility;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.Configuration;

@Config
@Utility(name = "Servo Test - 1", description = "Tests 1 Servo(s)")
public class ServoTest1 extends OpMode {
    public Servo servo;
    public static Configuration.ServoConfig cfg;
    public static double position;

    @Override
    public void init() {
        servo = cfg.createServo(hardwareMap);
    }

    @Override
    public void loop() {
        servo.setPosition(position);
    }
}
