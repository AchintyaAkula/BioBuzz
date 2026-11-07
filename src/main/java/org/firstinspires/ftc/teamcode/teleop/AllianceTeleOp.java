package org.firstinspires.ftc.teamcode.teleop;

import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.intake.Intake;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.util.PoseStorage;

public abstract class AllianceTeleOp extends OpMode {
    private DcMotorEx fl, bl, fr, br;
    private PinpointLocalizer localizer;
    private final Intake intake = new Intake();
    private final Shooter shooter = new Shooter();

    private final boolean isRed;

    protected AllianceTeleOp(boolean isRed) {
        this.isRed = isRed;
    }

    @Override
    public void init() {
        Scheduler.reset();
        fl = hardwareMap.get(DcMotorEx.class, "fl");
        bl = hardwareMap.get(DcMotorEx.class, "bl");
        fr = hardwareMap.get(DcMotorEx.class, "fr");
        br = hardwareMap.get(DcMotorEx.class, "br");
        fr.setDirection(DcMotorSimple.Direction.REVERSE);
        br.setDirection(DcMotorSimple.Direction.REVERSE);
        localizer = new PinpointLocalizer(hardwareMap, Constants.pinpointCfg);
        intake.init(hardwareMap);
        shooter.init(hardwareMap);
    }

    @Override
    public void start() {
        // Pinpoint resets during init, so a pose set there can be lost
        localizer.setPose(PoseStorage.take(isRed));
        localizer.update();
    }

    @Override
    public void loop() {
        localizer.update();
        Pose pose = localizer.pose();

        // Field-centric mecanum from Game Manual 0
        double y = -gamepad1.left_stick_y; // Remember, this is reversed!
        double x = gamepad1.left_stick_x * 1.5; // Counteract imperfect strafing
        double rx = gamepad1.right_stick_x;

        // Pinpoint heading is a field angle. Red's driver looks along heading 90, blue's along 270
        double driverForward = Math.toRadians(isRed ? 90 : 270);
        double botHeading = pose.heading() - driverForward;
        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        double frontLeftPower = rotY + rotX + rx;
        double backLeftPower = rotY - rotX + rx;
        double frontRightPower = rotY - rotX - rx;
        double backRightPower = rotY + rotX - rx;

        double max = Math.max(
                Math.max(Math.abs(frontLeftPower), Math.abs(backLeftPower)),
                Math.max(Math.abs(frontRightPower), Math.abs(backRightPower)));
        if (max > 1) {
            frontLeftPower /= max;
            backLeftPower /= max;
            frontRightPower /= max;
            backRightPower /= max;
        }

        fl.setPower(frontLeftPower);
        bl.setPower(backLeftPower);
        fr.setPower(frontRightPower);
        br.setPower(backRightPower);

        if (gamepad1.rightBumperWasPressed() && intake.state != Intake.IntakeState.TRANSFER) {
            intake.transfer().schedule();
        } else if (intake.state != Intake.IntakeState.TRANSFER) {
            if (gamepad1.b) {
                intake.eject();
            } else if (gamepad1.right_trigger_pressed) {
                intake.frontIntake();
            } else if (gamepad1.left_trigger_pressed) {
                intake.backIntake();
            } else {
                intake.stop();
            }
        }

        if (gamepad1.leftBumperWasPressed()) {
            shooter.on();
        } else if (gamepad1.leftBumperWasReleased()) {
            shooter.off();
        }

        shooter.update(pose, isRed);
        intake.update();
        Scheduler.execute();

        telemetry.addData("Pose", pose);
        shooter.addTelemetry(telemetry);
        telemetry.update();
    }
}