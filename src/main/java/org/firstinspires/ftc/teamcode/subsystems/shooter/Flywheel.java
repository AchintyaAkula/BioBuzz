package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.Configuration;
import org.firstinspires.ftc.teamcode.util.ShooterController;

@Config
public class Flywheel {

    // Tuning sequence: kV first (kV ≈ maxPower / maxVelocity), then kP, kS last.
    // Start with a kV estimate, drive at target, observe steady-state error, tune kP to close it.
    public static double kP = 0.0;
    public static double kS = 0.0; // static friction compensation — small positive value
    public static double kV = 0.0;

    // Shared target velocity for both wheels (encoder ticks/sec).
    // Adjust via Dashboard or dpad in the test opmode.
    public static double TARGET_VEL_TPS = 0.0;

    private DcMotorEx frontMotor;
    private DcMotorEx backMotor;

    // Separate controller instances — same gains for now, can be split if tension differs.
    private final ShooterController frontPID = new ShooterController(0, 0, 0, 0);
    private final ShooterController backPID = new ShooterController(0, 0, 0, 0);

    private double targetVel = 0.0;

    public void init(HardwareMap hwMap) {
        frontMotor = Configuration.FRONT_FLYWHEEL_MOTOR.createMotor(hwMap);
        backMotor = Configuration.BACK_FLYWHEEL_MOTOR.createMotor(hwMap);
    }

    /**
     * Sync gains from @Config statics and run the velocity control loop.
     * Must be called every loop when the flywheel is enabled.
     */
    public void update() {
        frontPID.kP = kP;
        frontPID.kS = kS;
        frontPID.kV = kV;
        frontPID.setTarget(targetVel);

        backPID.kP = kP;
        backPID.kS = kS;
        backPID.kV = kV;
        backPID.setTarget(targetVel);

        frontMotor.setPower(frontPID.calculate(frontMotor.getVelocity()));
        backMotor.setPower(backPID.calculate(backMotor.getVelocity()));
    }

    /** Cut power immediately. Use when disabling rather than setting target to 0. */
    public void disable() {
        targetVel = 0.0;
        frontMotor.setPower(0);
        backMotor.setPower(0);
    }

    public void setTargetVel(double tps) {
        targetVel = tps;
    }

    public double getFrontVelocity() {
        return frontMotor.getVelocity();
    }

    public double getBackVelocity() {
        return backMotor.getVelocity();
    }
}
