package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.Configuration;

public class Turret {
    private static final double TURRET_ROM = 540.0;
    private static final double TURRET_MAX_ANG = TURRET_ROM / 2.0;
    private static final double TURRET_0_POS = 0.5;

    private Servo turretServo;
    private double angDeg = 0.0;

    public void init(HardwareMap hwMap) {
        turretServo = Configuration.TURRET_SERVO.createServo(hwMap);
    }

    public void update() {
        turretServo.setPosition(TURRET_0_POS + (angDeg / TURRET_ROM));
    }

    public void setAngle(double angDeg) {
        this.angDeg = angDeg;
    }

    public void aimAt(Pose robot, Pose target) {
        double fieldAngDeg = Math.toDegrees(Math.atan2(target.y() - robot.y(), target.x() - robot.x()));
        double robotAngDeg = normalize(fieldAngDeg - Math.toDegrees(robot.heading()));
        setAngle(closestReachable(robotAngDeg));
    }

    // Angles past +-90 have 2 reachable solutions, so take the one needing the least travel
    private double closestReachable(double angDegNormalized) {
        double best = angDegNormalized;
        for (double candidate : new double[] {angDegNormalized - 360.0, angDegNormalized + 360.0}) {
            if (Math.abs(candidate) <= TURRET_MAX_ANG && Math.abs(candidate - angDeg) < Math.abs(best - angDeg)) {
                best = candidate;
            }
        }
        return best;
    }

    public static double normalize(double angDeg) {
        return angDeg - (360.0 * Math.floor((angDeg + 180.0) / 360.0));
    }
}
