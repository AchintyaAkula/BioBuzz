package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.Configuration;
import org.firstinspires.ftc.teamcode.util.VelPIDController;

public class Flywheel {
    public DcMotorEx frontFlywheel;
    public DcMotorEx backFlywheel;

    private VelPIDController frontPID = new VelPIDController(0.0019, 0.06, 0.00056, 0);
    private VelPIDController backPID = new VelPIDController(0.0019, 0.06, 0.00056, 0);

    private boolean on = false;
    private double target = 0.0;
    private double frontVelocity = 0.0;
    private double backVelocity = 0.0;

    public void init(HardwareMap hwMap) {
        frontFlywheel = Configuration.FRONT_FLYWHEEL_MOTOR.createMotor(hwMap);
        backFlywheel = Configuration.BACK_FLYWHEEL_MOTOR.createMotor(hwMap);
    }

    public void update() {
        frontVelocity = frontFlywheel.getVelocity();
        backVelocity = backFlywheel.getVelocity();

        frontPID.setTarget(target);
        backPID.setTarget(target);
        if (on) {
            frontFlywheel.setPower(frontPID.calculate(frontVelocity));
            backFlywheel.setPower(backPID.calculate(backVelocity));
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
