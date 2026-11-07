package org.firstinspires.ftc.teamcode.util;

// least squares line through (x, y) points
public class LinearFit {
    private int count = 0;
    private double sumX = 0.0;
    private double sumY = 0.0;
    private double sumXX = 0.0;
    private double sumXY = 0.0;
    private double sumYY = 0.0;

    public void add(double x, double y) {
        count++;
        sumX += x;
        sumY += y;
        sumXX += x * x;
        sumXY += x * y;
        sumYY += y * y;
    }

    public int getCount() {
        return count;
    }

    public double getSlope() {
        return (count * sumXY - sumX * sumY) / (count * sumXX - sumX * sumX);
    }

    public double getIntercept() {
        return (sumY - getSlope() * sumX) / count;
    }

    // 1.0 means every point is on the line
    public double getRSquared() {
        double covariance = count * sumXY - sumX * sumY;
        return covariance * covariance
                / ((count * sumXX - sumX * sumX) * (count * sumYY - sumY * sumY));
    }
}
