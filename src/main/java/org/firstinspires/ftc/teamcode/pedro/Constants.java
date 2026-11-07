package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.follower.Follower;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Configuration;

public class Constants {
    // Slow limits for autos, in inches and seconds
    private static final double AUTO_MAX_VELOCITY = 20.0;
    private static final double AUTO_MAX_ACCELERATION = 20.0;
    private static final double AUTO_MAX_DECELERATION = 20.0;

    public static MecanumConfig drivetrainCfg = new MecanumConfig((c) -> {
        c.frontLeftName.set(Configuration.FRONT_LEFT_MOTOR.name);
        c.frontRightName.set(Configuration.FRONT_RIGHT_MOTOR.name);
        c.backLeftName.set(Configuration.BACK_LEFT_MOTOR.name);
        c.backRightName.set(Configuration.BACK_RIGHT_MOTOR.name);

        c.frontLeftDirection.set(Configuration.FRONT_LEFT_MOTOR.direction);
        c.frontRightDirection.set(Configuration.FRONT_RIGHT_MOTOR.direction);
        c.backLeftDirection.set(Configuration.BACK_LEFT_MOTOR.direction);
        c.backRightDirection.set(Configuration.BACK_RIGHT_MOTOR.direction);
    });

    public static PinpointConfig pinpointCfg = new PinpointConfig((c) -> {
        c.name.set(Configuration.PINPOINT.name);
        c.podType.set(Configuration.PINPOINT.podType);

        c.xPodDirection.set(Configuration.PINPOINT.xPodDirection);
        c.yPodDirection.set(Configuration.PINPOINT.yPodDirection);

        c.xPodOffset.set(Configuration.PINPOINT.xPodOffset);
        c.yPodOffset.set(Configuration.PINPOINT.yPodOffset);

        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    private static void configureForesight(ForesightConfig c) {
        // TODO add configuration after tuning
    }

    public static ForesightConfig foresightCfg = new ForesightConfig(Constants::configureForesight);

    public static ForesightConfig autoForesightCfg = new ForesightConfig(c -> {
        configureForesight(c);
        c.maxVelocityConstraint.set(AUTO_MAX_VELOCITY);
        c.maxAccelerationConstraint.set(AUTO_MAX_ACCELERATION);
        c.maxDecelerationConstraint.set(AUTO_MAX_DECELERATION);
    });

    public static Follower create(HardwareMap hwM) {
        return new Follower(
                new PinpointLocalizer(hwM, pinpointCfg), new Mecanum(hwM, drivetrainCfg), new Foresight(foresightCfg));
    }

    public static Follower createAuto(HardwareMap hwM) {
        return new Follower(
                new PinpointLocalizer(hwM, pinpointCfg),
                new Mecanum(hwM, drivetrainCfg),
                new Foresight(autoForesightCfg));
    }
}
