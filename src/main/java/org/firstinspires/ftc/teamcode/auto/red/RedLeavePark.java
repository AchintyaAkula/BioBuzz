package org.firstinspires.ftc.teamcode.auto.red;

import static com.pedropathing.api.Paths.*;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.util.PoseStorage;

@Autonomous(name = "RedLeavePark", group = "Red")
public class RedLeavePark extends OpMode {

    private Follower follower;
    private int state = 0;

    private final PoseFactory p = PoseFactory.degrees();

    private final Pose start       = p.of(56, 8, 90);
    private final Pose leavePark   = p.of(24.5839, 35.6169, 90);
    private final Pose point2      = p.of(12.6431, 96.1638, 90);

    private Path leaveParkPath() {
        return line(start, leavePark).linear(start, leavePark);
    }

    private Path path2() {
        return line(leavePark, point2).constant(point2);
    }

    @Override
    public void init() {
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
    }

    @Override
    public void start() {
        state = 0;
    }

    @Override
    public void loop() {
        follower.update();
        PoseStorage.save(follower.pose()); // handoff to teleop: where the robot really is

        switch (state) {
            case 0:
                follower.follow(leaveParkPath());
                state = 1;
                break;

            case 1:
                if (!follower.isBusy()) {
                    follower.follow(path2());
                    state = 2;
                }
                break;

            case 2:
                if (!follower.isBusy()) {
                    state = -1; // done
                }
                break;
        }

        telemetry.addData("State", state);
        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));
        telemetry.update();
    }
}