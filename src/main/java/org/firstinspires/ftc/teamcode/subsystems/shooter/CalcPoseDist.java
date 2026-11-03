package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;

public class CalcPoseDist {

    /** Distance to the top picked from robot y. */
    public static double calcDistance(Follower follower, boolean isRedAlliance) {
        return calcDistance(follower, isRedAlliance, null);
    }

    /** @param lowYOverride true = low-y top, false = high-y top, null = choose from robot y. */
    public static double calcDistance(Follower follower, boolean isRedAlliance, Boolean lowYOverride) {
        Pose pose = follower.pose();
        return ShooterTarget.distance(pose, isRedAlliance, ShooterTarget.lowY(pose, lowYOverride));
    }
}
