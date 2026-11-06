package org.firstinspires.ftc.teamcode.tuners;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.List;
import org.firstinspires.ftc.teamcode.subsystems.intake.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Flywheel;

/**
 * Full intake -> transfer -> shoot test opmode.
 *
 * Gate and motors are fully independent:
 *   Right trigger       -- front roller down, both motors inward (gate unaffected)
 *   Left trigger        -- back roller down, both motors inward (gate unaffected)
 *   Right bumper (hold) -- gate CLOSES (motors unaffected)
 *   Release bumper      -- gate OPENS
 *   Release triggers    -- motors stop (gate unaffected)
 *   A (toggle)          -- flywheel on/off
 *   Dpad Up/Down        -- TARGET_VEL_TPS +/- 100 tps per press
 *
 * Typical shoot sequence:
 *   1. Hold RT/LT to collect -- gate open, ball enters robot
 *   2. Press A to spin up flywheel -- collect and spool simultaneously
 *   3. Hold RB -- gate closes, redirects ball upward into flywheel
 *   4. Release RB -- gate opens, keep collecting (RT/LT still held)
 *
 * Flywheel tuning (kV -> kP -> kS):
 *   1. Increase kV until flywheel reaches ~90% of target steadily.
 *   2. Add kP to close remaining steady-state error.
 *   3. Add small kS (~0.05) only if flywheel hesitates from rest.
 */
@Config
@TeleOp(name = "Intake Shooter Test", group = "Tuning")
public class IntakeShooterTest extends OpMode {

    private IntakeSubsystem intake;
    private Flywheel flywheel;
    private List<LynxModule> hubs;
    private FtcDashboard dashboard;

    private boolean flywheelEnabled = false;
    private boolean prevA = false;
    private boolean prevDpadUp = false;
    private boolean prevDpadDown = false;

    @Override
    public void init() {
        hubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : hubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        intake = new IntakeSubsystem();
        intake.init(hardwareMap);

        flywheel = new Flywheel();
        flywheel.init(hardwareMap);

        dashboard = FtcDashboard.getInstance();

        telemetry.addLine("RT=front  LT=back  RB=gate  A=flywheel  Dpad=speed");
        telemetry.update();
    }

    @Override
    public void loop() {
        for (LynxModule hub : hubs) {
            hub.clearBulkCache();
        }

        handleFlywheel();
        handleIntake();
        sendTelemetry();
    }

    private void handleFlywheel() {
        boolean a = gamepad1.a;
        if (a && !prevA) flywheelEnabled = !flywheelEnabled;
        prevA = a;

        boolean dpadUp = gamepad1.dpad_up;
        boolean dpadDown = gamepad1.dpad_down;
        if (dpadUp && !prevDpadUp) Flywheel.TARGET_VEL_TPS += 100;
        if (dpadDown && !prevDpadDown) Flywheel.TARGET_VEL_TPS = Math.max(0, Flywheel.TARGET_VEL_TPS - 100);
        prevDpadUp = dpadUp;
        prevDpadDown = dpadDown;

        if (flywheelEnabled) {
            flywheel.setTargetVel(Flywheel.TARGET_VEL_TPS);
            flywheel.update();
        } else {
            flywheel.disable();
        }
    }

    private void handleIntake() {
        // Gate: RB controls independently -- close to redirect ball up, open to let balls flow
        if (gamepad1.right_bumper) {
            intake.closeGate();
        } else {
            intake.openGate();
        }

        // Motors + clutch: triggers control independently of gate
        if (gamepad1.right_trigger > 0.1) {
            intake.collectFront();
        } else if (gamepad1.left_trigger > 0.1) {
            intake.collectBack();
        } else {
            intake.stopMotors();
        }

        intake.update();
    }

    private void sendTelemetry() {
        double frontVel = flywheel.getFrontVelocity();
        double backVel = flywheel.getBackVelocity();
        double target = Flywheel.TARGET_VEL_TPS;

        TelemetryPacket packet = new TelemetryPacket();
        packet.put("flywheel/target", target);
        packet.put("flywheel/front", frontVel);
        packet.put("flywheel/back", backVel);
        packet.put("flywheel/front_err", target - frontVel);
        packet.put("flywheel/back_err", target - backVel);
        packet.put("intake/state", intake.getState().name());
        packet.put("intake/gate", gamepad1.right_bumper ? "CLOSED" : "open");
        dashboard.sendTelemetryPacket(packet);

        telemetry.addData("Flywheel", flywheelEnabled ? "ON" : "off");
        telemetry.addData("Target (tps)", "%.0f", target);
        telemetry.addData("Front / Back", "%.0f / %.0f", frontVel, backVel);
        telemetry.addData("Gate", gamepad1.right_bumper ? "CLOSED" : "open");
        telemetry.addData("Intake", intake.getState());
        telemetry.update();
    }
}
