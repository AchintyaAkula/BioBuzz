package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.seattlesolvers.solverslib.util.InterpLUT;

public class ShooterInterpolation {

    private static final InterpLUT shooterSpeed = new InterpLUT();

    static {
        // distance in inches -> rpm
        shooterSpeed.add(0.0, 0.0);
        shooterSpeed.add(15, 0.25);
        shooterSpeed.add(25, 0.4);
        shooterSpeed.add(45, 0.6);
        shooterSpeed.add(53, 0.78);
        shooterSpeed.add(58, 0.83);
        shooterSpeed.add(63, 0.88);
        shooterSpeed.add(68, 0.92);
        shooterSpeed.add(19745, 0.92); // js so we dont error out if we r too far away
        shooterSpeed.createLUT();
    }

    public static double returnRPM(double distance) {
        return shooterSpeed.get(distance);
    }
}
