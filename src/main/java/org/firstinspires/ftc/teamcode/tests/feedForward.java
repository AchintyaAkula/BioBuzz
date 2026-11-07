package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.teamcode.subsystems.shooter.Flywheel;
import org.firstinspires.ftc.teamcode.util.LinearFit;

import java.util.ArrayList;
import java.util.List;

/**
 * Steps both flywheels through fixed powers, waits for each speed to settle,
 * then fits volts = kV * velocity + kS. kV is the slope and kS is the intercept.
 * Units are volts and rad/s, same as Flywheel.
 *
 * Clear the shooter, then press A after start. B stops the sweep.
 * Copy the "all" line into Flywheel.kV and Flywheel.kS.
 */
@Config
@TeleOp(name = "Feed Forward Test")
public class feedForward extends LinearOpMode {
    private static final String TAG = "FeedForwardTest";
    private static final int MIN_POINTS = 3;

    public static double MIN_POWER = 0.2;
    public static double MAX_POWER = 0.9;
    public static double POWER_STEP = 0.1;

    public static double WINDOW_SECONDS = 0.5;
    public static double MAX_STEP_SECONDS = 8.0;
    // rad/s between windows
    public static double STEADY_TOLERANCE = 1.0;
    // rad/s, slower wheels are stalled or have a backwards encoder
    public static double MIN_VELOCITY = 20.0;

    private final Flywheel flywheel = new Flywheel();
    private final LinearFit frontFit = new LinearFit();
    private final LinearFit backFit = new LinearFit();
    private final LinearFit allFit = new LinearFit();
    private final List<String> stepResults = new ArrayList<>();

    private VoltageSensor voltageSensor;

    @Override
    public void runOpMode() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        flywheel.init(hardwareMap);
        voltageSensor = hardwareMap.voltageSensor.iterator().next();

        telemetry.addLine("Clear the shooter. After start, A runs the sweep and B stops it.");
        telemetry.update();
        waitForStart();

        while (opModeIsActive() && !gamepad1.a) {
            idle();
        }

        boolean finished = runSweep();
        flywheel.off();

        while (opModeIsActive()) {
            telemetry.addLine(finished ? "Sweep done" : "Sweep stopped early");
            addFit("front", frontFit);
            addFit("back", backFit);
            addFit("all", allFit);
            for (String result : stepResults) {
                telemetry.addLine(result);
            }
            telemetry.update();
            sleep(100);
        }
    }

    private boolean runSweep() {
        int steps = (int) Math.round((MAX_POWER - MIN_POWER) / POWER_STEP);

        for (int i = 0; i <= steps; i++) {
            if (!measureStep(MIN_POWER + i * POWER_STEP)) {
                return false;
            }
        }
        return true;
    }

    private boolean measureStep(double power) {
        flywheel.frontFlywheel.setPower(power);
        flywheel.backFlywheel.setPower(power);

        double lastFront = Double.NaN;
        double lastBack = Double.NaN;
        ElapsedTime stepTimer = new ElapsedTime();

        while (true) {
            double front = 0.0;
            double back = 0.0;
            double battery = 0.0;
            int samples = 0;
            ElapsedTime windowTimer = new ElapsedTime();

            while (windowTimer.seconds() < WINDOW_SECONDS) {
                if (!opModeIsActive() || gamepad1.b) {
                    return false;
                }
                flywheel.update();
                front += flywheel.getFrontVelocity();
                back += flywheel.getBackVelocity();
                battery += voltageSensor.getVoltage();
                samples++;
            }
            front /= samples;
            back /= samples;
            battery /= samples;

            // NaN on the first window, so a step always takes at least two
            boolean steady = Math.abs(front - lastFront) < STEADY_TOLERANCE
                    && Math.abs(back - lastBack) < STEADY_TOLERANCE;

            if (steady || stepTimer.seconds() >= MAX_STEP_SECONDS) {
                recordStep(power, front, back, power * battery, steady);
                return true;
            }
            lastFront = front;
            lastBack = back;

            telemetry.addData("power", "%.2f", power);
            telemetry.addData("front vel", "%.1f", front);
            telemetry.addData("back vel", "%.1f", back);
            telemetry.update();
        }
    }

    private void recordStep(double power, double front, double back, double volts, boolean steady) {
        if (front >= MIN_VELOCITY) {
            frontFit.add(front, volts);
            allFit.add(front, volts);
        }
        if (back >= MIN_VELOCITY) {
            backFit.add(back, volts);
            allFit.add(back, volts);
        }

        String result = String.format("%.2f power: %.2f V, front %.1f, back %.1f rad/s%s",
                power, volts, front, back, steady ? "" : " (not settled)");
        stepResults.add(result);
        RobotLog.dd(TAG, result);
    }

    private void addFit(String name, LinearFit fit) {
        if (fit.getCount() < MIN_POINTS) {
            telemetry.addData(name, "only %d points, check encoder sign", fit.getCount());
            return;
        }
        telemetry.addData(name, "kV %.5f, kS %.3f, R2 %.4f, %d points",
                fit.getSlope(), fit.getIntercept(), fit.getRSquared(), fit.getCount());
    }
}
