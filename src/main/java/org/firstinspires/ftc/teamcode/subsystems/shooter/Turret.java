package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.Configuration;

@Config
public class Turret {

    // Physical range of motion — measure during hardware setup.
    // At ROM_DEGREES / 2 from center the servo hits its limit (mapped to pos 0.0 or 1.0).
    public static double ROM_DEGREES = 320.0;
    public static double CENTER_POS = 0.5; // servo position when turret faces forward
    public static double MIN_POS = 0.05;
    public static double MAX_POS = 0.95;

    private Servo turretServo;

    public void init(HardwareMap hwMap) {
        // NOTE: Configuration.TURRET_SERVO name is currently "" — will crash until servo is named.
        turretServo = Configuration.TURRET_SERVO.createServo(hwMap);
    }

    /** @param rad target angle in radians relative to robot forward (Pedro convention). */
    public void update(double rad) {
        setAngle(rad);
    }

    public void setAngle(double rad) {
        // Wrap to [-π, π]
        while (rad > Math.PI) rad -= 2 * Math.PI;
        while (rad < -Math.PI) rad += 2 * Math.PI;

        double pos = CENTER_POS + rad / Math.toRadians(ROM_DEGREES);
        turretServo.setPosition(Math.max(MIN_POS, Math.min(MAX_POS, pos)));
    }
}
