package org.firstinspires.ftc.teamcode.util.servo;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Utility;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.Configuration;

@Config
@Utility(name = "Servo Test - 2", description = "Tests 2 Servo(s)")
public class ServoTest2 extends OpMode {
    public Servo servo1;
    public static String name1 = "";
    public static Configuration.ServoConfig cfg1;
    public static double position1;

    public Servo servo2;
    public static String name2 = "";
    public static Configuration.ServoConfig cfg2;
    public static double position2;

    @Override
    public void init() {
        cfg1 = new Configuration.ServoConfig(name1);
        servo1 = cfg1.createServo(hardwareMap);

        cfg2 = new Configuration.ServoConfig(name2);
        servo2 = cfg2.createServo(hardwareMap);
    }

    @Override
    public void loop() {
        servo1.setPosition(position1);
        servo2.setPosition(position2);
    }
}
