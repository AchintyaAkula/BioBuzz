package org.firstinspires.ftc.teamcode.subsystems.intake;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.Configuration;

class IntakeClutch {
    private static final double FRONT_INTAKE_ENGAGE_POS = 0.9;
    private static final double FRONT_INTAKE_RELEASE_POS = 0.81;
    private static final double BACK_INTAKE_ENGAGE_POS = 0.81;
    private static final double BACK_INTAKE_RELEASE_POS = 0.94;

    private Servo frontIntakeClutch;
    private Servo backIntakeClutch;

    public void init(HardwareMap hwMap) {
        frontIntakeClutch = Configuration.FRONT_INTAKE_SERVO.createServo(hwMap);
        backIntakeClutch = Configuration.BACK_INTAKE_SERVO.createServo(hwMap);
    }

    public void engageFront() {
        frontIntakeClutch.setPosition(FRONT_INTAKE_ENGAGE_POS);
        backIntakeClutch.setPosition(BACK_INTAKE_RELEASE_POS);
    }

    public void engageBack() {
        frontIntakeClutch.setPosition(FRONT_INTAKE_RELEASE_POS);
        backIntakeClutch.setPosition(BACK_INTAKE_ENGAGE_POS);
    }

    public void engageBoth() {
        frontIntakeClutch.setPosition(FRONT_INTAKE_ENGAGE_POS);
        backIntakeClutch.setPosition(BACK_INTAKE_ENGAGE_POS);
    }

    public void release() {
        frontIntakeClutch.setPosition(FRONT_INTAKE_RELEASE_POS);
        backIntakeClutch.setPosition(BACK_INTAKE_RELEASE_POS);
    }
}

class TransferClutch {
    private static final double TRANSFER_ENGAGE_POS = 0.98;
    private static final double TRANSFER_RELEASE_POS = 0.6;

    private Servo transferServo;

    public void init(HardwareMap hwMap) {
        transferServo = Configuration.TRANSFER_SERVO.createServo(hwMap);
    }

    public void engage() {
        transferServo.setPosition(TRANSFER_ENGAGE_POS);
    }

    public void disengage() {
        transferServo.setPosition(TRANSFER_RELEASE_POS);
    }
}
