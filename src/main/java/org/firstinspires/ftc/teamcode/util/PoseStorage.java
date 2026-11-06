package org.firstinspires.ftc.teamcode.util;

import com.pedropathing.math.Pose;

/**
 * Hands the robot pose from auto to teleop. Static fields survive opmode switches as long as the
 * robot controller app is running. Auto saves the localized pose every loop; teleop reads it once
 * in init() so it starts tracking from where auto actually stopped.
 */
public final class PoseStorage {

    // Used when no auto ran before teleop (e.g., direct teleop start at practice).
    public static final Pose DEFAULT_POSE = new Pose(72, 72, Math.toRadians(90));

    private static Pose autoEndPose = null;

    private PoseStorage() {}

    public static void save(Pose pose) {
        autoEndPose = pose;
    }

    /**
     * Returns the saved auto end pose (or DEFAULT_POSE if no auto ran) and clears the stored
     * value so a subsequent teleop-without-auto gets the default rather than a stale position.
     */
    public static Pose take() {
        Pose pose = autoEndPose != null ? autoEndPose : DEFAULT_POSE;
        autoEndPose = null;
        return pose;
    }
}
