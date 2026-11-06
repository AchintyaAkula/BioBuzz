package org.firstinspires.ftc.teamcode.subsystems.intake;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.Configuration;

/** Controls the front/back intake roller clutch servos. Positions are @Config on IntakeSubsystem. */
class IntakeClutch {
    private Servo frontServo;
    private Servo backServo;

    void init(HardwareMap hwMap) {
        frontServo = Configuration.FRONT_INTAKE_SERVO.createServo(hwMap);
        backServo = Configuration.BACK_INTAKE_SERVO.createServo(hwMap);
    }

    void engageFront() {
        frontServo.setPosition(IntakeSubsystem.FRONT_ENGAGE_POS);
        backServo.setPosition(IntakeSubsystem.BACK_RELEASE_POS);
    }

    void engageBack() {
        frontServo.setPosition(IntakeSubsystem.FRONT_RELEASE_POS);
        backServo.setPosition(IntakeSubsystem.BACK_ENGAGE_POS);
    }

    void releaseAll() {
        frontServo.setPosition(IntakeSubsystem.FRONT_RELEASE_POS);
        backServo.setPosition(IntakeSubsystem.BACK_RELEASE_POS);
    }
}

/** Controls the transfer servo that seats the transfer mechanism before wheels run. */
class TransferClutch {
    private Servo transferServo;

    void init(HardwareMap hwMap) {
        transferServo = Configuration.TRANSFER_SERVO.createServo(hwMap);
    }

    void engage() {
        transferServo.setPosition(IntakeSubsystem.TRANSFER_ENGAGE_POS);
    }

    void disengage() {
        transferServo.setPosition(IntakeSubsystem.TRANSFER_RELEASE_POS);
    }
}
