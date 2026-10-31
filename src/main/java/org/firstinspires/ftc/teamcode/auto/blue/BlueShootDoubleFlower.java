package org.firstinspires.ftc.teamcode.auto.blue;

import static com.pedropathing.api.Paths.*;
import com.pedropathing.api.Paths;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.IntakeState;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.shooter.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.shooter.hood.HoodSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.shooter.turret.TurretSubsystem;


@Autonomous(name = "BlueShootDoubleFlower", group = "Autonomous")
public class BlueShootDoubleFlower extends OpMode {

    private Follower follower;
    private final ElapsedTime stateTimer = new ElapsedTime();
    private int state = 0;

    private TurretSubsystem turret;
    private HoodSubsystem hood;
    private ShooterSubsystem shooter;
    private IntakeSubsystem intake;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(88, 136, 270);
    private final Pose endshootone = poseFactory.of(88.5108, 115.5347, 270);
    private final Pose flower1 = poseFactory.of(135.9544, 97.1313, 0);
    private final Pose point3Start = poseFactory.of(135.9544, 97.1313, 0);
    private final Pose point3 = poseFactory.of(110.2852, 33.9161, 90);
    private final Pose endshoottwo = poseFactory.of(86.1485, 33.5329, 90);
    private final Pose point5 = poseFactory.of(97.259, 11.0564, 90);
    private final Pose point6 = poseFactory.of(86.6593, 30.468, 90);
    private final Pose point7 = poseFactory.of(132.3786, 30.8511, 0.4801);

    @Override
    public void init() {
        Servo hoodServo = hardwareMap.get(Servo.class, "hood");

        follower = Constants.INSTANCE.create(hardwareMap);
        follower.setPose(start);
        follower.update();

        turret = new TurretSubsystem(hardwareMap);
        hood = new HoodSubsystem();
        shooter = new ShooterSubsystem(hardwareMap, telemetry);
        intake = new IntakeSubsystem();

        turret.initTurret();
        shooter.initShooter(false, follower);         // false = not red alliance
        hood.initHood(hoodServo, false, follower);    // false = not red alliance
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

        telemetry.addData("state", state);
        telemetry.addData("x", follower.pose().x());
        telemetry.addData("y", follower.pose().y());
        telemetry.addData("heading", follower.pose().heading());

        if (follower.currentPath() != null) {
            telemetry.addData("Current path distance remaining", follower.distanceToEndpoint());
            telemetry.addData("Path number", follower.pathIndex());
        }

        telemetry.update();
    }

    private void autonomousUpdate() {
        switch (state) {
            case 0: // drive to shoot spot one
                follower.follow(endshootone());
                setState(1);
                break;

            // ---------- at endshootone ----------
            case 1: // arrived -> command 1
                if (!follower.isBusy()) {
                    shooter.runShooter();
                    setState(2);
                }
                break;

            case 2: // wait, then command 2
                if (stateTimer.seconds() > 0.5) {
                    intake.setState(IntakeState.TRANSFER);
                    setState(3);
                }
                break;

            case 3: // wait, then drive to flower
                if (stateTimer.seconds() > 1) {
                    intake.setState(IntakeState.STOPPED);
                    shooter.stopShooter();
                    follower.follow(flower1());
                    setState(4);
                }
                break;

            // ---------- at flower1 ----------
            case 4: // arrived -> command 1
                if (!follower.isBusy()) {
                    // ===== COMMAND 1 AFTER flower1 ====
                    intake.setState(IntakeState.FRONT);
                    setState(5);
                }
                break;

            case 5: // wait, then command 2
                if (stateTimer.seconds() > 1) {
                    intake.setState(IntakeState.STOPPED);
                    setState(6);
                }
                break;

            case 6: // wait, then drive path 3
                if (stateTimer.seconds() > 0.5) {
                    follower.follow(path3());
                    setState(7);
                }
                break;

            // ---------- path 3: no commands, no waits ----------
            case 7: // path 3 done -> straight to shoot spot two
                if (!follower.isBusy()) {
                    follower.follow(endshoottwo());
                    setState(8);
                }
                break;

            // ---------- at endshoottwo ----------
            case 8: // arrived -> command 1
                if (!follower.isBusy()) {
                    shooter.runShooter();
                    setState(9);
                }
                break;

            case 9: // wait, then command 2
                if (stateTimer.seconds() > 1) {
                    intake.setState(IntakeState.TRANSFER);
                    setState(10);
                }
                break;

            case 10: // wait, then drive path 5
                if (stateTimer.seconds() > 1) {
                    intake.setState(IntakeState.STOPPED);
                    shooter.stopShooter();
                    follower.follow(path5());
                    setState(11);
                }
                break;

            // ---------- at path5 ----------
            case 11: // arrived -> command 1
                if (!follower.isBusy()) {
                    intake.setState(IntakeState.FRONT);
                    shooter.runShooter();
                    setState(12);
                }
                break;

            case 12: // wait, then command 2
                if (stateTimer.seconds() > 1) {
                    intake.setState(IntakeState.STOPPED);
                    intake.setState(IntakeState.TRANSFER);
                    setState(13);
                }
                break;

            case 13: // wait, then drive path 6
                if (stateTimer.seconds() > 0.5) {
                    follower.follow(path6());
                    setState(14);
                }
                break;

            // ---------- at path6 ----------
            case 14: // arrived -> command 1
                if (!follower.isBusy()) {
                    // ===== COMMAND 1 AFTER path6 =====
                    setState(15);
                }
                break;

            case 15: // wait, then command 2
                if (stateTimer.seconds() > 0.5) {
                    // ===== COMMAND 2 AFTER path6 =====
                    setState(16);
                }
                break;

            case 16: // wait, then drive path 7
                if (stateTimer.seconds() > 0.5) {
                    follower.follow(path7());
                    setState(17);
                }
                break;

            // ---------- at path7 ----------
            case 17: // arrived -> command 1
                if (!follower.isBusy()) {
                    // ===== COMMAND 1 AFTER path7 =====
                    setState(18);
                }
                break;

            case 18: // wait, then command 2
                if (stateTimer.seconds() > 0.5) {
                    // ===== COMMAND 2 AFTER path7 =====
                    setState(19);
                }
                break;

            case 19: // final wait, then done
                if (stateTimer.seconds() > 0.5) {
                    setState(-1);
                }
                break;
        }
    }

    private void setState(int s) {
        state = s;
        stateTimer.reset();
    }

    // ---------- Paths ----------

    public Path endshootone() {
        return Paths.line(start, endshootone).linear(start, endshootone);
    }

    public Path flower1() {
        return Paths.line(endshootone, flower1).linear(endshootone, flower1);
    }

    public Path path3() {
        return Paths.line(point3Start, point3).linear(point3Start, point3);
    }

    public Path endshoottwo() {
        return Paths.line(point3, endshoottwo).constant(endshoottwo);
    }

    public Path path5() {
        return Paths.line(endshoottwo, point5).constant(point5);
    }

    public Path path6() {
        return Paths.line(point5, point6).constant(point6);
    }

    public Path path7() {
        return Paths.line(point6, point7).tangent();
    }
}