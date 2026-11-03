package org.firstinspires.ftc.teamcode.subsystems.intake;

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

    public void engageFront() {
        frontIntakeClutch.setPosition(FRONT_INTAKE_ENGAGE_POS);
        backIntakeClutch.setPosition(BACK_INTAKE_RELEASE_POS);
    }
    public void engageBack() {
        frontIntakeClutch.setPosition(FRONT_INTAKE_RELEASE_POS);
        backIntakeClutch.setPosition(BACK_INTAKE_ENGAGE_POS);
    }
}
