package org.firstinspires.ftc.teamcode.util.hardware.motor;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Utility;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Configuration;

@Config
@Utility(name = "Motor Test - 1", description = "Tests 1 Motor(s)")
public class MotorTest1 extends OpMode {
    public DcMotorEx motor;
    public static Configuration.MotorConfig cfg;
    public static double power;

    @Override
    public void init() {
        motor = cfg.createMotor(hardwareMap);
    }

    @Override
    public void loop() {
        motor.setPower(power);

        telemetry.addData("Position (ticks)", motor.getCurrentPosition());
        telemetry.addData("Velocity (ticks/s)", motor.getVelocity());
        telemetry.addData("Current (mA)", motor.getCurrent(CurrentUnit.MILLIAMPS));
        telemetry.update();
    }
}
