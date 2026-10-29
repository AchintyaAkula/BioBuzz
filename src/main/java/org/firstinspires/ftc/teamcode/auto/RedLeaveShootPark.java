package org.firstinspires.ftc.teamcode.autos;

import static com.pedropathing.api.Paths.*;

import org.firstinspires.ftc.teamcode.subsystems.IntakeState;
import org.firstinspires.ftc.teamcode.subsystems.shooter.turret.TurretSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.shooter.hood.HoodSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.shooter.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;


import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous(name = "Red Auto", group = "Red")
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

    TurretSubsystem turret = new TurretSubsystem(hardwareMap);
    HoodSubsystem hood = new HoodSubsystem();
    ShooterSubsystem shooter = new ShooterSubsystem(hardwareMap, telemetry);
    IntakeSubsystem intake = new IntakeSubsystem();

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
        Servo hoodServo = hardwareMap.get(Servo.class, "hood");
        follower = Constants.INSTANCE.create(hardwareMap);
        follower.setPose(start);
        turret.initTurret();
        shooter.initShooter(true, follower);
        hood.initHood(hoodServo, true, follower);


    }

    @Override
    public void start() {
        setState(0);
    }

    @Override
    public void loop() {
        follower.update();
        autonomousUpdate();
        turret.aimTurret();
        hood.runhood();


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
                    shooter.runShooter();
                    setState(2);
                }
                break;

            case 2: // wait for the shooter to spin up, then start transfer
                if (stateTimer.seconds() > 2) {
                    intake.setState(IntakeState.TRANSFER);
                    setState(3);                       // resets stateTimer
                }
                break;

            case 3: // let transfer run, then drive path 2
                if (stateTimer.seconds() > 3) {      // tune this number
                    intake.setState(IntakeState.STOPPED);
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
