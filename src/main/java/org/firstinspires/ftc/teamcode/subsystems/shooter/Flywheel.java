package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.Configuration;
//import org.firstinspires.ftc.teamcode.util.VelPIDController; tf. is this lmao
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.seattlesolvers.solverslib.controller.PIDController;

@Config
public class Flywheel {
    public DcMotorEx frontFlywheel;
    public DcMotorEx backFlywheel;

    // not pid, js p
    public static double kV = 0.00056;
    public static double kS = 0.06;
    public static double kP = 0.0019;
    public static double kI = 0.0;
    public static double kD = 0.0;

    public static double nominalVoltage = 12.0;

    private boolean on = false;
    private double target = 0.0;
    private double frontVelocity = 0.0;
    private double backVelocity = 0.0;

    private VoltageSensor voltageSensor;

    private final PIDController frontPID = new PIDController(kP, kI, kD);
    private final PIDController backPID = new PIDController(kP, kI, kD);


    public void init(HardwareMap hwMap) {
        frontFlywheel = Configuration.FRONT_FLYWHEEL_MOTOR.createMotor(hwMap);
        backFlywheel = Configuration.BACK_FLYWHEEL_MOTOR.createMotor(hwMap);
        voltageSensor = hwMap.voltageSensor.iterator().next();
    }


    public void update() {
        frontVelocity = frontFlywheel.getVelocity();
        backVelocity = backFlywheel.getVelocity();

        frontVelocity = frontFlywheel.getVelocity();
        backVelocity = backFlywheel.getVelocity();


        if (on) {
            double voltComp = nominalVoltage / voltageSensor.getVoltage();
            double ff = kV*target+kS*Math.signum(target);

            double frontPow = (frontPID.calculate(frontVelocity, target) + ff) * voltComp;
            double backPow = (backPID.calculate(backVelocity, target) + ff) * voltComp;

            frontFlywheel.setPower(Math.max(-1.0, Math.min(1.0, frontPow)));
            backFlywheel.setPower(Math.max(-1.0, Math.min(1.0, backPow)));
        }
    }

    public void togglePower() {
        if (on) {
            off();
        } else {
            on();
        }
    }

    public void on() {
        frontPID.reset();
        backPID.reset();
        on = true;
    }

    public void off() {
        on = false;
        frontFlywheel.setPower(0);
        backFlywheel.setPower(0);
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public double getTarget() {
        return target;
    }

    public double getFrontVelocity() {
        return frontVelocity;
    }

    public double getBackVelocity() {
        return backVelocity;
    }
}
