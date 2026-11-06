package org.firstinspires.ftc.teamcode.util.motor;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Utility;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.teamcode.Configuration;

@Config
@Utility(name = "Motor Test - 1", description = "Tests 1 Motor(s)")
public class MotorTest1 extends OpMode {
    public DcMotorEx motor;
    public static String name = "";
    public static DcMotorSimple.Direction dir = DcMotorSimple.Direction.FORWARD;
    public static Configuration.MotorConfig cfg;
    public static double power;

    @Override
    public void init() {
        cfg = new Configuration.MotorConfig(name, dir);
        motor = cfg.createMotor(hardwareMap);
    }

    @Override
    public void loop() {
        motor.setPower(power);
    }
}
