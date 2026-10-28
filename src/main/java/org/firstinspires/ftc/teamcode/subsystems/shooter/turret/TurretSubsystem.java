package org.firstinspires.ftc.teamcode.subsystems.shooter.turret;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.constants.TurretConstants;

public class TurretSubsystem {
    private final HardwareMap hardwareMap;
    private Servo turret;

    public TurretSubsystem(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public void initTurret() {
        turret = hardwareMap.get(Servo.class, "turret");
        center();
    }

    public void setAngle(double degrees) {
        double half = TurretConstants.ROM_DEGREES / 2;
        double clamped = Math.max(-half, Math.min(half, degrees));
        double t = (clamped + half) / TurretConstants.ROM_DEGREES; // 0..1 across travel
        turret.setPosition(TurretConstants.MIN_POS + t * (TurretConstants.MAX_POS - TurretConstants.MIN_POS));
    }

    public void center() {
        setAngle(0);
    }

    public void aimTurret() {
        // ill finish this later
    }
}
