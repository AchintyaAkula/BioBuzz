package org.firstinspires.ftc.teamcode.subsystems.shooter.hood;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.subsystems.shooter.CalcPoseDist;
import org.firstinspires.ftc.teamcode.constants.HoodConstants;
public class HoodSubsystem {
    Servo hood;
    Follower follower;
    boolean isRedAlliance;

    public void initHood(Servo hood, boolean isRedAlliance, Follower follower) {
        this.hood = hood;
        this.follower = follower;
        this.isRedAlliance = isRedAlliance;
    }

    public void runhood() {
        double pos = HoodInterpolation.returnRPM(CalcPoseDist.calcDistance(follower, isRedAlliance));
        hood.setPosition(pos);
    }

    public void retractHood() {
        hood.setPosition(0);
    }

    public void extendHood() {
        hood.setPosition(1);
    }
}
