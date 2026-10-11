@file:Suppress("unused")

package org.firstinspires.ftc.teamcode.subsystems

import com.pedropathing.math.Pose
import com.pedropathing.math.Vector2D
import com.pedropathing.math.Velocity
import com.qualcomm.hardware.limelightvision.LLResult
import com.qualcomm.hardware.limelightvision.LLResultTypes.DetectorResult
import com.qualcomm.hardware.limelightvision.LLStatus
import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.hardware.HardwareMap
import dev.frozenmilk.dairy.mercurial.continuations.Continuation
import dev.frozenmilk.dairy.mercurial.processes.Channel
import dev.frozenmilk.dairy.mercurial.processes.Fiber
import dev.frozenmilk.dairy.mercurial.processes.Spawnable
import dev.frozenmilk.dairy.mercurial.processes.StateMachine
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.tan

class Limelight(
    hwMap: HardwareMap
) : StateMachine() {
    private val ll: Limelight3A = hwMap[Limelight3A::class.java, LL_CF_NAME]
    private var confThreshold: Double = 0.44

    private val dataReceiver: Channel<LLResult> = Channel.single()
    private val statusReceiver: Channel<LLStatus> = Channel.single()
    private val statusForwarder: Fiber<Nothing> = Spawnable.Builder<Nothing>(
        object : Continuation {
            override fun eval(): Continuation = run {
                statusReceiver.send(ll.status)
                this
            }
        }
    ).spawnLink()

    private val requests: Channel<Mode> = Channel.single()

    private val idle = object : Mode {
        override fun enter(previousMode: Mode) = ll.stop()
        override fun eval(): Mode = requests.poll() ?: this
    }

    private val active = object : Mode {
        override fun enter(previousMode: Mode) = ll.run {
            setPollRateHz(LL_POLL_RATE_HZ)
            pipelineSwitch(LL_PIPELINE_NUM)
            start()
        }
        override fun eval(): Mode {
            with(ll.latestResult) { if (isValid && staleness < MAX_STALENESS_MS) dataReceiver.send(this) }
            return requests.poll() ?: this
        }
    }

    override fun init(): Mode = active

    fun stop() { requests.send(idle) }
    fun activate() { requests.send(active) }

    fun incrConf() { confThreshold = max(confThreshold + CONF_STEP, CONF_ABS_MAX) }
    fun decrConf() { confThreshold = min(confThreshold - CONF_STEP, CONF_ABS_MIN) }

    fun targetPose(robotPose: Pose): Pose? {
        val res = dataReceiver.poll() ?: return null
        var target: DetectorResult? = null
        for (r in res.detectorResults) {
            with(r.confidence) { if (this >= confThreshold && this > (target?.confidence ?: -1.0)) target = r }
        }
        target ?: return null

        val tx = Math.toRadians(target.targetXDegrees)
        val ty = Math.toRadians(target.targetYDegrees)
        val vertAng = Math.toRadians(LL_PITCH_DEG) - ty
        if (vertAng < 0.02) return null

        val deltaH = LL_HEIGHT_IN - BALL_HEIGHT_IN
        val relX = deltaH / tan(vertAng) + LL_X_OFFSET
        val relY = -(deltaH * cos(ty) / sin(vertAng)) * tan(tx) + LL_Y_OFFSET

        val offset = Vector2D.cartesian(relX, relY)
            .rotate(robotPose.heading())
            .apply { if (magnitude() > MAX_PLAN_RANGE) return@targetPose null }

        return robotPose.exp(
            Velocity.fromVector(
                Vector2D.polar(
                    offset.magnitude() - max(0.0, STOP_DIST - LL_X_OFFSET),
                    offset.theta(),
                ).apply { if (magnitude() < 3.0) return@targetPose robotPose }
            )
        )
    }

    companion object {
        // TODO tune the values below
        private const val LL_PITCH_DEG: Double = -9.3
        private const val LL_HEIGHT_IN = 9.5
        private const val LL_X_OFFSET = 7.625
        private const val LL_Y_OFFSET = 4.75

        private const val LL_CF_NAME: String = "ll"
        private const val LL_PIPELINE_NUM: Int = 3
        private const val LL_POLL_RATE_HZ: Int = 40

        private const val CONF_STEP = 0.05
        private const val CONF_ABS_MIN = 0.05
        private const val CONF_ABS_MAX = 0.95

        private const val BALL_HEIGHT_IN = 1.4
        private const val STOP_DIST = 16.0
        private const val MAX_PLAN_RANGE = 60.0

        private const val MAX_STALENESS_MS: Long = 120
    }
}
