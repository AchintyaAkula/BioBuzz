package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.Configuration;

@Config
public class Turret {
    // One servo, 500 deg of total travel, servo 0..1 maps linearly onto it
    public static double TURRET_ROM = 500.0;
    // Keeps the commanded angle off the servo's hard ends (0.0 / 1.0)
    public static double limitMarginDeg = 5.0;

    public static double turret0Pos = 0.5;
    public static double turretDirection = 1.0; // 1.0 or -1.0

    private Servo turretServo;
    // Last chosen (commanded) angle. Doubles as the hysteresis state for picking a wrap side
    private double angDeg = 0.0;
    private boolean aiming = true;

    public void init(HardwareMap hwMap) {
        turretServo = Configuration.TURRET_SERVO.createServo(hwMap);
    }

    public void update() {
        turretServo.setPosition(getTargetPosition());
    }

    /** Servo position the turret is commanded to, 0..1. The servo does not report where it actually is. */
    public double getTargetPosition() {
        double pos = turret0Pos + (turretDirection * angDeg / TURRET_ROM);
        return Math.max(0.0, Math.min(1.0, pos));
    }

    public void setAngle(double angDeg) {
        this.angDeg = clamp(angDeg);
    }

    public double getAngle() {
        return angDeg;
    }

    public static double getMinAngle() {
        return -(TURRET_ROM / 2.0) + limitMarginDeg;
    }

    public static double getMaxAngle() {
        return (TURRET_ROM / 2.0) - limitMarginDeg;
    }

    public void toggleAiming() {
        aiming = !aiming;
    }

    public boolean isAiming() {
        return aiming;
    }

    public void aimAt(Pose robot, Pose target) {
        if (!aiming) {
            setAngle(0.0);
            return;
        }

        double fieldAngDeg = Math.toDegrees(Math.atan2(target.y() - robot.y(), target.x() - robot.x()));
        double robotAngDeg = fieldAngDeg - Math.toDegrees(robot.heading());
        setAngle(pickCandidate(robotAngDeg));
    }

    /**
     * Hysteresis on the wrap side. A bearing and the same bearing +-360 point the turret the same way,
     * and with 500 deg of travel any bearing between (360 - max) and max is reachable both ways.
     * Taking whichever candidate in range is closest to the last commanded angle means the turret only
     * jumps ~360 deg when the current side runs out of travel, not whenever the bearing crosses +-180.
     */
    private double pickCandidate(double targetDeg) {
        double min = getMinAngle();
        double max = getMaxAngle();

        double chosen = angDeg; // nothing in range -> hold position
        double bestDist = Double.MAX_VALUE;
        for (double cand : new double[] {targetDeg, targetDeg + 360.0, targetDeg - 360.0}) {
            if (cand >= min && cand <= max) {
                double dist = Math.abs(cand - angDeg);
                if (dist < bestDist) {
                    bestDist = dist;
                    chosen = cand;
                }
            }
        }
        return chosen;
    }

    private static double clamp(double angDeg) {
        return Math.max(getMinAngle(), Math.min(getMaxAngle(), angDeg));
    }

    public static double normalize(double angDeg) {
        return angDeg - (360.0 * Math.floor((angDeg + 180.0) / 360.0));
    }

}