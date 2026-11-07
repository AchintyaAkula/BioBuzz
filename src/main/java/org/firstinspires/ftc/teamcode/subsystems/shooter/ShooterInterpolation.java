package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.pedropathing.math.Pose;
import com.seattlesolvers.solverslib.util.InterpLUT;

public class ShooterInterpolation {

    private static final InterpLUT shooterSpeed = new InterpLUT();

    static {
        // distance in inches -> velocity in ticks/sec
        shooterSpeed.add(0.0, 0);
        shooterSpeed.add(15, 1000);
        shooterSpeed.add(25, 1500);
        shooterSpeed.add(45, 1750);
        shooterSpeed.add(53, 2000);
        shooterSpeed.add(58, 2250);
        shooterSpeed.add(63, 2500);
        shooterSpeed.add(68, 2750);
        shooterSpeed.add(19745, 3000); // js so we dont error out if we r too far away
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

    public static double returnVelocity(Pose pose, Pose target) {
        return shooterSpeed.get(target.distance(pose));
    }
}
