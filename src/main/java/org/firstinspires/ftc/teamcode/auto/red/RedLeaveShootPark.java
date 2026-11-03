package org.firstinspires.ftc.teamcode.auto.red;

import static com.pedropathing.api.Paths.*;

import org.firstinspires.ftc.teamcode.subsystems.shooter.turret.TurretSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.shooter.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.intake.IntakeSubsystem;


import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.util.PoseStorage;

@Autonomous(name = "RedLeaveShootPark", group = "Red")
public class RedLeaveShootPark extends OpMode {

    private Follower follower;
    private final ElapsedTime stateTimer = new ElapsedTime();
    private int state = 0;

    // Poses (x, y, heading in degrees)
    private final PoseFactory p = PoseFactory.degrees();

    private final Pose start  = p.of(56, 8, 90);
    private final Pose point1 = p.of(58.4997, 32.7249, 90);
    private final Pose point2 = p.of(22.8597, 41.3773, 90);
    private final Pose point3 = p.of(11.8768, 95.5253, 101.4658);

    // Subsystems (created in init(), because hardwareMap is not ready before that)
    private TurretSubsystem turret;
    private ShooterSubsystem shooter;
    private IntakeSubsystem intake;

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
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        turret = new TurretSubsystem(hardwareMap);
        shooter = new ShooterSubsystem(hardwareMap, telemetry);
        intake = new IntakeSubsystem(hardwareMap, follower);
        turret.initTurret(true, follower);
        shooter.initShooter(true, follower);


    }

    @Override
    public void start() {
        setState(0);
    }

    @Override
    public void loop() {
        follower.update();
        PoseStorage.save(follower.pose()); // handoff to teleop: where the robot really is
        autonomousUpdate();
        intake.update();
        turret.aimTurret();
        shooter.runShooter(); // always on, power from current distance every loop


        telemetry.addData("State", state);
        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));
        telemetry.addData("Follower Mode", follower.mode());
        telemetry.update();
    }

    private void autonomousUpdate() {
        switch (state) {
            case 0:

                follower.follow(path1());
                setState(1);
                break;

            case 1:
                if (!follower.isBusy()) {
                    setState(2);
                }
                break;

            case 2: // wait for the shooter to spin up, then start transfer
                if (stateTimer.seconds() > 2) {
                    intake.transferOn();
                    setState(3);                       // resets stateTimer
                }
                break;

            case 3: // let transfer run, then drive path 2
                if (stateTimer.seconds() > 3) {      // tune this number
                    intake.intakeOff();
                    follower.follow(path2());
                    setState(4);
                }
                break;

            case 4: // path 2 finished -> path 3
                if (!follower.isBusy()) {
                    follower.follow(path3());
                    setState(5);
                }
                break;

            case 5: // path 3 finished
                if (!follower.isBusy()) {
                    setState(-1);
                }
                break;
        }
    }


    private void setState(int s) {
        state = s;
        stateTimer.reset();
    }
}
