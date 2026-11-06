package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.Configuration;

public class Turret {
    private static final double TURRET_ROM = 540.0;
    private static final double TURRET_0_POS = 0.5;

    private Servo turretServo;
    private double angDegNormalized = 0.0;

    public void init(HardwareMap hwMap) {
        turretServo = Configuration.TURRET_SERVO.createServo(hwMap);
    }

    public void update() {
        turretServo.setPosition(TURRET_0_POS + (angDegNormalized * (360.0 / TURRET_ROM)));
    }

    public void setAngle(double angDegNormalized) {
        this.angDegNormalized = angDegNormalized;
    }

    public static double normalize(double angDeg) {
        if (angDeg >= 180.0) {
            return angDeg - 180.0;
        } else if (angDeg < -180.0) {
            return angDeg + 180.0;
        } else {
            return angDeg;
        }
    }
}
