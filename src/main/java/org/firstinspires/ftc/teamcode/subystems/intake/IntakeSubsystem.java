package org.firstinspires.ftc.teamcode.subystems.intake;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Configuration;

public class IntakeSubsystem {
    public enum IntakeState {
        FRONT,
        BACK,
        TRANSFER,
        STOPPED
    }

    private static final double FAST_INTAKE_POWER = 1.0;
    private static final double SLOW_INTAKE_POWER = 0.8;
    private static final double PASSIVE_INTAKE_POWER = -0.5;
    private static final double INTAKE_STALL_CURRENT_MA = 3000.0;

    private final IntakeClutch clutch = new IntakeClutch();
    private final TransferClutch transfer = new TransferClutch();

    private DcMotorEx frontIntakeMotor;
    private DcMotorEx backIntakeMotor;

    private IntakeState state;

    public void init(HardwareMap hwMap) {
        frontIntakeMotor = Configuration.FRONT_INTAKE_MOTOR.createMotor(hwMap);
        frontIntakeMotor.setCurrentAlert(INTAKE_STALL_CURRENT_MA, CurrentUnit.MILLIAMPS);

        backIntakeMotor = Configuration.BACK_INTAKE_MOTOR.createMotor(hwMap);
        backIntakeMotor.setCurrentAlert(INTAKE_STALL_CURRENT_MA, CurrentUnit.MILLIAMPS);

        clutch.init(hwMap);
        transfer.init(hwMap);
    }

    public void setState(IntakeState state) {
        switch (state) {
            case FRONT:
                frontIntakeMotor.setPower(FAST_INTAKE_POWER);
                backIntakeMotor.setPower(PASSIVE_INTAKE_POWER);
                break;
            case BACK:
                frontIntakeMotor.setPower(PASSIVE_INTAKE_POWER);
                backIntakeMotor.setPower(FAST_INTAKE_POWER);
                break;
            case TRANSFER:
                transfer.engage();
                if (this.state == IntakeState.BACK) {
                    frontIntakeMotor.setPower(SLOW_INTAKE_POWER);
                    backIntakeMotor.setPower(FAST_INTAKE_POWER);
                } else {
                    frontIntakeMotor.setPower(FAST_INTAKE_POWER);
                    backIntakeMotor.setPower(SLOW_INTAKE_POWER);
                }
                break;
            case STOPPED:
                frontIntakeMotor.setPower(0.0);
                backIntakeMotor.setPower(0.0);
                break;
        }
        this.state = state;
    }

    public void update() {
        if (state == IntakeState.FRONT) {
            if (backIntakeMotor.isOverCurrent()) {
                backIntakeMotor.setPower(0);
            }
        } else if (state == IntakeState.BACK) {
            if (frontIntakeMotor.isOverCurrent()) {
                frontIntakeMotor.setPower(0);
            }
        }
    }
}
