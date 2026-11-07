package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.qualcomm.robotcore.hardware.DcMotor;
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

    public static final double TICKS_TO_RAD = 2 * Math.PI / 28;

    // not pid, js p
    // volts and rad/s, same units as ShooterMove
    // @akula @lingala @ big c @ varen a. sarda
    // lets add some shooting while moving
    public static double kV = 0.02;
    public static double kS = 0.8;
    public static double kP = 0.28;
    public static double kI = 0.05;
    public static double kD = 0.0001;

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
        frontFlywheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backFlywheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        voltageSensor = hwMap.voltageSensor.iterator().next();
    }


    public void update() {
        frontVelocity = frontFlywheel.getVelocity() * TICKS_TO_RAD;
        backVelocity = backFlywheel.getVelocity() * TICKS_TO_RAD;

        if (on) {
            double presentVoltage = voltageSensor.getVoltage();
            double ffVolts = kV*target+kS*Math.signum(target);

            // separate wheels, so each motor runs its own loop off its own encoder
            frontFlywheel.setPower(wheelVolts(frontPID, frontVelocity, ffVolts, presentVoltage) / presentVoltage);
            backFlywheel.setPower(wheelVolts(backPID, backVelocity, ffVolts, presentVoltage) / presentVoltage);
        }
    }

    private double wheelVolts(PIDController pid, double velocity, double ffVolts, double presentVoltage) {
        double pidVolts = pid.calculate(velocity, target);
        pidVolts = Math.max(-presentVoltage, Math.min(presentVoltage, pidVolts));

        return Math.max(-presentVoltage, Math.min(presentVoltage, pidVolts + ffVolts));
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
