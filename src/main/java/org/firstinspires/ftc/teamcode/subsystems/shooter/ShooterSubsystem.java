package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.DcMotorEx;
public class ShooterSubsystem {
    private final Telemetry telemetry;
    private final HardwareMap hardwareMap;

    DcMotorEx shooter1;
    DcMotorEx shooter2;
    public ShooterSubsystem(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
    }

    public void init() {
        shooter1 = hardwareMap.get(DcMotorEx.class, "shooter1");
    }

}
