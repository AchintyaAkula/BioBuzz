package org.firstinspires.ftc.teamcode

import com.pedropathing.follower.Follower
import org.firstinspires.ftc.teamcode.pedro.Constants
import org.firstinspires.ftc.teamcode.subsystems.Limelight
import org.firstinspires.ftc.teamcode.util.OpMode

class Robot {
    val f: Follower by lazy { Constants.create(OpMode().hardwareMap) }
    val ll: Limelight by lazy { Limelight() }

    fun init() {
        ll.init()
    }
}