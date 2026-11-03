package org.firstinspires.ftc.teamcode.teleop;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.intake.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.DrivetrainSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.shooter.CalcPoseDist;
import org.firstinspires.ftc.teamcode.subsystems.shooter.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.shooter.ShooterTarget;
import org.firstinspires.ftc.teamcode.subsystems.shooter.turret.TurretSubsystem;
import org.firstinspires.ftc.teamcode.util.PoseStorage;

/**
 * Shared teleop.
 * Controls (gamepad1):
 *  left stick    = translate (robot-centric)
 *  right stick X = rotate
 *  left bumper   = hold to shoot (transferOn: servo closed + alternating intake). Has priority over intaking.
 *  right bumper  = hold to intake (intakeOn: servo open + side picked by velocity)
 *  neither held  = intakeOff (servo open, intake off)
 *  A             = relocalize (pinpoint pose snapped to nearby walls)
 *  B             = toggle shooter motors (on by default)
 * Shooter power and turret aim are recomputed every loop from the robot pose.
 * Start pose = where auto ended (PoseStorage), or (72, 72, 90deg) if no auto ran.
 * Target top is picked from robot y: y &lt; 72 shoots the low-y top, else the high-y top.
 */
public abstract class TeleOpBase extends OpMode {
    private final boolean isRed;

    private Follower follower;
    private DrivetrainSubsystem drivetrain;
    private TurretSubsystem turret;
    private ShooterSubsystem shooter;
    private IntakeSubsystem intake;

    private boolean shooterOn = true;

    protected TeleOpBase(boolean isRed) {
        this.isRed = isRed;
    }

    @Override
    public void init() {
        follower = Constants.create(hardwareMap);
        // where auto actually ended, or (72, 72, 90deg) if no auto ran
        follower.setPose(PoseStorage.take());

        drivetrain = new DrivetrainSubsystem(follower);

        turret = new TurretSubsystem(hardwareMap);
        turret.initTurret(isRed, follower);

        shooter = new ShooterSubsystem(hardwareMap, telemetry);
        shooter.initShooter(isRed, follower);

        intake = new IntakeSubsystem(hardwareMap, follower);
    }

    @Override
    public void start() {
        shooterOn = true;
    }

    @Override
    public void loop() {
        if (gamepad1.aWasPressed()) {
            drivetrain.relocalize();
        }

        // pedro convention: +strafe = left, +turn = counter-clockwise
        follower.manual(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
        follower.update();

        boolean shooting = gamepad1.left_bumper; // hold
        boolean intaking = gamepad1.right_bumper; // hold
        if (gamepad1.bWasPressed()) shooterOn = !shooterOn;

        // shooting wins over intaking
        if (shooting) intake.transferOn();
        else if (intaking) intake.intakeOn();
        else intake.intakeOff();

        turret.aimTurret();
        double shooterPower = 0;
        if (shooterOn) shooterPower = shooter.runShooter(); // power from current distance, every loop
        else shooter.stopShooter();

        Pose p = follower.pose();
        boolean lowY = ShooterTarget.lowY(p, null);
        telemetry.addData("alliance", isRed ? "RED" : "BLUE");
        telemetry.addData("target", "(%.0f, %.0f)", ShooterTarget.x(isRed), ShooterTarget.y(lowY));
        telemetry.addData("distance", "%.1f", CalcPoseDist.calcDistance(follower, isRed));
        telemetry.addData("shooter", shooterOn ? "ON" : "OFF (B)");
        telemetry.addData("shooter power", "%.2f", shooterPower);
        telemetry.addData("intake", shooting ? "SHOOTING (transfer closed, alternating)"
                : intaking ? "INTAKING " + (intake.isFrontActive() ? "FRONT" : "BACK") + " (transfer open)"
                : "OFF (transfer open)");
        telemetry.addData("pose", "x=%.1f y=%.1f h=%.0fdeg", p.x(), p.y(), Math.toDegrees(p.heading()));
        telemetry.update();
    }

    @Override
    public void stop() {
        follower.stop();
        follower.update();
        intake.intakeOff();
        shooter.stopShooter();
    }
}
