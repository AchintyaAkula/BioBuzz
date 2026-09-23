package org.firstinspires.ftc.teamcode.pedro

import com.pedropathing.algorithm.Foresight
import com.pedropathing.revhub.drivetrains.Mecanum
import com.pedropathing.revhub.localizers.PinpointLocalizer
import com.pedropathing.tuning.autotune.Procedure
import com.pedropathing.tuning.autotune.Tuner
import org.firstinspires.ftc.teamcode.pedro.procedures.ForesightTuner
import org.firstinspires.ftc.teamcode.pedro.procedures.MecanumTuner
import org.firstinspires.ftc.teamcode.pedro.procedures.PinpointTuner
import org.firstinspires.ftc.teamcode.pedro.procedures.Tests

object Tuning {
    @Tuner
    fun mecanumTuner(): Procedure = MecanumTuner()

    @Tuner
    fun pinpointTuner(): Procedure = PinpointTuner()

    @Tuner
    fun foresightTuner(): Procedure = ForesightTuner(
        { hwMap -> PinpointLocalizer(hwMap, Constants.pinpoint) },
        { hwMap -> Mecanum(hwMap, Constants.drivetrain) }
    )

    @Tuner
    fun tests(): Procedure = Tests (
        { hwMap -> Mecanum(hwMap, Constants.drivetrain) },
        { hwMap -> PinpointLocalizer(hwMap, Constants.pinpoint) },
        { Foresight(Constants.foresight) }
    )
}