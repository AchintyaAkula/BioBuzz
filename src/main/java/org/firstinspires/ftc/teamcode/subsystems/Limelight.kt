package org.firstinspires.ftc.teamcode.subsystems

import com.pedropathing.math.Pose
import com.qualcomm.hardware.limelightvision.Limelight3A
import org.firstinspires.ftc.teamcode.OpMode
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class Limelight {
    val ll: Limelight3A by lazy { OpMode().hardwareMap["ll"] as Limelight3A }

    fun init() {
        ll.pipelineSwitch(5)
        ll.start()
    }

    fun relTarget(): Pose? {
        val res = ll.latestResult
        if (!res.isValid || res == null) return null

        val ty = CAMERA_PITCH - res.ty

        val r = CAMERA_HEIGHT / tan(Math.toRadians(ty))
        val theta = Math.toRadians(90 - res.tx)

        return Pose(r * cos(theta), r * sin(theta), -res.tx)
    }

    companion object {
        const val CAMERA_HEIGHT = 0.0
        const val CAMERA_PITCH = 0.0
    }
}