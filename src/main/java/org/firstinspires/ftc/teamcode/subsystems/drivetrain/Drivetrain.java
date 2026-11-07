package org.firstinspires.ftc.teamcode.subsystems.drivetrain;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.pedro.Constants;

public class Drivetrain {
    private Follower follower;

    public void init(HardwareMap hwMap) {
        follower = Constants.create(hwMap);
    }

    public void initAuto(HardwareMap hwMap) {
        follower = Constants.createAuto(hwMap);
    }

    public void update() {
        follower.update();
    }

    public void drive(double forward, double strafe, double turn) {
        follower.manual(forward, strafe, turn);
    }

    public void follow(Path path) {
        follower.follow(path);
    }

    public boolean isBusy() {
        return follower.isBusy();
    }

    public void setPose(Pose pose) {
        follower.setPose(pose);
    }

    public Pose pose() {
        return follower.pose();
    }
}
