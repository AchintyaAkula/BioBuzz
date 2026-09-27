package org.firstinspires.ftc.teamcode

import com.pedropathing.api.Paths
import com.pedropathing.ivy.Command
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain
import org.firstinspires.ftc.teamcode.subsystems.Limelight

class Robot {
    val dt: Drivetrain by lazy { Drivetrain() }
    val ll: Limelight by lazy { Limelight() }

    fun goToTarget(): Command? {
        val targetPos = dt.motionData.pose() + (ll.relTarget() ?: return null)
        val path = Paths.line(dt.motionData.pose(), targetPos).tangent()
        return dt.follow(path)
    }

    fun init() {}
    fun update() {
        dt.update()
    }
}