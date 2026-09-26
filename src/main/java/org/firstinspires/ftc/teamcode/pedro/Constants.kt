package org.firstinspires.ftc.teamcode.pedro

import com.pedropathing.algorithm.Foresight
import com.pedropathing.algorithm.ForesightConfig
import com.pedropathing.follower.Follower
import com.pedropathing.revhub.drivetrains.Mecanum
import com.pedropathing.revhub.drivetrains.MecanumConfig
import com.pedropathing.revhub.localizers.PinpointConfig
import com.pedropathing.revhub.localizers.PinpointLocalizer
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver.GoBildaOdometryPods as PodType
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver.EncoderDirection as EncDir
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

object Constants {
    val drivetrain: MecanumConfig = MecanumConfig {
        c ->
            // TODO change the names to match actual config names
            c.frontLeftName.set("fl")
            c.frontRightName.set("fr")
            c.backLeftName.set("bl")
            c.backRightName.set("br")

            // TODO Change the directions to match actual directions
            c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD)
            c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD)
            c.backLeftDirection.set(DcMotorSimple.Direction.FORWARD)
            c.backRightDirection.set(DcMotorSimple.Direction.FORWARD)
    }

    val pinpoint: PinpointConfig = PinpointConfig {
        c ->
            // TODO change name
            c.name.set("pp")
            c.podType.set(PodType.goBILDA_4_BAR_POD)

            // TODO change directions
            c.xPodDirection.set(EncDir.FORWARD)
            c.yPodDirection.set(EncDir.FORWARD)

            // TODO add actual offsets
            c.xPodOffset.set(0.0)
            c.yPodOffset.set(0.0)
    }

    val foresight: ForesightConfig = ForesightConfig {
        c ->
        // TODO copy over the generated code from AutoTune
    }

    fun create(hwMap: HardwareMap) = Follower(
        PinpointLocalizer(hwMap, pinpoint),
        Mecanum(hwMap, drivetrain),
        Foresight(foresight)
    )
}
