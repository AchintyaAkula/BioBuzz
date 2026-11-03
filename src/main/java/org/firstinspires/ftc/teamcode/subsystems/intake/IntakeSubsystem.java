package org.firstinspires.ftc.teamcode.subsystems.intake;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Configuration;

public class IntakeSubsystem {
    public enum IntakeState {
        FRONT,
        BACK,
        TRANSFER,
        STOPPED
    }

    private static final double FAST_INTAKE_POWER = 1.0;
    private static final double SLOW_INTAKE_POWER = 0.7;
    private static final double PASSIVE_INTAKE_POWER = -0.55;
    private static final double INTAKE_STALL_CURRENT_MA = 3000.0;
    private static final double VELOCITY_DEADBAND = 2; // in/s, below this keep the last side

    private final IntakeClutch clutch = new IntakeClutch();
    private final TransferClutch transfer = new TransferClutch();

    private DcMotorEx frontIntakeMotor;
    private DcMotorEx backIntakeMotor;

    private Follower follower; // only needed for intakeOn() (side picked from velocity)
    private boolean autoSide = false;
    private boolean frontActive = true;

    private IntakeState state = IntakeState.STOPPED;

    /** Use together with {@link #init(HardwareMap)}. */
    public IntakeSubsystem() {}

    /** Init immediately; the follower lets {@link #intakeOn()} pick the side from robot velocity. */
    public IntakeSubsystem(HardwareMap hwMap, Follower follower) {
        this.follower = follower;
        init(hwMap);
    }

    public void init(HardwareMap hwMap) {
        frontIntakeMotor = Configuration.FRONT_INTAKE_MOTOR.createMotor(hwMap);
        frontIntakeMotor.setCurrentAlert(INTAKE_STALL_CURRENT_MA, CurrentUnit.MILLIAMPS);

        backIntakeMotor = Configuration.BACK_INTAKE_MOTOR.createMotor(hwMap);
        backIntakeMotor.setCurrentAlert(INTAKE_STALL_CURRENT_MA, CurrentUnit.MILLIAMPS);

        clutch.init(hwMap);
        transfer.init(hwMap);
    }

    public void setState(IntakeState state) {
        autoSide = false;
        applyState(state);
    }

    public IntakeState getState() {
        return state;
    }

    /** Intake on, side chosen from the robot's forward/backward velocity. Needs the follower constructor. */
    public void intakeOn() {
        autoSide = true;
        pickSide();
        applyState(frontActive ? IntakeState.FRONT : IntakeState.BACK);
    }

    public void transferOn() {
        setState(IntakeState.TRANSFER);
    }

    public void intakeOff() {
        setState(IntakeState.STOPPED);
    }

    /** true if the front side is the one running in intakeOn. */
    public boolean isFrontActive() {
        return frontActive;
    }

    public void update() {
        if (autoSide && (state == IntakeState.FRONT || state == IntakeState.BACK)) {
            pickSide();
            IntakeState wanted = frontActive ? IntakeState.FRONT : IntakeState.BACK;
            if (wanted != state) applyState(wanted);
        }

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

    private void pickSide() {
        if (follower == null) return;
        double forward = follower.twist().vx; // robot-relative, + = forward
        if (forward > VELOCITY_DEADBAND) frontActive = true;
        else if (forward < -VELOCITY_DEADBAND) frontActive = false;
    }

    private void applyState(IntakeState state) {
        switch (state) {
            case FRONT:
                frontIntakeMotor.setPower(FAST_INTAKE_POWER);
                backIntakeMotor.setPower(PASSIVE_INTAKE_POWER);
                clutch.engageFront();
                transfer.disengage();
                break;
            case BACK:
                frontIntakeMotor.setPower(PASSIVE_INTAKE_POWER);
                backIntakeMotor.setPower(FAST_INTAKE_POWER);
                clutch.engageBack();
                transfer.disengage();
                break;
            case TRANSFER:
                transfer.engage();
                if (this.state == IntakeState.BACK) {
                    frontIntakeMotor.setPower(SLOW_INTAKE_POWER);
                    backIntakeMotor.setPower(FAST_INTAKE_POWER);
                } else {
                    frontIntakeMotor.setPower(FAST_INTAKE_POWER);
                    backIntakeMotor.setPower(SLOW_INTAKE_POWER);
                }
                break;
            case STOPPED:
                frontIntakeMotor.setPower(0.0);
                backIntakeMotor.setPower(0.0);
                transfer.disengage();
                break;
        }
        this.state = state;
    }
}
