package org.firstinspires.ftc.teamcode.auto.red;

import static com.pedropathing.api.Paths.line;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.util.PoseStorage;

@Autonomous(name = "RedLeavePark", group = "Red")
public class RedLeavePark extends OpMode {
    private enum State {
        START,
        LEAVE_PARK,
        PATH_2,
        DONE
    }

    private final PoseFactory p = PoseFactory.degrees();

    private final Pose start = p.of(56, 8, 90);
    private final Pose leavePark = p.of(24.5839, 35.6169, 90);
    private final Pose point2 = p.of(12.6431, 96.1638, 90);

    private final Drivetrain drivetrain = new Drivetrain();

    private State state = State.START;

    private Path leaveParkPath() {
        return line(start, leavePark).linear(start, leavePark);
    }

    private Path path2() {
        return line(leavePark, point2).constant(point2);
    }

    @Override
    public void init() {
        drivetrain.init(hardwareMap);
    }

    @Override
    public void start() {
        // Pinpoint resets during init, so a pose set there can be lost
        drivetrain.setPose(start);
    }

    @Override
    public void loop() {
        drivetrain.update();
        PoseStorage.save(drivetrain.pose());

        switch (state) {
            case START:
                drivetrain.follow(leaveParkPath());
                state = State.LEAVE_PARK;
                break;

            case LEAVE_PARK:
                if (!drivetrain.isBusy()) {
                    drivetrain.follow(path2());
                    state = State.PATH_2;
                }
                break;

            case PATH_2:
                if (!drivetrain.isBusy()) {
                    state = State.DONE;
                }
                break;

            case DONE:
                break;
        }

        telemetry.addData("State", state);
        telemetry.addData("Pose", drivetrain.pose());
        telemetry.update();
    }
}
