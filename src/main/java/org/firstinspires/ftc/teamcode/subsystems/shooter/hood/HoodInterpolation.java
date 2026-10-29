package org.firstinspires.ftc.teamcode.subsystems.shooter.hood;

import com.seattlesolvers.solverslib.util.InterpLUT;
import com.seattlesolvers.solverslib.util.LUT;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

public class HoodInterpolation {
    private static final InterpLUT hoodAngle = new InterpLUT();

    static {
        // distance in inches -> rpm
        hoodAngle.add(0.0, 0.0);
        hoodAngle.add(15, 0.1);
        hoodAngle.add(25, 0.2);
        hoodAngle.add(45, 0.3);
        hoodAngle.add(53, 0.4);
        hoodAngle.add(58, 0.5);
        hoodAngle.add(63, 0.6);
        hoodAngle.add(68, 0.7);
        hoodAngle.add(19745, 0.7); // js so we dont error out if we r too far away
        hoodAngle.createLUT();
    }
    public static double returnRPM(double distance) {

        return hoodAngle.get(distance);

    }

}
