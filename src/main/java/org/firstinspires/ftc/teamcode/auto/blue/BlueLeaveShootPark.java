package org.firstinspires.ftc.teamcode.auto.blue;

import static com.pedropathing.api.Paths.line;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.intake.Intake;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.util.PoseStorage;

@Autonomous(name = "BlueLeaveShootPark", group = "Blue")
public class BlueLeaveShootPark extends OpMode {
    private enum State {
        START,
        PATH_1,
        SPIN_UP,
        TRANSFER,
        PATH_2,
        PATH_3,
        DONE
    }

    private static final boolean IS_RED = false;

    private static final double SPIN_UP_SEC = 2.0;
    private static final double RUN_TRANSFER_SEC = 3.0;

    private final PoseFactory p = PoseFactory.degrees();

    private final Pose start = p.of(88, 136, 270);
    private final Pose point1 = p.of(85.5003, 111.2751, 270);
    private final Pose point2 = p.of(121.1403, 102.6227, 270);
    private final Pose point3 = p.of(132.1232, 48.4747, 281.4658);

    private final Drivetrain drivetrain = new Drivetrain();
    private final Intake intake = new Intake();
    private final Shooter shooter = new Shooter();

    private final ElapsedTime stateTimer = new ElapsedTime();
    private State state = State.START;

    private Path path1() {
        return line(start, point1).linear(start, point1);
    }

    private Path path2() {
        return line(point1, point2).constant(point2);
    }

    private Path path3() {
        return line(point2, point3).tangent();
    }

    @Override
    public void init() {
        drivetrain.initAuto(hardwareMap);
        intake.init(hardwareMap);
        shooter.init(hardwareMap);
    }

    @Override
    public void start() {
        // Pinpoint resets during init, so a pose set there can be lost
        drivetrain.setPose(start);
        shooter.on();
    }

    @Override
    public void loop() {
        drivetrain.update();
        PoseStorage.save(drivetrain.pose());

        autonomousUpdate();

        intake.update();
        shooter.update(drivetrain.pose(), IS_RED);

        telemetry.addData("State", state);
        telemetry.addData("Pose", drivetrain.pose());
        shooter.addTelemetry(telemetry);
        telemetry.update();
    }

    private void autonomousUpdate() {
        switch (state) {
            case START:
                drivetrain.follow(path1());
                setState(State.PATH_1);
                break;

            case PATH_1:
                if (!drivetrain.isBusy()) {
                    setState(State.SPIN_UP);
                }
                break;

            case SPIN_UP:
                if (stateTimer.seconds() > SPIN_UP_SEC) {
                    intake.startTransfer(RUN_TRANSFER_SEC);
                    setState(State.TRANSFER);
                }
                break;

            case TRANSFER:
                if (!intake.isTransferring()) {
                    drivetrain.follow(path2());
                    setState(State.PATH_2);
                }
                break;

            case PATH_2:
                if (!drivetrain.isBusy()) {
                    drivetrain.follow(path3());
                    setState(State.PATH_3);
                }
                break;

            case PATH_3:
                if (!drivetrain.isBusy()) {
                    setState(State.DONE);
                }
                break;

            case DONE:
                break;
        }
    }

    private void setState(State newState) {
        state = newState;
        stateTimer.reset();
    }
}
