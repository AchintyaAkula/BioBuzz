package org.firstinspires.ftc.teamcode.tests.camera;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
public class LimelightRelocalization {
    public static void relocalize(Limelight3A limelight, Follower follow) {
        Pose current = follow.pose();
        limelight.updateRobotOrientation(Math.toDegrees(current.heading() + Math.PI / 2));

        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            Position p = result.getBotpose_MT2().getPosition().toUnit(DistanceUnit.INCH);
            follow.setPose(new Pose(p.y + 72, 72 - p.x, current.heading()));
        }
    }
}
