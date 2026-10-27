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
    val drivetrain: MecanumConfig = MecanumConfig { c ->
        c.frontLeftName.set("lf")
        c.frontRightName.set("rf")
        c.backLeftName.set("lb")
        c.backRightName.set("rb")

        c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD)
        c.frontRightDirection.set(DcMotorSimple.Direction.REVERSE)
        c.backLeftDirection.set(DcMotorSimple.Direction.FORWARD)
        c.backRightDirection.set(DcMotorSimple.Direction.REVERSE)
    }

    val pinpoint: PinpointConfig = PinpointConfig { c ->
        c.name.set("pinpoint")
        c.podType.set(PodType.goBILDA_4_BAR_POD)

        c.xPodDirection.set(EncDir.REVERSED)
        c.yPodDirection.set(EncDir.FORWARD)

        c.xPodOffset.set(-4.292755906)
        c.yPodOffset.set(3.86389764)

        c.globalDistanceUnit.set(DistanceUnit.INCH)
        c.offsetUnits.set(DistanceUnit.INCH)
    }

    val foresight: ForesightConfig = ForesightConfig { c ->
        // TODO tune pedro
    }

    fun create(hwMap: HardwareMap) = Follower(
        PinpointLocalizer(hwMap, pinpoint),
        Mecanum(hwMap, drivetrain),
        Foresight(foresight),
    )
}
