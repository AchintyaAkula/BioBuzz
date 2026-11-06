package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Vector2D;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Turret;

@Config
@TeleOp(name = "Turret Test", group = "Test")
public class TurretTest extends OpMode {
    private Follower follower;
    private final Turret turret = new Turret();

    public static double targetX = 0.0;
    public static double targetY = 72.0;

    @Override
    public void init() {
        follower = Constants.create(hardwareMap);
        turret.init(hardwareMap);
    }

    @Override
    public void loop() {
        follower.update();

        // Get robot position as Vector2D from follower pose
        Vector2D robotPos = follower.pose().toVector2D();
        Vector2D targetPos = Vector2D.cartesian(targetX, targetY);

        // Vector from robot to target
        Vector2D toTarget = targetPos.minus(robotPos);

        // Use Vector2D's .theta() method to get angle in radians, then convert to degrees
        double angleRad = toTarget.theta();
        double angleDeg = Math.toDegrees(angleRad);

        // Account for robot heading
        double relativeAngleDeg = angleDeg - Math.toDegrees(follower.pose().heading());
        double normalizedAngle = Turret.normalize(relativeAngleDeg);

        turret.setAngle(normalizedAngle);
        turret.update();

        telemetry.addData("Robot X", follower.pose().x());
        telemetry.addData("Robot Y", follower.pose().y());
        telemetry.addData("Robot Heading (deg)", Math.toDegrees(follower.pose().heading()));
        telemetry.addData("Target Angle (deg)", angleDeg);
        telemetry.addData("Normalized Turret Angle", normalizedAngle);
        telemetry.update();
    }
}
