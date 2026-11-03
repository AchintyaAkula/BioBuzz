/*
package org.firstinspires.ftc.teamcode.auto.blue;

import static com.pedropathing.api.Paths.*;
import com.pedropathing.api.Paths;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.util.PoseStorage;
import org.firstinspires.ftc.teamcode.subsystems.intake.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.shooter.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.shooter.turret.TurretSubsystem;


@Autonomous(name = "BlueShootFarFlower", group = "Autonomous")
public class BlueShootFarFlower extends OpMode {

    private Follower follower;
    private final ElapsedTime stateTimer = new ElapsedTime();
    private int state = 0;

    // Subsystems (created in init(), because hardwareMap is not ready before that)
    private TurretSubsystem turret;
    private ShooterSubsystem shooter;
    private IntakeSubsystem intake;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(88, 136, 270);
    private final Pose shoot1 = poseFactory.of(88, 108, 270);
    private final Pose point2 = poseFactory.of(114.6273, 107.2202, 270);
    private final Pose point3 = poseFactory.of(116.032, 34.6823, 271.1095);
    private final Pose intake1 = poseFactory.of(98.6638, 12.972, 270);
    private final Pose shoot2Start = poseFactory.of(98.6638, 12.972, 270);
    private final Pose shoot2 = poseFactory.of(92.2784, 33.6606, 90);
    private final Pose point6 = poseFactory.of(132.1232, 29.574, 354.144);

    @Override
    public void init() {

        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();

        turret = new TurretSubsystem(hardwareMap);
        shooter = new ShooterSubsystem(hardwareMap, telemetry);
        intake = new IntakeSubsystem(hardwareMap, follower);

        turret.initTurret(false, follower);           // false = not red alliance
        shooter.initShooter(false, follower);         // false = not red alliance
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
                follower.follow(shoot1());
                setState(1);
                break;

            // ---------- at shoot1 ----------
            case 1: // arrived -> command 1
                if (!follower.isBusy()) {
                    setState(2);
                }
                break;

            case 2: // wait, then command 2
                if (stateTimer.seconds() > 1) {
                    intake.transferOn();
                    setState(3);
                }
                break;

            case 3: // wait, then drive path 2
                if (stateTimer.seconds() > 1) {
                    intake.intakeOff();
                    follower.follow(path2());
                    setState(4);
                }
                break;

            // ---------- path 2 and path 3: no commands, no waits ----------
            case 4: // path 2 done -> straight to path 3
                if (!follower.isBusy()) {
                    follower.follow(path3());
                    setState(5);
                }
                break;

            case 5: // path 3 done -> straight to intake spot
                if (!follower.isBusy()) {
                    intake.intakeOn(); // run while driving in, side picked from velocity
                    follower.follow(intake1());
                    setState(6);
                }
                break;

            // ---------- at intake1 ----------
            case 6: // arrived -> command 1
                if (!follower.isBusy()) {
                    intake.intakeOn();
                    setState(7);
                }
                break;

            case 7: // wait, then command 2
                if (stateTimer.seconds() > 1.25) {
                    intake.intakeOff();
                    setState(8);
                }
                break;

            case 8: // wait, then drive to shoot spot two
                if (stateTimer.seconds() > 0.5) {
                    follower.follow(shoot2());
                    setState(9);
                }
                break;

            // ---------- at shoot2 ----------
            case 9: // arrived -> command 1
                if (!follower.isBusy()) {
                    setState(10);
                }
                break;

            case 10: // wait, then command 2
                if (stateTimer.seconds() > 1) {
                    intake.transferOn();
                    setState(11);
                }
                break;

            case 11: // wait, then drive path 6
                if (stateTimer.seconds() > 1.25) {
                    intake.intakeOff();
                    follower.follow(path6());
                    setState(12);
                }
                break;

            // ---------- at path6 ----------
            case 12: // arrived -> command 1
                if (!follower.isBusy()) {
                    // ===== COMMAND 1 AFTER path6 =====
                    setState(13);
                }
                break;

            case 13: // wait, then command 2
                if (stateTimer.seconds() > 0.5) {
                    // ===== COMMAND 2 AFTER path6 =====
                    setState(14);
                }
                break;

            case 14: // final wait, then done
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

    public Path shoot1() {
        return Paths.line(start, shoot1).constant(shoot1);
    }

    public Path path2() {
        return Paths.line(shoot1, point2).constant(point2);
    }

    public Path path3() {
        return Paths.line(point2, point3).tangent();
    }

    public Path intake1() {
        return Paths.line(point3, intake1).constant(intake1);
    }

    public Path shoot2() {
        return Paths.line(shoot2Start, shoot2).linear(shoot2Start, shoot2);
    }

    public Path path6() {
        return Paths.line(shoot2, point6).tangent();
    }
}*/
