package org.firstinspires.ftc.teamcode.subsystems.intake;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Configuration;

class TransferClutch {
    private static final double TRANSFER_ENGAGE_POS = 0.1;
    private static final double TRANSFER_RELEASE_POS = 0.7;

    private Servo transferServo;

    public void init(HardwareMap hwMap) {
        transferServo = Configuration.TRANSFER_SERVO.createServo(hwMap);
    }

    public void engage() { transferServo.setPosition(TRANSFER_ENGAGE_POS); }
    public void disengage() { transferServo.setPosition(TRANSFER_RELEASE_POS); }
}
