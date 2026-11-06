package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.pedropathing.math.Pose;
import com.pedropathing.math.Vector2D;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.util.InterpLUT;
import org.firstinspires.ftc.teamcode.util.Alliance;

public class ShooterSubsystem {

    private final Turret turret = new Turret();
    private final Flywheel flywheel = new Flywheel();
    private Alliance alliance;

    // Distance (inches) → flywheel velocity (ticks/sec) LUT.
    // Add measured data points from field tuning sessions.
    private static final InterpLUT speedLut = new InterpLUT();

    static {
        speedLut.add(0.0, 0.0);
        // TODO: add real data points from field tuning
        speedLut.add(19745.0, Flywheel.TARGET_VEL_TPS);
        speedLut.createLUT();
    }

    public void init(HardwareMap hwMap, Alliance alliance) {
        this.alliance = alliance;
        turret.init(hwMap);
        flywheel.init(hwMap);
    }

    /** Call every loop with the current robot pose. */
    public void update(Pose robotPose) {
        Pose goal = (robotPose.y() > 141.5 / 2.0) ? alliance.turretTargetFar : alliance.turretTargetClose;
        Vector2D toGoal = Vector2D.cartesian(goal.x() - robotPose.x(), goal.y() - robotPose.y());

        turret.update(toGoal.theta() - robotPose.heading());
        flywheel.setTargetVel(speedLut.get(toGoal.magnitude()));
        flywheel.update();
    }

    public Flywheel getFlywheel() {
        return flywheel;
    }
}
