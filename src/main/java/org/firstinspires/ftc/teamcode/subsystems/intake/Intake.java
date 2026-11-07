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

    private final IntakeClutch clutch = new IntakeClutch();
    private final TransferClutch transfer = new TransferClutch();

    private DcMotorEx frontIntakeMotor;
    private DcMotorEx backIntakeMotor;

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
        }
    }

    public void frontIntake() {
        state = IntakeState.FRONT;
        transfer.disengage();
        clutch.engageFront();
        frontIntakeMotor.setPower(FAST_INTAKE_POWER);
        backIntakeMotor.setPower(PASSIVE_INTAKE_POWER);
    }

    public void backIntake() {
        state = IntakeState.BACK;
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
//        boolean wasFront = state == IntakeState.FRONT;
        state = IntakeState.TRANSFER;
        return Groups.sequential(
                Commands.instant(transfer::engage),
                Commands.waitMs(350),
                Commands.instant(() -> {
                    frontIntakeMotor.setPower(0.8);
                    backIntakeMotor.setPower(0.5);
                }),
                        Commands.waitMs(350),
                        Commands.instant(() -> {
                            frontIntakeMotor.setPower(0.5);
                            backIntakeMotor.setPower(0.8);
                        }),
                        Commands.waitMs(800),
                        Commands.instant(this::stop))
                .requiring(this)
                .setPriority(1);

//        if (!wasFront) {
//            return Groups.sequential(
//                            Commands.instant(transfer::engage),
//                            Commands.waitMs(350),
//                            Commands.instant(() -> {
//                                frontIntakeMotor.setPower(0.5);
//                                backIntakeMotor.setPower(0.8);
//                            }),
//                            Commands.waitMs(350),
//                            Commands.instant(() -> {
//                                frontIntakeMotor.setPower(0.8);
//                                backIntakeMotor.setPower(0.5);
//                            }),
//                            Commands.waitMs(800),
//                            Commands.instant(this::stop))
//                    .requiring(this)
//                    .setPriority(1);
//        } else {
//            return Groups.sequential(
//                            Commands.instant(transfer::engage),
//                            Commands.waitMs(350),
//                            Commands.instant(() -> {
//                                frontIntakeMotor.setPower(0.8);
//                                backIntakeMotor.setPower(0.5);
//                            }),
//                            Commands.waitMs(350),
//                            Commands.instant(() -> {
//                                frontIntakeMotor.setPower(0.5);
//                                backIntakeMotor.setPower(0.8);
//                            }),
//                            Commands.waitMs(800),
//                            Commands.instant(this::stop))
//                    .requiring(this)
//                    .setPriority(1);
//        }
    }

    public void stop() {
        state = IntakeState.STOPPED;
        transfer.disengage();
                    frontIntakeMotor.setPower(0);
                    backIntakeMotor.setPower(0);
                    clutch.release();

    }
}
