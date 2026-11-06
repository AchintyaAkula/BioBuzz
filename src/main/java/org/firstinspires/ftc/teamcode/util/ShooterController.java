package org.firstinspires.ftc.teamcode.util;

public class ShooterController {
    public double kP, kS, kV;
    public double target;

    public ShooterController(double kP, double kS, double kV, double target) {
        this.kP = kP;
        this.kS = kS;
        this.kV = kV;
        this.target = target;
    }

    public double calculate(double actual) {
        double err = target - actual;
        return kP * err + kS * Math.signum(target) + kV * target;
    }

    public double getTarget() {
        return target;
    }

    public void setTarget(double target) {
        this.target = target;
    }
}
