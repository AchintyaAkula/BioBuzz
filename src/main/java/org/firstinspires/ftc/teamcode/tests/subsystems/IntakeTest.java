package org.firstinspires.ftc.teamcode.tests.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.acmerobotics.dashboard.config.Config;

@Config
@TeleOp(name = "IntakeTest", group = "Test")
public class IntakeTest extends OpMode {

    public static double fast = 1; // fyi when testing lower these to 0.7, and 0.5 otherwise you will loose hearing
    public static double slow = 0.8;
    public static double swapPeriod = 0.2; // seconds

    public static String TRANSFER_NAME = "idk";
    public static double open = 0.4;
    public static double close = 1.0;

    public static double shooterPower = 0.8; // temporary, since LUT is not tuned

    public static boolean openTransfer = true;
    public static boolean shooterOn = false;

    private DcMotor shooterBack, shooterFront, intakeBack, intakeFront;
    private Servo transfer;

    @Override
    public void init() {
        shooterBack = hardwareMap.get(DcMotor.class, "intakeBackT");
        shooterFront = hardwareMap.get(DcMotor.class, "intakeFrontT");

        intakeBack = hardwareMap.get(DcMotor.class, "intakeBackB");
        intakeFront = hardwareMap.get(DcMotor.class, "intakeFrontB");

        transfer = hardwareMap.get(Servo.class, TRANSFER_NAME);
    }

    private void setPower(double front, double back, double shooter) {
        intakeFront.setPower(front);
        intakeBack.setPower(back);

        shooterBack.setPower(shooter);
        shooterFront.setPower(shooter);
    }

    @Override
    public void loop() {
        double front = 0.0;
        double back = 0.0;

        if (gamepad1.right_trigger > 0.0) {
            // switch every 'swapPeriod' seconds
            boolean swapped = ((long) (getRuntime() / swapPeriod)) % 2 == 1;
            front = swapped ? slow : fast;
            back = swapped ? fast : slow;

        } else if (gamepad1.left_trigger > 0.0) {
            front = slow;
            back = fast;
        } else {

            if (gamepad1.a) front = fast;
            if (gamepad1.b) back = fast;
        }

        if (gamepad1.leftBumperWasPressed()) {
            shooterOn = !shooterOn;
        }
        double shooter = shooterOn ? shooterPower : 0.0;

        setPower(front, back, shooter);

        if (gamepad1.rightBumperWasPressed()) {
            openTransfer = !openTransfer;
        }
        transfer.setPosition(openTransfer ? open : close);
    }
}
