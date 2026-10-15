package org.firstinspires.ftc.teamcode.tests;

import android.util.Size;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.ArrayList;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagSingleDetection;

@TeleOp(name = "AprilTagDistance", group = "Camera")
public class LogitechCamTest extends OpMode {

    public static final double CamTilt = 108; // in degrees
    public static final double THRESHOLD = 35;

    private AprilTagProcessor aprilTag;
    private VisionPortal portal;

    @Override
    public void init() {
        aprilTag = new AprilTagProcessor.Builder()
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

    @Override
    public void loop() {
        List<AprilTagSingleDetection> tags = getTags();

        if (tags.isEmpty()) {
            telemetry.addLine("No tag detected");
            return;
        }

        for (AprilTagSingleDetection d : tags) {
            double tx = d.ftcPose.bearing;
            double ty = d.ftcPose.elevation;
            double range = d.ftcPose.range;
            double floorDistance = calcFloorDistance(tx, ty, range);

            telemetry.addData("id", d.id);
            telemetry.addData("tx", "%.1f", tx);
            telemetry.addData("ty", "%.1f", ty);
            telemetry.addData("range", "%.1f", range);
            telemetry.addData("floor distance", "%.1f", floorDistance);

            if (floorDistance <= THRESHOLD) {
                telemetry.addLine("goal down");
            } else {
                telemetry.addLine("goal up");
            }
        }
    }

    @Override
    public void stop() {
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
            if (d.metadata != null && (d.id == 30 || d.id == 34 || d.id == 38 || d.id == 42)) {
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
