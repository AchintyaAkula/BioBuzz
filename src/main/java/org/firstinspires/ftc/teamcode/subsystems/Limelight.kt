package org.firstinspires.ftc.teamcode.subsystems

import com.pedropathing.math.Pose
import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.hardware.HardwareMap
import dev.frozenmilk.dairy.mercurial.processes.Channel
import dev.frozenmilk.dairy.mercurial.processes.StateMachine
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class Limelight(
    hwMap: HardwareMap,
) : StateMachine() {
    companion object {
        const val CONFIG_NAME: String = "ll"
        const val BLOB_PIPELINE: Int = 5

        const val CAMERA_HEIGHT: Double = 0.0
        const val CAMERA_PITCH: Double = 0.0
    }

    private val ll = hwMap[Limelight3A::class.java, CONFIG_NAME]

    private val switch: Channel<Mode> = Channel.single()
    private val blobPose: Channel<Pose> = Channel.single()

    private val stopped = object : Mode {
        override fun enter(previousMode: Mode) = ll.stop()
        override fun eval(): Mode = switch.poll() ?: this
    }

    private val detectBlob = object : Mode {
        override fun enter(previousMode: Mode) {
            ll.run {
                stop()
                pipelineSwitch(BLOB_PIPELINE)
                start()
            }
        }

        override fun eval(): Mode {
            val res = ll.latestResult

            if (res.isValid && res != null) {
                val ty = CAMERA_PITCH - res.ty

                val r = CAMERA_HEIGHT / tan(Math.toRadians(ty))
                val theta = Math.toRadians(90 - res.tx)

                blobPose.send(Pose(r * cos(theta), r * sin(theta), -res.tx))
            }

            return switch.poll() ?: this
        }
    }

    override fun init(): Mode = stopped

    fun stop() = switch.send(stopped)
    fun start() = switch.send(detectBlob)

    fun blobPose(): Pose = blobPose.poll() ?: Pose.zero()
}