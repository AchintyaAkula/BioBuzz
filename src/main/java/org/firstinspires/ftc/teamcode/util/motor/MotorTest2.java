package org.firstinspires.ftc.teamcode.util.motor;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Utility;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.teamcode.Configuration;

@Config
@Utility(name = "Motor Test - 2", description = "Tests 2 Motor(s)")
public class MotorTest2 extends OpMode {
    public DcMotorEx motor1;
    public static String name1 = "";
    public static DcMotorSimple.Direction dir1 = DcMotorSimple.Direction.FORWARD;
    public static Configuration.MotorConfig cfg1;
    public static double power1;

    public DcMotorEx motor2;
    public static String name2 = "";
    public static DcMotorSimple.Direction dir2 = DcMotorSimple.Direction.FORWARD;
    public static Configuration.MotorConfig cfg2;
    public static double power2;

    @Override
    public void init() {
        cfg1 = new Configuration.MotorConfig(name1, dir1);
        motor1 = cfg1.createMotor(hardwareMap);

        cfg2 = new Configuration.MotorConfig(name2, dir2);
        motor2 = cfg2.createMotor(hardwareMap);
    }

    @Override
    public void loop() {
        motor1.setPower(power1);
        motor2.setPower(power2);
    }
}
