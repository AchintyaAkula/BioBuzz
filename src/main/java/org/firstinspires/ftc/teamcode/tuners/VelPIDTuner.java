package org.firstinspires.ftc.teamcode.tuners;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.teamcode.Configuration;

// 0.0004, 0.06,
@Config
@TeleOp()
public class VelPIDTuner extends OpMode {
    public DcMotorEx motor;
    public static String name = "";
    public static DcMotorSimple.Direction dir = DcMotorSimple.Direction.FORWARD;
    public static Configuration.MotorConfig cfg;

    public static double target;
    public static double kP;
    public static double kS;
    public static double kV;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        cfg = new Configuration.MotorConfig(name, dir);
        motor = cfg.createMotor(hardwareMap);
    }

    @Override
    public void loop() {
        double currentVel = motor.getVelocity();
        double error = target - currentVel;
        double pow = kP * error + kS * Math.signum(target) + kV * target;

        motor.setPower(pow);

        telemetry.addData("Target Velocity", target);
        telemetry.addData("Actual Velocity", currentVel);
        telemetry.addData("Error", error);
        telemetry.addData("Power", pow);
        telemetry.update();
    }
}
