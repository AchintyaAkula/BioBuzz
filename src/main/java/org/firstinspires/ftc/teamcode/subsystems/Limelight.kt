package org.firstinspires.ftc.teamcode.subsystems

import com.pedropathing.math.Pose
import com.qualcomm.hardware.limelightvision.Limelight3A
import org.firstinspires.ftc.teamcode.util.OpMode
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class Limelight {
    // TODO change to actual config name
    val ll: Limelight3A by lazy { OpMode().hardwareMap["ll"] as Limelight3A }

    fun init() {
        // TODO change to actual pipeline
        ll.pipelineSwitch(5)
        ll.start()
    }

    fun targetPos(): Pose? {
        val res = ll.latestResult
        if (!res.isValid || res == null) return null

        val ty = CAMERA_PITCH - res.ty

        val r = CAMERA_HEIGHT / tan(Math.toRadians(ty))
        val theta = Math.toRadians(90 - res.tx)

        return Pose(r * cos(theta), r * sin(theta), -res.tx)
    }

    companion object {
        // TODO change to actual height of camera from ground
        const val CAMERA_HEIGHT = 0.0
        // TODO change to actual pitch of camera relative to facing forward
        // The more facing downward, the higher the value
        const val CAMERA_PITCH = 0.0
    }
}