package org.firstinspires.ftc.teamcode.test.intake.normal;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Configuration;
import org.firstinspires.ftc.teamcode.test.intake.IntakeState;

public class Test extends OpMode {
    private Subsystem intake = new Subsystem();

    private DcMotor fw1;
    private DcMotor fw2;

    public static double FW_POWER = 0.5;

    private boolean flywheelRunning = false;

    @Override
    public void init() {
        intake.init(hardwareMap);
        fw1 = Configuration.FRONT_FLYWHEEL_MOTOR.createMotor(hardwareMap);
        fw2 = Configuration.BACK_FLYWHEEL_MOTOR.createMotor(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad1.right_trigger > 0) {
            intake.setState(IntakeState.FRONT);
        } else if (gamepad1.left_trigger  > 0) {
            intake.setState(IntakeState.BACK);
        } else {
            intake.setState(IntakeState.STOPPED);
        }

        if (gamepad1.right_bumper) {
            intake.setState(IntakeState.TRANSFER);
        }

        if (gamepad1.leftBumperWasPressed()) {
            flywheelRunning = !flywheelRunning;
        }

        if (flywheelRunning) {
            fw1.setPower(FW_POWER);
            fw2.setPower(FW_POWER);
        } else {
            fw1.setPower(0);
            fw2.setPower(0);
        }
    }
}
