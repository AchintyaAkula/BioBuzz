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

    public static double fast = 0.5;
    // change these for comp, js kept them bc they are too loud and annoying while testing and didnt want to contstantly have to switch them in ftc dashboard.
    public static double slow = 0.4;

    public static String TRANSFER_NAME = "idk";
    public static double open = 0.0;
    public static double close = 1.0;

    public static boolean openTransfer = true;

    private DcMotor intakeBackT, intakeFrontT, intakeBackB, intakeFrontB;
    private Servo transfer;

    @Override
    public void init() {
        intakeBackT = hardwareMap.get(DcMotor.class, "intakeBackT");
        intakeFrontT = hardwareMap.get(DcMotor.class, "intakeFrontT");
        intakeBackB = hardwareMap.get(DcMotor.class, "intakeBackB");
        intakeFrontB = hardwareMap.get(DcMotor.class, "intakeFrontB");
        transfer = hardwareMap.get(Servo.class, TRANSFER_NAME);
    }

    private void setPower(double front, double back) {
        intakeFrontT.setPower(front);
        intakeFrontB.setPower(front);
        intakeBackT.setPower(back);
        intakeBackB.setPower(back);
    }

    @Override
    public void loop() {
        double front = 0.0;
        double back = 0.0;

        if (gamepad1.right_trigger > 0.0) {
            front = fast;
            back = slow;
        } else if (gamepad1.left_trigger > 0.0) {
            front = slow;
            back = fast;
        } else {

            if (gamepad1.a) front = fast;
            if (gamepad1.b) back = fast;
        }

        setPower(front, back);

        if (gamepad1.rightBumperWasPressed()) {
            openTransfer = !openTransfer;
        }
        transfer.setPosition(openTransfer ? open : close);
    }
}
