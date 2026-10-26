package org.firstinspires.ftc.teamcode.subsystems.shooter.hood;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

public class HoodInterpolation {
    public static double returnRPM(double distance) {
        double[] x = {0.0, 15, 25, 45, 53, 58, 63, 68}; // distance in inches
        double[] y = {0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7}; // servo position

        SplineInterpolator interpolator = new SplineInterpolator();
        PolynomialSplineFunction function = interpolator.interpolate(x, y);

        return function.value(distance);
    }

}
