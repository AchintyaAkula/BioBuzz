package org.firstinspires.ftc.teamcode.subsystems.drivetrain;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.pedro.Constants;

public class Drivetrain {
    private Follower follower;

    public void init(HardwareMap hwMap) {
        follower = Constants.create(hwMap);
    }

    public void update() {
        follower.update();
    }

    public void drive(double forward, double strafe, double turn) {
        follower.manual(forward, strafe, turn);
    }

    public void setPose(Pose pose) {
        follower.setPose(pose);
    }

    public Pose pose() {
        return follower.pose();
    }
}
