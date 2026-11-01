package org.firstinspires.ftc.teamcode.test.intake.normal;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Configuration;
import org.firstinspires.ftc.teamcode.test.intake.IntakeState;

@Config(value = "Queued Transfer")
public class Subsystem {
    private DcMotor intake_f;
    private DcMotor intake_b;

    public static double passiveIntakeSpeed = -0.5;
    public static double activeIntakeSpeed = 1.0;
    public static double transferIntakeSpeed = 0.8;

    private IntakeState state = IntakeState.STOPPED;

    public void setState(IntakeState state) {
        switch (state) {
            case FRONT:
                intake_f.setPower(activeIntakeSpeed);
                intake_b.setPower(passiveIntakeSpeed);
                break;
            case BACK:
                intake_f.setPower(passiveIntakeSpeed);
                intake_b.setPower(activeIntakeSpeed);
                break;
            case TRANSFER:
                if (this.state == IntakeState.BACK) {
                    intake_f.setPower(transferIntakeSpeed);
                    intake_b.setPower(activeIntakeSpeed);
                } else {
                    intake_f.setPower(activeIntakeSpeed);
                    intake_b.setPower(transferIntakeSpeed);
                }
                break;
            case STOPPED:
                intake_f.setPower(0);
                intake_b.setPower(0);
                break;
        }
        this.state = state;
    }

    public void init(HardwareMap hwMap) {
        intake_f = Configuration.FRONT_INTAKE_MOTOR.createMotor(hwMap);
        intake_b = Configuration.BACK_INTAKE_MOTOR.createMotor(hwMap);
    }
}