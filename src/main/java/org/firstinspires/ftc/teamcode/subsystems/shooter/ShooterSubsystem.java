package org.firstinspires.ftc.teamcode.subsystems.shooter;

import static org.firstinspires.ftc.teamcode.subsystems.shooter.CalcPoseDist.calcDistance;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.teamcode.constants.ShooterConstants;
import org.firstinspires.ftc.teamcode.pedro.Constants;

public class ShooterSubsystem {
    private final Telemetry telemetry;
    private final HardwareMap hardwareMap;

    DcMotorEx shooter1;
    DcMotorEx shooter2;
    boolean isRedAlliance;
    Follower follower;
    public ShooterSubsystem(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
    }

    public void initShooter(boolean isRedAlliance, Follower follower) {
        this.isRedAlliance = isRedAlliance;
        this.follower = follower;

        if (isRedAlliance) {
            follower.setPose(new Pose(ShooterConstants.RED_X, ShooterConstants.RED_Y, Math.toRadians(90)));
        } else {
            follower.setPose(new Pose(ShooterConstants.BLUE_X, ShooterConstants.BLUE_Y, Math.toRadians(90)));
        }
        
        shooter1 = hardwareMap.get(DcMotorEx.class, ShooterConstants.MOTOR_NAME_ONE);
        shooter2 = hardwareMap.get(DcMotorEx.class, ShooterConstants.MOTOR_NAME_TWO);
    }
    public void runShooter() {
        double rpm = ShooterInterpolation.returnRPM(calcDistance(follower, isRedAlliance));
        shooter1.setVelocity(rpm * ShooterConstants.TICKS_PER_REV / 60.0);
        shooter2.setVelocity(rpm * ShooterConstants.TICKS_PER_REV / 60.0);
    }

    public void stopShooter() {
        shooter1.setVelocity(0);
        shooter2.setVelocity(0);
    }


}
