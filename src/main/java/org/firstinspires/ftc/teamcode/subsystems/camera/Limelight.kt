@file:Suppress("unused")

package org.firstinspires.ftc.teamcode.subsystems.camera

import com.pedropathing.math.Pose
import com.pedropathing.math.Vector2D
import com.pedropathing.math.Velocity
import com.qualcomm.hardware.limelightvision.LLResult
import com.qualcomm.hardware.limelightvision.LLResultTypes.DetectorResult
import com.qualcomm.hardware.limelightvision.LLStatus
import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.teamcode.util.OpMode
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin
import kotlin.math.tan

//TODO we need to look at this and make sure its all fine, bcos it looks very cursed right now
class Limelight {
    private val ll: Limelight3A by lazy {
        OpMode().hardwareMap.get(Limelight3A::class.java, LL_CF_NAME)
    }
    private lateinit var currStatus: LLStatus
    private lateinit var currResult: LLResult
    private var confThreshold = 0.44

    fun getTargetPose(robotPose: Pose): Pose? {
        val target: DetectorResult = getBestValidResult() ?: return null

        val tx = Math.toRadians(target.targetXDegrees)
        val ty = Math.toRadians(target.targetYDegrees)
        val vertAng = Math.toRadians(LL_PITCH_DEG) - ty
        val deltaH = LL_HEIGHT_IN - BALL_HEIGHT_IN

        if (vertAng < 0.02) return null

        val relX = deltaH / tan(vertAng) + LL_X_OFFSET
        val relY = -(deltaH * cos(ty) / sin(vertAng)) * tan(tx) + LL_Y_OFFSET

        val offset = Vector2D.cartesian(relX, relY)
            .rotate(robotPose.heading())
            .apply { if (magnitude() > MAX_PLAN_RANGE) return@getTargetPose null }
        val adjustedOffset = Vector2D.polar(
            offset.magnitude() - max(0.0, STOP_DIST - LL_X_OFFSET),
            offset.theta(),
        ).apply { if (magnitude() < 3.0) return@getTargetPose robotPose }

        return robotPose.exp(Velocity.fromVector(adjustedOffset))
    }

    fun incrThreshold() {
        confThreshold = Range.clip(confThreshold + CONF_STEP, CONF_ABS_MIN, CONF_ABS_MAX)
    }
    fun decrThreshold() {
        confThreshold = Range.clip(confThreshold - CONF_STEP, CONF_ABS_MIN, CONF_ABS_MAX)
    }

    fun init() {
        ll.setPollRateHz(LL_POLL_RATE_HZ)
        ll.pipelineSwitch(LL_PIPELINE_NUM)
        ll.start()
    }
    fun update() {
        currResult = ll.latestResult
        currStatus = ll.status
    }

    private fun getBestValidResult(): DetectorResult? {
        if (!currResult.isValid || (currResult.staleness >= MAX_STALENESS_MS)) return null

        var best: DetectorResult? = null
        for (r in currResult.detectorResults) {
            if (r.confidence < confThreshold) continue
            if (r.confidence > (best?.confidence ?: -1.0)) {
                best = r
            }
        }
        return best
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
