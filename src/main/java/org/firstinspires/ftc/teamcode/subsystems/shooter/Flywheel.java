package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.Configuration;

public class Flywheel {
    private DcMotorEx frontFlywheel;
    private DcMotorEx backFlywheel;

    private double pow = 0.0;
    private boolean on = false;

    public void init(HardwareMap hwMap) {
        frontFlywheel = Configuration.FRONT_FLYWHEEL_MOTOR.createMotor(hwMap);
        backFlywheel = Configuration.BACK_FLYWHEEL_MOTOR.createMotor(hwMap);
    }

    public void update() {
        if (on) {
            frontFlywheel.setPower(pow);
            backFlywheel.setPower(pow);
        }
    }

    public void togglePower() { on = !on; }
    public void setPower(double pow) { this.pow = pow; }
}