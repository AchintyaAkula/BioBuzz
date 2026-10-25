package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.LogitechCam;

@Config
@TeleOp(name = "AprilTagDistance", group = "Camera")
public class LogitechCamTest extends OpMode {

    private LogitechCam cam;
    private Follower follower;

    @Override
    public void init() {
        cam = new LogitechCam(hardwareMap, telemetry);
        cam.init();
        follower = Constants.INSTANCE.create(hardwareMap);
    }

    @Override
    public void loop() {
        follower.update();

        Boolean goalDown = cam.isGoalDown();
        Boolean lowYSideUp = cam.isLowYSideUp(goalDown, follower.pose());

        telemetry.addData("robot y", "%.1f", follower.pose().y());
        telemetry.addData("goal down", goalDown);
        telemetry.addData("y<72 side up", lowYSideUp);
    }

    @Override
    public void stop() {
        if (cam != null) {
            cam.close();
        }
    }
}
