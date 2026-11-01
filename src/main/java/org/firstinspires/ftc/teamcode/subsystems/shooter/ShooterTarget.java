package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.constants.ShooterConstants;

/**
 * Picks which top to shoot at. Nothing is cached: every call uses the pose passed in, so the
 * target follows the robot as it drives.
 * Default: robot y &lt; 72 shoots (x, 62), else (x, 82).
 * Blue x = 84, red x = 60.
 */
public final class ShooterTarget {
    private ShooterTarget() {}

    public static double x(boolean isRed) {
        return isRed ? ShooterConstants.RED_GOAL_X : ShooterConstants.BLUE_GOAL_X;
    }

    public static double y(boolean lowY) {
        return ShooterConstants.GOAL_Y + (lowY ? -ShooterConstants.TOP_OFFSET_Y : ShooterConstants.TOP_OFFSET_Y);
    }

    /** @param override true = low-y top, false = high-y top, null = choose from robot y. */
    public static boolean lowY(Pose robot, Boolean override) {
        return override != null ? override : robot.y() < ShooterConstants.GOAL_Y;
    }

    public static double distance(Pose robot, boolean isRed, boolean lowY) {
        return Math.hypot(x(isRed) - robot.x(), y(lowY) - robot.y());
    }
}
