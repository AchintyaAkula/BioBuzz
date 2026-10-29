package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import org.firstinspires.ftc.teamcode.constants.ShooterConstants;
public class CalcPoseDist {

    public static double calcDistance(Follower follower, boolean isRedAlliance) {
        Pose pose = follower.pose();

        double goalX;
        double goalY;

        if (isRedAlliance) {
            goalX = ShooterConstants.RED_GOAL_X;
            goalY = ShooterConstants.RED_GOAL_Y;
        } else {
            goalX = ShooterConstants.BLUE_GOAL_X;
            goalY = ShooterConstants.BLUE_GOAL_Y;
        }

        return Math.hypot(goalX - pose.x(), goalY - pose.y());
    }
}