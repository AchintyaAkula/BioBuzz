package org.firstinspires.ftc.teamcode.subsystems.drivetrain;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.constants.DrivetrainConstants;

public class DrivetrainSubsystem {
    public static double WALL_TOLERANCE = 3; // inches

    private final Follower follower;

    public DrivetrainSubsystem(Follower follower) {
        this.follower = follower;
    }

    public boolean relocalize() {
        Pose p = follower.pose();

        double quarter = Math.PI / 2;

        long k = Math.round(p.heading() / quarter);

        double snapped = k * quarter; // round heading to nearest 90 degree whenever you are relocalizing with a wall also

        boolean sideways = Math.floorMod(k, 2) == 1;

        double halfX = (sideways ? DrivetrainConstants.WIDTH : DrivetrainConstants.LENGTH) / 2;
        double halfY = (sideways ? DrivetrainConstants.LENGTH : DrivetrainConstants.WIDTH) / 2;

        double x = p.x(), y = p.y();

        boolean used = false;

        for (double t : new double[]{halfX, 144 - halfX}) {
            if (Math.abs(t - p.x()) <= WALL_TOLERANCE) { x = t; used = true; }
        }
        for (double t : new double[]{halfY, 144 - halfY}) {
            if (Math.abs(t - p.y()) <= WALL_TOLERANCE) { y = t; used = true; }
        }

        if (used) follower.setPose(new Pose(x, y, snapped));
        return used;
    }
}
