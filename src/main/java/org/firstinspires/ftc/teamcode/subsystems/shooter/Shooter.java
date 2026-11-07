package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Shooter {
    private final Flywheel flywheel = new Flywheel();

    public void init(HardwareMap hwMap) {
        flywheel.init(hwMap);
    }

    public void update(Pose pose, boolean isRed) {
        flywheel.setTarget(ShooterInterpolation.returnRPM(pose, isRed));
        flywheel.update();
    }

    public void on() {
        flywheel.on();
    }

    public void off() {
        flywheel.off();
    }
}
