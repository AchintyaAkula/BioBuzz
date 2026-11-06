package org.firstinspires.ftc.teamcode.subsystems.intake;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.commands.Commands;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.Configuration;

class IntakeClutch {
    private static final double FRONT_INTAKE_ENGAGE_POS = 0.94;
    private static final double FRONT_INTAKE_RELEASE_POS = 0.81;
    private static final double BACK_INTAKE_ENGAGE_POS = 0.79;
    private static final double BACK_INTAKE_RELEASE_POS = 0.98;

    private Servo frontIntakeClutch;
    private Servo backIntakeClutch;

    public void init(HardwareMap hwMap) {
        frontIntakeClutch = Configuration.FRONT_INTAKE_SERVO.createServo(hwMap);
        backIntakeClutch = Configuration.BACK_INTAKE_SERVO.createServo(hwMap);
    }

    public Command engageFront() {
        return Commands.instant(() -> {
            frontIntakeClutch.setPosition(FRONT_INTAKE_ENGAGE_POS);
            backIntakeClutch.setPosition(BACK_INTAKE_RELEASE_POS);
        });
    }

    public Command engageBack() {
        return Commands.instant(() -> {
            frontIntakeClutch.setPosition(FRONT_INTAKE_RELEASE_POS);
            backIntakeClutch.setPosition(BACK_INTAKE_ENGAGE_POS);
        });
    }
}

class TransferClutch {
    private static final double TRANSFER_ENGAGE_POS = 0.1;
    private static final double TRANSFER_RELEASE_POS = 0.7;

    private Servo transferServo;

    public void init(HardwareMap hwMap) {
        transferServo = Configuration.TRANSFER_SERVO.createServo(hwMap);
    }

    public Command engage() {
        return Commands.instant(() -> transferServo.setPosition(TRANSFER_ENGAGE_POS));
    }

    public Command disengage() {
        return Commands.instant(() -> transferServo.setPosition(TRANSFER_RELEASE_POS));
    }
}
