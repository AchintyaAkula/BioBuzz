package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.constants.ShooterConstants;

public class ShooterSubsystem {
    private final Telemetry telemetry;
    private final HardwareMap hardwareMap;

    private DcMotor shooter1;
    private DcMotor shooter2;
    private boolean isRedAlliance;
    private Follower follower;
    /** true = low-y top, false = high-y top, null = choose from robot y. */
    private Boolean lowYOverride = null;

    public ShooterSubsystem(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
    }

    public void initShooter(boolean isRedAlliance, Follower follower) {
        this.isRedAlliance = isRedAlliance;
        this.follower = follower;

        shooter1 = hardwareMap.get(DcMotor.class, ShooterConstants.MOTOR_NAME_ONE);
        shooter2 = hardwareMap.get(DcMotor.class, ShooterConstants.MOTOR_NAME_TWO);
    }

    public void setLowYOverride(Boolean lowY) {
        lowYOverride = lowY;
    }

    /**
     * Sets power from the LUT using distance to the target top. The target is picked again on every
     * call from the current robot pose, so call this every loop. Returns the power set.
     */
    public double runShooter() {
        double power = ShooterInterpolation.returnRPM(
                CalcPoseDist.calcDistance(follower, isRedAlliance, lowYOverride)); // LUT output is motor power
        shooter1.setPower(power);
        shooter2.setPower(power);
        return power;
    }

    public void stopShooter() {
        shooter1.setPower(0);
        shooter2.setPower(0);
    }
}
