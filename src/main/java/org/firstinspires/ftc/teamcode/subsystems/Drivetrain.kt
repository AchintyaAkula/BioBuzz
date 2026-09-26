package org.firstinspires.ftc.teamcode.subsystems

import com.pedropathing.follower.Follower
import org.firstinspires.ftc.teamcode.pedro.Constants
import org.firstinspires.ftc.teamcode.util.OpMode

class Drivetrain {
    val f: Follower by lazy { Constants.create(OpMode().hardwareMap) }



    fun update() {
        f.update()
    }
}