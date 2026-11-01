package org.firstinspires.ftc.teamcode.subsystems.shooter.turret;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.constants.TurretConstants;
import org.firstinspires.ftc.teamcode.subsystems.shooter.ShooterTarget;

public class TurretSubsystem {
    private final HardwareMap hardwareMap;
    private Servo turret;
    private Follower follower;
    private boolean isRedAlliance;
    private Boolean lowYOverride = null;

    public TurretSubsystem(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public void initTurret(boolean isRedAlliance, Follower follower) {
        this.isRedAlliance = isRedAlliance;
        this.follower = follower;
        turret = hardwareMap.get(Servo.class, "turret");
        center();
    }

    public void setAngle(double degrees) {
        double half = TurretConstants.ROM_DEGREES / 2;
        double clamped = Math.max(-half, Math.min(half, degrees));
        double t = (clamped + half) / TurretConstants.ROM_DEGREES; // 0..1 across travel
        turret.setPosition(TurretConstants.MIN_POS + t * (TurretConstants.MAX_POS - TurretConstants.MIN_POS));
    }

    public void center() {
        setAngle(0);
    }

    /** true = low-y top, false = high-y top, null = choose from robot y. */
    public void setLowYOverride(Boolean lowY) {
        lowYOverride = lowY;
    }

    public void aimTurret() {
        boolean lowY = ShooterTarget.lowY(follower.pose(), lowYOverride);
        aimAt(ShooterTarget.x(isRedAlliance), ShooterTarget.y(lowY));
    }

    public void aimAt(double goalX, double goalY) {
        Pose pose = follower.pose();

        double fieldAngle = Math.toDegrees(Math.atan2(goalY - pose.y(), goalX - pose.x()));
        double relative = fieldAngle - Math.toDegrees(pose.heading()) + TurretConstants.ANGLE_OFFSET_DEGREES;

        relative = ((relative + 180) % 360 + 360) % 360 - 180;

        setAngle(relative);
    }
}
