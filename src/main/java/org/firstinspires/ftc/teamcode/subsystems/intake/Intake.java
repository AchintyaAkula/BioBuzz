package org.firstinspires.ftc.teamcode.subsystems.intake;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.commands.Commands;
import com.pedropathing.ivy.groups.Groups;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Configuration;

public class Intake {
    public enum IntakeState {
        FRONT,
        BACK,
        TRANSFER,
        STOPPED
    }

    private static final double FAST_INTAKE_POWER = 1.0;
    private static final double SLOW_INTAKE_POWER = 0.7;
    private static final double PASSIVE_INTAKE_POWER = -0.4;
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

    public Command frontIntake() {
        state = IntakeState.FRONT;
        return Groups.sequential(transfer.disengage(), clutch.engageFront(), Commands.instant(() -> {
                    frontIntakeMotor.setPower(FAST_INTAKE_POWER);
                    backIntakeMotor.setPower(PASSIVE_INTAKE_POWER);
                }))
                .requiring(this)
                .setPriority(0);
    }

    public Command backIntake() {
        state = IntakeState.BACK;
        return Groups.sequential(transfer.disengage(), clutch.engageBack(), Commands.instant(() -> {
                    frontIntakeMotor.setPower(PASSIVE_INTAKE_POWER);
                    backIntakeMotor.setPower(FAST_INTAKE_POWER);
                }))
                .requiring(this)
                .setPriority(0);
    }

    public Command transfer() {
        state = IntakeState.TRANSFER;
        return Groups.sequential(
                        transfer.engage(),
                        Commands.waitMs(200),
                        Commands.instant(() -> {
                            if (this.state == IntakeState.BACK) {
                                frontIntakeMotor.setPower(SLOW_INTAKE_POWER);
                                backIntakeMotor.setPower(FAST_INTAKE_POWER);
                            } else {
                                frontIntakeMotor.setPower(FAST_INTAKE_POWER);
                                backIntakeMotor.setPower(SLOW_INTAKE_POWER);
                            }
                        }),
                        Commands.waitMs(400),
                        stop())
                .requiring(this)
                .setPriority(1);
    }

    public Command stop() {
        state = IntakeState.STOPPED;
        return Groups.sequential(transfer.disengage(), Commands.instant(() -> {
                    frontIntakeMotor.setPower(0);
                    backIntakeMotor.setPower(0);
                }))
                .requiring(this)
                .setPriority(-1);
    }
}
