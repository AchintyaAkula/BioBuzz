package org.firstinspires.ftc.teamcode.util;

import com.pedropathing.math.Pose;

public enum Alliance {
    RED(Pose.zero().withX(57.359).withY(57.981), Pose.zero().withX(57.359).withY(141.5 - 57.981)),
    BLUE(
            Pose.zero().withX(141.5 - 57.359).withY(57.981),
            Pose.zero().withX(141.5 - 57.359).withY(141.5 - 57.981));

    public final Pose turretTargetClose;
    public final Pose turretTargetFar;

    Alliance(Pose turretTargetClose, Pose turretTargetFar) {
        this.turretTargetClose = turretTargetClose;
        this.turretTargetFar = turretTargetFar;
    }
}
