package org.firstinspires.ftc.teamcode.subsystems.intake;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.commands.Commands;
import com.pedropathing.ivy.groups.Groups;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Configuration;

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
    private static final double TRANSFER_ENGAGE_SEC = 0.2;

    private final IntakeClutch clutch = new IntakeClutch();
    private final TransferClutch transfer = new TransferClutch();
    private final ElapsedTime transferTimer = new ElapsedTime();

    private DcMotorEx frontIntakeMotor;
    private DcMotorEx backIntakeMotor;

    private boolean lastIntakeBack = false;
    private boolean timedTransfer = false;
    private boolean transferMotorsOn = false;
    private double transferRunSec = 0.0;

    public IntakeState state;

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
        } else if (timedTransfer) {
            updateTimedTransfer();
        }
    }

    public void frontIntake() {
        state = IntakeState.FRONT;
        lastIntakeBack = false;
        transfer.disengage();
        clutch.engageFront();
        frontIntakeMotor.setPower(FAST_INTAKE_POWER);
        backIntakeMotor.setPower(PASSIVE_INTAKE_POWER);
    }

    public void backIntake() {
        state = IntakeState.BACK;
        lastIntakeBack = true;
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
        state = IntakeState.TRANSFER;
        return Groups.sequential(
                        Commands.instant(transfer::engage),
                        Commands.waitMs(200),
                        Commands.instant(this::runTransferMotors),
                        Commands.waitMs(400),
                        Commands.instant(this::stop))
                .requiring(this)
                .setPriority(1);
    }

    // Non-Ivy transfer for autos, advanced by update()
    public void startTransfer(double runSec) {
        if (isTransferring()) {
            return;
        }
        state = IntakeState.TRANSFER;
        timedTransfer = true;
        transferRunSec = runSec;
        transferMotorsOn = false;
        transfer.engage();
        transferTimer.reset();
    }

    public boolean isTransferring() {
        return state == IntakeState.TRANSFER;
    }

    private void updateTimedTransfer() {
        if (!transferMotorsOn) {
            if (transferTimer.seconds() > TRANSFER_ENGAGE_SEC) {
                runTransferMotors();
                transferMotorsOn = true;
                transferTimer.reset();
            }
        } else if (transferTimer.seconds() > transferRunSec) {
            stop();
        }
    }

    private void runTransferMotors() {
        frontIntakeMotor.setPower(lastIntakeBack ? SLOW_INTAKE_POWER : FAST_INTAKE_POWER);
        backIntakeMotor.setPower(lastIntakeBack ? FAST_INTAKE_POWER : SLOW_INTAKE_POWER);
    }

    public void stop() {
        state = IntakeState.STOPPED;
        timedTransfer = false;
        transfer.disengage();
        frontIntakeMotor.setPower(0);
        backIntakeMotor.setPower(0);
        clutch.release();
    }
}
