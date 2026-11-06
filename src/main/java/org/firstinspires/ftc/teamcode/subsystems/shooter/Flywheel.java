package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.Configuration;
//import org.firstinspires.ftc.teamcode.util.ShooterController;

@Config
public class Flywheel {
    private DcMotorEx frontFlywheel;
    private DcMotorEx backFlywheel;

    private boolean toggle = false;

    /*private final ShooterController frontFwPID = new ShooterController(0.0, 0.0, 0.0, 0.0);
    private final ShooterController backFwPID = new ShooterController(0.0, 0.0, 0.0, 0.0);*/

    public static double pow = 0.0;

    public void init(HardwareMap hwMap) {
        frontFlywheel = Configuration.FRONT_FLYWHEEL_MOTOR.createMotor(hwMap);
        backFlywheel = Configuration.BACK_FLYWHEEL_MOTOR.createMotor(hwMap);
    }

    public void update() {
        /*frontFwPID.setTarget(targetVel_tps);
        backFwPID.setTarget(targetVel_tps);

        frontFlywheel.setPower(frontFwPID.calculate(frontFlywheel.getVelocity()));
        backFlywheel.setPower(backFwPID.calculate(backFlywheel.getVelocity()));*/

        if (toggle) {
            frontFlywheel.setPower(pow);
            backFlywheel.setPower(pow);
        }
    }

    public void powerSwitch() {
        toggle = !toggle;
    }
    public void incrPow(double amt) {
        pow += amt;
    }
}