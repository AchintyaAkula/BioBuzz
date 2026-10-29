package org.firstinspires.ftc.teamcode.util.hardware.motor;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Utility;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Configuration;

@Config
@Utility(name = "Motor Test - 2", description = "Tests 2 Motor(s)")
public class MotorTest2 extends OpMode {
    public DcMotorEx motor_1;
    public static Configuration.MotorConfig cfg_1;
    public static double power_1;

    public DcMotorEx motor_2;
    public static Configuration.MotorConfig cfg_2;
    public static double power_2;

    @Override
    public void init() {
        motor_1 = cfg_1.createMotor(hardwareMap);
        motor_2 = cfg_2.createMotor(hardwareMap);
    }

    @Override
    public void loop() {
        motor_1.setPower(power_1);
        motor_2.setPower(power_2);

        telemetry.addData("Position 1 (ticks)", motor_1.getCurrentPosition());
        telemetry.addData("Velocity 1 (ticks/s)", motor_1.getVelocity());
        telemetry.addData("Current 1 (mA)", motor_1.getCurrent(CurrentUnit.MILLIAMPS));
        telemetry.addLine();
        telemetry.addData("Position 2 (ticks)", motor_2.getCurrentPosition());
        telemetry.addData("Velocity 2 (ticks/s)", motor_2.getVelocity());
        telemetry.addData("Current 2 (mA)", motor_2.getCurrent(CurrentUnit.MILLIAMPS));
        telemetry.update();
    }
}
