package org.firstinspires.ftc.teamcode.subsystems.intake;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.commands.Commands;
import com.pedropathing.ivy.groups.Groups;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Configuration;

@Config
public class Intake {
    public enum IntakeState {
        FRONT,
        BACK,
        TRANSFER,
        STOPPED,
        EJECT
    }

    private static final double FAST_INTAKE_POWER = 1.0;
    private static final double SLOW_INTAKE_POWER = 0.8;
    private static final double PASSIVE_INTAKE_POWER = -0.6;
    private static final double EJECT_INTAKE_POWER = -0.7;
    private static final double INTAKE_STALL_CURRENT_MA = 3000.0;
    private static double TRANSFER_FAST_POWER = 0.9;
    private static double TRANSFER_SLOW_POWER = 0.4;

    private final IntakeClutch clutch = new IntakeClutch();
    private final TransferClutch transfer = new TransferClutch();

    private DcMotorEx frontIntakeMotor;
    private DcMotorEx backIntakeMotor;

    public IntakeState state;

    // stop() runs every idle loop and clears state, so remember the side here
    private boolean lastIntakeFront = true;

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

    public void frontIntake() {
        state = IntakeState.FRONT;
        lastIntakeFront = true;
        transfer.disengage();
        clutch.engageFront();
        frontIntakeMotor.setPower(FAST_INTAKE_POWER);
        backIntakeMotor.setPower(PASSIVE_INTAKE_POWER);
    }

    public void backIntake() {
        state = IntakeState.BACK;
        lastIntakeFront = false;
        transfer.disengage();
        clutch.engageBack();
                    frontIntakeMotor.setPower(PASSIVE_INTAKE_POWER);
                    backIntakeMotor.setPower(FAST_INTAKE_POWER);
    }

    public void eject() {
        state = IntakeState.EJECT;
        backIntakeMotor.setPower(EJECT_INTAKE_POWER);
        frontIntakeMotor.setPower(EJECT_INTAKE_POWER);
    }

    public Command transfer() {
        // last intake side goes fast first, then the other side takes over
        double firstFrontPower = lastIntakeFront ? TRANSFER_FAST_POWER : TRANSFER_SLOW_POWER;
        double firstBackPower = lastIntakeFront ? TRANSFER_SLOW_POWER : TRANSFER_FAST_POWER;
        double firstFront = lastIntakeFront ? TRANSFER_FAST_POWER : -0;
        double firstBack = lastIntakeFront ? -0 : TRANSFER_FAST_POWER;

        state = IntakeState.TRANSFER;
        return Groups.sequential(
                // otherwise intake power keeps feeding balls while the clutches close
                Commands.instant(() -> {
                    frontIntakeMotor.setPower(0);
                    backIntakeMotor.setPower(0);
                }),
                Commands.instant(clutch::engageBoth),
                Commands.instant(transfer::engage),
                Commands.waitMs(300),
                Commands.instant(() -> {
                    frontIntakeMotor.setPower(firstFront);
                    backIntakeMotor.setPower(firstBack);
                }),
                Commands.waitMs(450),
                Commands.instant(() -> {
                    frontIntakeMotor.setPower(firstBackPower);
                    backIntakeMotor.setPower(firstFrontPower);
                }),
                Commands.waitMs(800),
                Commands.instant(this::stop))
                .requiring(this)
                .setPriority(1);
    }

    public void stop() {
        state = IntakeState.STOPPED;
        transfer.disengage();
                    frontIntakeMotor.setPower(0);
                    backIntakeMotor.setPower(0);
                    clutch.release();

    }
}
