package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Shooter {
    private static final double TARGET_SWITCH_Y = 72.0;
    private static final double TARGET_SWITCH_DEADBAND = 3.0;

    private final Flywheel flywheel = new Flywheel();
    private final Turret turret = new Turret();

    private boolean lowY = true;
    private Pose aimTarget;

    public void init(HardwareMap hwMap) {
        flywheel.init(hwMap);
        turret.init(hwMap);
    }

    public void update(Pose pose, boolean isRed) {
        // Keep current target inside dead zone, so aim and speed do not jump at the switch line
        if (pose.y() < TARGET_SWITCH_Y - TARGET_SWITCH_DEADBAND) {
            lowY = true;
        } else if (pose.y() > TARGET_SWITCH_Y + TARGET_SWITCH_DEADBAND) {
            lowY = false;
        }
        aimTarget = ShooterInterpolation.shooterTarget(isRed, lowY);

        flywheel.setTarget(ShooterInterpolation.returnVelocity(pose, aimTarget));
        flywheel.update();

        turret.aimAt(pose, aimTarget);
        turret.update();
    }

    public void on() {
        flywheel.on();
    }

    public void off() {
        flywheel.off();
    }

    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData("Flywheel Target", flywheel.getTarget());
        telemetry.addData("Flywheel Front Velocity", flywheel.getFrontVelocity());
        telemetry.addData("Flywheel Back Velocity", flywheel.getBackVelocity());
        telemetry.addData("Turret Angle", turret.getAngle());
        telemetry.addData("Aim Target", aimTarget);
    }
}
