package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Bottom intake rollers + transfer servo. Call update() every loop.
 * Only one state at a time:
 *  intakeOn   = transfer open, the side the robot is driving toward runs FAST, the other side OTHER_SIDE
 *  transferOn = transfer closed, both sides alternate fast/slow (like IntakeTest2) to feed the shooter
 *  intakeOff  = transfer open, intake off
 */
@Config
public class IntakeSubsystem {
    public static double FAST = 1;
    public static double SLOW = 0.8;
    public static double OTHER_SIDE = -0.2; // inactive side in intakeOn
    public static double SWAP_PERIOD = 0.2; // seconds
    public static double VELOCITY_DEADBAND = 2; // in/s, below this keep the last side

    public static String TRANSFER_NAME = "idk";
    public static double TRANSFER_OPEN = 0.4;
    public static double TRANSFER_CLOSE = 1.0;

    public enum State {
        INTAKE_ON,
        TRANSFER_ON,
        INTAKE_OFF
    }

    private final DcMotor intakeFront;
    private final DcMotor intakeBack;
    private final Servo transfer;
    private final Follower follower;
    private final ElapsedTime timer = new ElapsedTime();

    private State state = State.INTAKE_OFF;
    private boolean frontActive = true;

    public IntakeSubsystem(HardwareMap hardwareMap, Follower follower) {
        this.follower = follower;
        intakeFront = hardwareMap.get(DcMotor.class, "intakeFrontB");
        intakeBack = hardwareMap.get(DcMotor.class, "intakeBackB");
        transfer = hardwareMap.get(Servo.class, TRANSFER_NAME);
    }

    public void intakeOn() {
        setState(State.INTAKE_ON);
    }

    public void transferOn() {
        setState(State.TRANSFER_ON);
    }

    public void intakeOff() {
        setState(State.INTAKE_OFF);
    }

    private void setState(State state) {
        this.state = state;
        update();
    }

    public State getState() {
        return state;
    }

    /** true if the front side is the one running in intakeOn. */
    public boolean isFrontActive() {
        return frontActive;
    }

    public void update() {
        double forward = follower.twist().vx; // robot-relative, + = forward
        if (forward > VELOCITY_DEADBAND) frontActive = true;
        else if (forward < -VELOCITY_DEADBAND) frontActive = false;

        double front = 0.0;
        double back = 0.0;

        switch (state) {
            case INTAKE_ON:
                front = frontActive ? FAST : OTHER_SIDE;
                back = frontActive ? OTHER_SIDE : FAST;
                break;
            case TRANSFER_ON:
                // switch every 'SWAP_PERIOD' seconds
                boolean swapped = ((long) (timer.seconds() / SWAP_PERIOD)) % 2 == 1;
                front = swapped ? SLOW : FAST;
                back = swapped ? FAST : SLOW;
                break;
            case INTAKE_OFF:
                break;
        }

        intakeFront.setPower(front);
        intakeBack.setPower(back);
        transfer.setPosition(state == State.TRANSFER_ON ? TRANSFER_CLOSE : TRANSFER_OPEN);
    }
}
