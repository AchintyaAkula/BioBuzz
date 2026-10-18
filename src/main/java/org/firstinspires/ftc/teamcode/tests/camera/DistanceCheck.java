package org.firstinspires.ftc.teamcode.tests.camera;

import com.pedropathing.api.Paths;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.List;

@TeleOp(name = "Distance Check", group = "Hive Vision")
public class DistanceCheck extends LinearOpMode {
    // ---- camera geometry (copied from BallTracker, measure these) ----
    private static final double CAM_PITCH_DEG = -9.3;
    private static final double CAM_H = 9.5;
    private static final double BALL_H = 1.4;
    private static final double CAM_X_OFFSET = 7.625;
    private static final double CAM_Y_OFFSET = 4.75;
    private static final long MAX_STALENESS_MS = 120;
    private static final double CONF_STEP = 0.05;
    private static final double CONF_MIN = 0.05;
    private static final double CONF_MAX = 0.95;

    // ---- planning ----
    private static final double STOP_DIST = 16.0;
    private static final double MAX_PLAN_RANGE = 60.0;

    @Override
    public void runOpMode() {
        Limelight3A limelight = hardwareMap.get(Limelight3A.class, "ll");
        Follower follower = Constants.INSTANCE.create(hardwareMap);
        follower.setPose(new Pose(0, 0, 0));

        int pipeline = 3;
        limelight.pipelineSwitch(pipeline);
        limelight.start();

        boolean prevX = false;
        boolean prevA = false;
        boolean prevRb = false;
        boolean prevLb = false;
        double minConf = 0.44;
        double lastTx = 0, lastTy = 0, lastDist = 0, lastConf = 0;
        int lastCls = -1;
        boolean hasTarget = false;
        double targetX = 0, targetY = 0;

        waitForStart();
        while (opModeIsActive()) {
            follower.update();

            // toggle pipeline from 3 to 5 (idk why but the other code mentioned 5 so i kept it)
            boolean x = gamepad1.x;
            if (x && !prevX) {
                pipeline = (pipeline == 3) ? 5 : 3;
                limelight.pipelineSwitch(pipeline);
            }
            prevX = x;

            // change confidence threshold for the balls it will pick ip
            boolean rb = gamepad1.right_bumper;
            boolean lb = gamepad1.left_bumper;
            if (rb && !prevRb) minConf = Math.min(CONF_MAX, minConf + CONF_STEP);
            if (lb && !prevLb) minConf = Math.max(CONF_MIN, minConf - CONF_STEP);
            prevRb = rb;
            prevLb = lb;

            // stop path
            if (gamepad1.b) {
                follower.stop();
            }

            LLStatus st = limelight.getStatus();
            LLResult r = limelight.getLatestResult();

            boolean stale = r == null || r.getStaleness() > MAX_STALENESS_MS;
            List<LLResultTypes.DetectorResult> dets = (r == null) ? null : r.getDetectorResults();

            LLResultTypes.DetectorResult best = null;
            if (!stale && dets != null) {
                for (LLResultTypes.DetectorResult d : dets) {
                    if (d.getConfidence() < minConf) continue;
                    if (best == null || d.getConfidence() > best.getConfidence()) best = d;
                }
            }

            if (best != null) {
                lastTx = best.getTargetXDegrees();
                lastTy = best.getTargetYDegrees();
                lastConf = best.getConfidence();
                lastCls = best.getClassId();
                lastDist = groundRange(lastTy);
                hasTarget = true;
            }

            // go to best ball
            boolean a = gamepad1.a;
            if (a && !prevA) {
                if (best != null) {
                    Pose pose = follower.pose();
                    double[] p = project(best.getTargetXDegrees(), best.getTargetYDegrees(), pose);
                    if (p != null && Math.hypot(p[0] - pose.x(), p[1] - pose.y()) <= MAX_PLAN_RANGE) {
                        double dx = p[0] - pose.x();
                        double dy = p[1] - pose.y();
                        double heading = Math.atan2(dy, dx);
                        double travel = Math.hypot(dx, dy) - Math.max(0.0, STOP_DIST - CAM_X_OFFSET);
                        double ax, ay;
                        if (travel < 3.0) {
                            ax = pose.x();
                            ay = pose.y();
                        } else {
                            ax = Range.clip(pose.x() + travel * Math.cos(heading), -144, 138);
                            ay = Range.clip(pose.y() + travel * Math.sin(heading), -144, 138);
                        }
                        double endHeading = pose.heading() + wrap(heading - pose.heading());
                        Path path = Paths.line(pose, new Pose(ax, ay)).linear(pose.heading(), endHeading);
                        follower.follow(path);
                        targetX = ax;
                        targetY = ay;
                    }
                }
            }
            prevA = a;

            Pose pose = follower.pose();
            telemetry.addData("pipelineInfo", "Index%d FPS=%.0f", st.getPipelineIndex(), st.getFps());

            telemetry.addData("foundBall", hasTarget);

            telemetry.addData("confidenceFilter", "min=%.2f (RB + / LB -)", minConf);

            if (hasTarget) {
                telemetry.addData(
                        "best",
                        "cls=%d conf=%.2f tx=%+.1f ty=%+.1f DIST=%.1f in",
                        lastCls,
                        lastConf,
                        lastTx,
                        lastTy,
                        lastDist);
            }

            telemetry.addData(
                    "pose", "x=%.1f y=%.1f heading=%.0fdeg", pose.x(), pose.y(), Math.toDegrees(pose.heading()));

            telemetry.addData("follower", "busy=%s", follower.isBusy());
            if (follower.isBusy()) {
                telemetry.addData("Target", "x=%.1f y=%.1f", targetX, targetY);
            }
            telemetry.update();
        }
        follower.stop();
        limelight.stop();
    }

    private static double groundRange(double tyDeg) {
        double depression = CAM_PITCH_DEG - tyDeg;
        if (depression < 1.0) return 999.0;
        return (CAM_H - BALL_H) / Math.tan(Math.toRadians(depression));
    }

    private static double[] project(double txDeg, double tyDeg, Pose pose) {
        double p = Math.toRadians(CAM_PITCH_DEG);
        double h = CAM_H - BALL_H;
        double tanTx = Math.tan(Math.toRadians(txDeg));
        double tanTy = Math.tan(Math.toRadians(tyDeg));

        double denom = Math.sin(p) - tanTy * Math.cos(p);
        if (denom <= 0.02) return null;
        double s = h / denom;

        double xr = s * (Math.cos(p) + tanTy * Math.sin(p)) + CAM_X_OFFSET;
        double yr = -s * tanTx + CAM_Y_OFFSET;

        double th = pose.heading();
        double fx = pose.x() + xr * Math.cos(th) - yr * Math.sin(th);
        double fy = pose.y() + xr * Math.sin(th) + yr * Math.cos(th);
        return new double[] {fx, fy};
    }

    private static double wrap(double a) {
        return Math.atan2(Math.sin(a), Math.cos(a));
    }
}
