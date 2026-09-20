package org.firstinspires.ftc.teamcode

import com.pedropathing.api.Paths.*;
import com.pedropathing.paths.Path;
import com.pedropathing.follower.Follower
import org.firstinspires.ftc.teamcode.subsystems.Vision

class Robot {
    val follower: Follower by lazy { Constants.create(OpMode.hardwareMap) }
    val vision: Vision = Vision()

    fun goToNearestBlob() {
        val target = vision.getTargetPose(follower.pose())
        if (target == null) return

        follower.follow(line(follower.pose(), target).tangential())
    }
}
