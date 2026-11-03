package org.firstinspires.ftc.teamcode.util;

import com.pedropathing.math.Pose;

/**
 * Hands the robot pose from autonomous to teleop. Static fields live as long as the robot
 * controller app, so the pose survives switching opmodes.
 * Autos save the localized pose every loop, so this is where the robot actually was when
 * auto stopped (not the last path target).
 */
public final class PoseStorage {
    /** Teleop start pose when no auto ran. */
    public static final Pose DEFAULT_POSE = new Pose(72, 72, Math.toRadians(90));

    private static Pose autoEndPose = null;

    private PoseStorage() {}

    public static void save(Pose pose) {
        autoEndPose = pose;
    }

    /** Returns the saved auto end pose (or DEFAULT_POSE) and clears it, so a later teleop without auto uses the default. */
    public static Pose take() {
        Pose pose = autoEndPose != null ? autoEndPose : DEFAULT_POSE;
        autoEndPose = null;
        return pose;
    }
}
