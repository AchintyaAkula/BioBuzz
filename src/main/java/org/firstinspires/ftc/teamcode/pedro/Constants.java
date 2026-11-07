package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
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
        Controller primaryTranslationalForward = Controller.proportional(0.27162784743075574);
        Controller secondaryTranslationalForward = Controller.proportional(0.10035923159590786);
        Controller primaryTranslationalLateral = Controller.proportional(0.3380062468832345);
        Controller secondaryTranslationalLateral = Controller.proportional(0.12488427653009933);

        c.forwardTranslational.set(
                Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
        c.strafeTranslational.set(
                Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

        c.coast.set(Controller.proportionalFeedforward(0.014386184870839489));
        c.brake.set(Controller.proportionalFeedforward(0.012228257140213566));

        c.headingFeedback.set(Controller.proportional(3.1873236303651207));
        c.headingBrakeCoefficients.set(Vector2D.cartesian(0.05235545986193113, 0.0028968493908407495));

        c.linearBrakeCoefficients.set(Matrix.diag(0.07344728064461475, 0.05203700995284949));
        c.quadraticBrakeCoefficients.set(Matrix.diag(0.0014008923699721674, 0.0016861057768475698));

        c.maxAchievableForwardVelocity.set(70.08580365316251);
        c.maxAchievableStrafeVelocity.set(62.048354528421704);
        c.naturalForwardDeceleration.set(23.70081763109381);
        c.naturalStrafeDeceleration.set(48.76614591790026);
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
