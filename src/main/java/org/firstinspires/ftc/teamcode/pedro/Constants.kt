package org.firstinspires.ftc.teamcode.pedro

import com.pedropathing.algorithm.Foresight
import com.pedropathing.algorithm.ForesightConfig
import com.pedropathing.controllers.Controller
import com.pedropathing.follower.Follower
import com.pedropathing.math.Matrix
import com.pedropathing.math.Vector2D
import com.pedropathing.revhub.drivetrains.Mecanum
import com.pedropathing.revhub.drivetrains.MecanumConfig
import com.pedropathing.revhub.localizers.PinpointConfig
import com.pedropathing.revhub.localizers.PinpointLocalizer
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver.EncoderDirection as EncDir
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver.GoBildaOdometryPods as PodType


object Constants {
    val drivetrain: MecanumConfig = MecanumConfig {
        c ->
            c.frontLeftName.set("lf")
            c.frontRightName.set("rf")
            c.backLeftName.set("lb")
            c.backRightName.set("rb")

            c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD)
            c.frontRightDirection.set(DcMotorSimple.Direction.REVERSE)
            c.backLeftDirection.set(DcMotorSimple.Direction.FORWARD)
            c.backRightDirection.set(DcMotorSimple.Direction.REVERSE)
    }

    val pinpoint: PinpointConfig = PinpointConfig {
        c ->
            c.name.set("pinpoint")
            c.podType.set(PodType.goBILDA_4_BAR_POD)

            c.xPodDirection.set(EncDir.REVERSED)
            c.yPodDirection.set(EncDir.FORWARD)

            c.xPodOffset.set(6.18433374119556)
            c.yPodOffset.set(-1.3435446371243693)

            c.globalDistanceUnit.set(DistanceUnit.INCH)
            c.offsetUnits.set(DistanceUnit.INCH)
    }

    val foresight: ForesightConfig = ForesightConfig {
        c ->
            val primaryTranslationalForward = Controller.proportional(0.2821602735707548)
            val secondaryTranslationalForward = Controller.proportional(0.10425068162302784)
            val primaryTranslationalLateral = Controller.proportional(0.3980021602853099)
            val secondaryTranslationalLateral = Controller.proportional(0.14705116341183502)

            c.forwardTranslational.set(
                Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward)
            )
            c.strafeTranslational.set(
                Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral)
            )

            c.coast.set(Controller.proportionalFeedforward(0.012152047223640215))
            c.brake.set(Controller.proportionalFeedforward(0.010329240140094183))

            c.headingFeedback.set(Controller.proportional(4.466376487809267))
            c.headingBrakeCoefficients.set(Vector2D.cartesian(0.05008508679973391, 0.006462703025168752))

            c.linearBrakeCoefficients.set(Matrix.diag(0.11751344245635795, 0.04301517669921903))
            c.quadraticBrakeCoefficients.set(Matrix.diag(0.0014455936891614586, 0.002320795932500794))

            c.maxAchievableForwardVelocity.set(79.35889244713312)
            c.maxAchievableStrafeVelocity.set(64.79726588463494)
            c.naturalForwardDeceleration.set(28.12052313431262)
            c.naturalStrafeDeceleration.set(67.65830323880897)
    }

    fun create(hwMap: HardwareMap) = Follower(
        PinpointLocalizer(hwMap, pinpoint),
        Mecanum(hwMap, drivetrain),
        Foresight(foresight)
    )
}
