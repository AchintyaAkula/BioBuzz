package org.firstinspires.ftc.teamcode.subsystems.intake;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Configuration;

@Config
public class IntakeSubsystem {

    public enum IntakeState {
        FRONT,
        BACK,
        TRANSFER,
        STOPPED
    }

    // Intake clutch servo positions — tune via Dashboard after verifying Axon PWM range
    public static double FRONT_ENGAGE_POS = 0.94;
    public static double FRONT_RELEASE_POS = 0.81;
    public static double BACK_ENGAGE_POS = 0.79;
    public static double BACK_RELEASE_POS = 0.98;

    // Transfer servo positions
    public static double TRANSFER_ENGAGE_POS = 0.1;
    public static double TRANSFER_RELEASE_POS = 0.7;

    // Motor powers
    public static double INTAKE_POWER = 1.0;
    public static double TRANSFER_POWER = 0.8;
    public static double STALL_CURRENT_MA = 3000.0;

    private final IntakeClutch clutch = new IntakeClutch();
    private final TransferClutch transfer = new TransferClutch();

    private DcMotorEx frontMotor;
    private DcMotorEx backMotor;

    private IntakeState state = IntakeState.STOPPED;

    public void init(HardwareMap hwMap) {
        frontMotor = Configuration.FRONT_INTAKE_MOTOR.createMotor(hwMap);
        frontMotor.setCurrentAlert(STALL_CURRENT_MA, CurrentUnit.MILLIAMPS);

        backMotor = Configuration.BACK_INTAKE_MOTOR.createMotor(hwMap);
        backMotor.setCurrentAlert(STALL_CURRENT_MA, CurrentUnit.MILLIAMPS);

        clutch.init(hwMap);
        transfer.init(hwMap);
    }

    /**
     * Call every loop. Handles stall protection — cuts the engaged motor if it over-currents.
     * Stall current threshold is tunable via STALL_CURRENT_MA.
     */
    public void update() {
        switch (state) {
            case FRONT:
                if (frontMotor.isOverCurrent()) frontMotor.setPower(0);
                break;
            case BACK:
                if (backMotor.isOverCurrent()) backMotor.setPower(0);
                break;
            default:
                break;
        }
    }

    public void setState(IntakeState newState) {
        switch (newState) {
            case FRONT:
                // Front roller down, back roller up. Both motors run inward.
                clutch.engageFront();
                transfer.disengage();
                frontMotor.setPower(INTAKE_POWER);
                backMotor.setPower(INTAKE_POWER);
                break;

            case BACK:
                // Back roller down, front roller up. Both motors run inward.
                clutch.engageBack();
                transfer.disengage();
                frontMotor.setPower(INTAKE_POWER);
                backMotor.setPower(INTAKE_POWER);
                break;

            case TRANSFER:
                // Servo + wheels simultaneously — kept for backward compat with existing teleops.
                // For the correct servo-first sequence use engageTransferServo() + runTransferWheels().
                transfer.engage();
                frontMotor.setPower(TRANSFER_POWER);
                backMotor.setPower(TRANSFER_POWER);
                break;

            case STOPPED:
                clutch.releaseAll();
                transfer.disengage();
                frontMotor.setPower(0);
                backMotor.setPower(0);
                break;
        }
        this.state = newState;
    }

    /** Step 1 of the proper transfer sequence: seat the servo before running wheels. */
    public void engageTransferServo() {
        transfer.engage();
    }

    /** Step 2 of the proper transfer sequence: run wheels inward after servo has seated. */
    public void runTransferWheels() {
        frontMotor.setPower(TRANSFER_POWER);
        backMotor.setPower(TRANSFER_POWER);
        this.state = IntakeState.TRANSFER;
    }

    public void stop() {
        setState(IntakeState.STOPPED);
    }

    // --- Independent gate control (does not touch motors or clutch) ---

    public void openGate() {
        transfer.disengage();
    }

    public void closeGate() {
        transfer.engage();
    }

    // --- Independent motor + clutch control (does not touch gate) ---

    /** Front roller down, both motors inward. Gate unchanged. */
    public void collectFront() {
        clutch.engageFront();
        frontMotor.setPower(INTAKE_POWER);
        backMotor.setPower(INTAKE_POWER);
        this.state = IntakeState.FRONT;
    }

    /** Back roller down, both motors inward. Gate unchanged. */
    public void collectBack() {
        clutch.engageBack();
        frontMotor.setPower(INTAKE_POWER);
        backMotor.setPower(INTAKE_POWER);
        this.state = IntakeState.BACK;
    }

    /** Stop motors only. Gate unchanged. */
    public void stopMotors() {
        frontMotor.setPower(0);
        backMotor.setPower(0);
        this.state = IntakeState.STOPPED;
    }

    public IntakeState getState() {
        return state;
    }
}
