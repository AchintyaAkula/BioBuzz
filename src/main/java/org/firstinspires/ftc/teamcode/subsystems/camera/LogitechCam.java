package org.firstinspires.ftc.teamcode.subsystems.camera;

import android.util.Size;
import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import java.util.ArrayList;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagLibrary;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagSingleDetection;


@Config
public class LogitechCam {

    public static final double TAG_SIZE_INCHES = 6.5;
    public static final double Y_SPLIT = 72;
    public static final double CamTilt = 108;
    public static double THRESHOLD = 16;

    private final HardwareMap hardwareMap;
    private final Telemetry telemetry;

    private AprilTagProcessor aprilTag;
    private VisionPortal portal;

    public LogitechCam(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
    }

    public void init() {
        AprilTagLibrary.Builder library = new AprilTagLibrary.Builder();
        for (int id = 1; id <= 60; id++) {
            library.addTag(id, "tag" + id, TAG_SIZE_INCHES, DistanceUnit.INCH);
        }

        aprilTag = new AprilTagProcessor.Builder()
                .setTagLibrary(library.build())
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .setDrawAxes(true)
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .setDrawCubeProjection(true)
                .build();

        portal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "logitech"))
                .setCameraResolution(new Size(640, 480))
                .addProcessor(aprilTag)
                .build();
    }

    /** @return true if goal is down, false if up, null if no tag is detected. */
    public Boolean isGoalDown() {
        List<AprilTagSingleDetection> tags = getTags();

        if (tags.isEmpty()) {
            telemetry.addLine("No tag detected");
            return null;
        }

        AprilTagSingleDetection d = tags.get(0);
        double tx = d.ftcPose.bearing;
        double ty = d.ftcPose.elevation;
        double range = d.ftcPose.range;
        double floorDistance = calcFloorDistance(tx, ty, range);

        telemetry.addData("id", d.id);
        telemetry.addData("tx", "%.1f", tx);
        telemetry.addData("ty", "%.1f", ty);
        telemetry.addData("range", "%.1f", range);
        telemetry.addData("floor distance", "%.1f", floorDistance);

        return floorDistance <= THRESHOLD;
    }

    /**
     * The camera sees the goal on the robot's own side, so the goal state only describes the
     * y &lt; 72 side while the robot is on that side.
     *
     * @param down result of {@link #isGoalDown()}
     * @return true if the y &lt; 72 side is up, false if down, null if no tag is detected or the
     *         robot is on the y &gt;= 72 side (state of the other side is unknown).
     */
    public Boolean isLowYSideUp(Boolean down, Pose robotPose) {
        if (down == null || robotPose.y() >= Y_SPLIT) {
            return null;
        }
        return !down;
    }

    public void close() {
        if (portal != null) {
            portal.close();
        }
    }

    private List<AprilTagSingleDetection> getTags() {
        List<AprilTagSingleDetection> tags = new ArrayList<>();
        for (AprilTagDetection detection : aprilTag.getDetections()) {
            if (!(detection instanceof AprilTagSingleDetection)) {
                continue;
            }
            AprilTagSingleDetection d = (AprilTagSingleDetection) detection;
            if (d.metadata != null && d.ftcPose != null) {
                tags.add(d);
            }
        }
        return tags;
    }

    public static double calcFloorDistance(double tx, double ty, double range) {
        double x = Math.toRadians(tx);
        double y = Math.toRadians(ty);
        double t = Math.toRadians(CamTilt);
        double forward = Math.cos(y) * Math.cos(x) * Math.cos(t) - Math.sin(y) * Math.sin(t);
        double sideways = Math.cos(y) * Math.sin(x);
        return range * Math.hypot(forward, sideways);
    }
}