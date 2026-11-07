package org.firstinspires.ftc.teamcode.util;

public class VelPIDController {
    public double kP, kS, kV;
    public double target;

    public VelPIDController(double kP, double kS, double kV, double target) {
        this.kP = kP;
        this.kS = kS;
        this.kV = kV;
        this.target = target;
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public double getTarget() {
        return target;
    }

    public double calculate(double actual) {
        double err = target - actual;
        double pow = kP * err + kS * Math.signum(target) + kV * target;

        return pow;
    }
}
