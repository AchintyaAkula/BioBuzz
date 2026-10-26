package org.firstinspires.ftc.teamcode.subsystems.shooter;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

public class ShooterInterpolation {
    public static double returnRPM(double distance) {
        distance = Math.max(0, Math.min(68, distance));
        double[] x = {0.0, 15, 25, 45, 53, 58, 63, 68}; // distance in inches
        double[] y = {0, 1200, 2000, 2600, 2900, 3100, 3400, 3700}; // rpm

        SplineInterpolator interpolator = new SplineInterpolator();
        PolynomialSplineFunction function = interpolator.interpolate(x, y);

        return function.value(distance);
    }

}
