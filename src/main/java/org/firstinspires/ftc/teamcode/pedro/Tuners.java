package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.pedropathing.tuning.autotune.Procedure;
import com.pedropathing.tuning.autotune.Tuner;

import org.firstinspires.ftc.teamcode.pedro.procedures.ForesightTuner;
import org.firstinspires.ftc.teamcode.pedro.procedures.MecanumTuner;
import org.firstinspires.ftc.teamcode.pedro.procedures.PinpointTuner;
import org.firstinspires.ftc.teamcode.pedro.procedures.Tests;

public class Tuners {
    @Tuner
    public Procedure mecanumTuner() { return new MecanumTuner(); }

    @Tuner
    public Procedure pinpointTuner() { return new PinpointTuner(); }

    public Procedure foresightTuner() {
        return new ForesightTuner(
                (hwM) -> new PinpointLocalizer(hwM, Constants.pinpointCfg),
                (hwM) -> new Mecanum(hwM, Constants.drivetrainCfg)
        );
    }

    @Tuner
    public Procedure tests() {
        return new Tests(
                (hwM) -> new Mecanum(hwM, Constants.drivetrainCfg),
                (hwM) -> new PinpointLocalizer(hwM, Constants.pinpointCfg),
                () -> new Foresight(Constants.foresightCfg)
        );
    }
}
