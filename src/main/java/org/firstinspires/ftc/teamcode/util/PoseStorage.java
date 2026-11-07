package org.firstinspires.ftc.teamcode.util;

import com.pedropathing.math.Pose;

/**
 * Hands the robot pose from autonomous to teleop. Static fields live as long as the robot
 * controller app, so the pose survives switching opmodes.
 * Autos save the localized pose every loop, so this is where the robot actually was when
 * auto stopped (not the last path target).
 */
public final class PoseStorage {
    /** Teleop start poses when no auto ran, with the robot facing away from its driver. */
    public static final Pose RED_DEFAULT_POSE = new Pose(72, 12, Math.toRadians(90));
    public static final Pose BLUE_DEFAULT_POSE = new Pose(72, 12, Math.toRadians(270));

    private static Pose autoEndPose = null;

    private PoseStorage() {}

    public static void save(Pose pose) {
        autoEndPose = pose;
    }

    /** Returns the saved auto end pose (or the alliance default) and clears it, so a later teleop without auto uses the default. */
    public static Pose take(boolean isRed) {
        Pose defaultPose = isRed ? RED_DEFAULT_POSE : BLUE_DEFAULT_POSE;
        Pose pose = autoEndPose != null ? autoEndPose : defaultPose;
        autoEndPose = null;
        return pose;
    }
}
