package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.pedropathing.math.Pose;
import com.seattlesolvers.solverslib.util.InterpLUT;

public class ShooterInterpolation {

    private static final InterpLUT shooterSpeed = new InterpLUT();

    static {
        // distance in inches -> velocity in ticks/sec
        shooterSpeed.add(0.0, 285);
        shooterSpeed.add(15, 285);
        shooterSpeed.add(25, 285);
        shooterSpeed.add(45, 285);
        shooterSpeed.add(53, 285);
        shooterSpeed.add(58, 285);
        shooterSpeed.add(63, 285);
        shooterSpeed.add(68, 285);
        shooterSpeed.add(19745, 285); // js so we dont error out if we r too far away
        shooterSpeed.createLUT();
    }

    public static Pose shooterTarget(boolean isRed, boolean lowY) {
        int targetX;
        int targetY;

        if (isRed) {
            targetX = 59;
        } else {
            targetX = 85;
        }
        if (lowY) {
            targetY = 60;
        } else {
            targetY = 84;
        }

        return new Pose(targetX, targetY);
    }

    // table is ticks/sec, flywheel wants rad/s
    public static double returnVelocity(Pose pose, Pose target) {
        return shooterSpeed.get(target.distance(pose)) * Flywheel.TICKS_TO_RAD;
    }
}
