@file:Suppress("unused")

package org.firstinspires.ftc.teamcode.subsystems

import com.pedropathing.math.Pose
import com.qualcomm.hardware.limelightvision.LLResult
import com.qualcomm.hardware.limelightvision.LLStatus
import com.qualcomm.hardware.limelightvision.Limelight3A
import org.firstinspires.ftc.teamcode.util.OpMode
import org.firstinspires.ftc.teamcode.util.Subsystem
import java.util.function.Supplier
import kotlin.math.max
import kotlin.math.min
import kotlin.time.ComparableTimeMark
import kotlin.time.TimeSource.Monotonic

class Rewrite(val robotPoseSupplier: Supplier<Pose>) : Subsystem {
    private val device by lazy { OpMode().hardwareMap.get(Limelight3A::class.java, LL_CF_NAME) }
    private lateinit var prevCalcTStamp: ComparableTimeMark
    private var currData: LLResult? = null
    private var currStatus: LLStatus? = null
    private var targetPose: Pose? = null
    private var confThreshold = 0.44

    //region Subsystem functions
    override fun init() = device.run {
        setPollRateHz(LL_POLL_RATE_HZ)
        pipelineSwitch(LL_PIPELINE_NUM)
        start()
    }
    override fun update() {
        updateData()
        if ((Monotonic.markNow() - prevCalcTStamp).inWholeMilliseconds > UPDATE_PERIOD_MS) updateTargetPose()
    }
    //endregion

    //region User Access functions
    fun incrThreshold() {
        confThreshold = max(CONF_ABS_MAX, confThreshold + CONF_STEP)
    }
    fun decrThreshold() {
        confThreshold = min(CONF_ABS_MIN, confThreshold - CONF_STEP)
    }
    //endregion

    //region Utility functions
    private fun updateData() = with(device.latestResult) {
        currData = if (isValid && staleness < MAX_STALENESS_MS) this else null
        currStatus = device.status
    }
    private fun updateTargetPose() {
        // Math that sets the target pose
    }
    //endregion

    companion object {
        // TODO tune the values below
        //region Detection Settings
        private const val CONF_STEP = 0.05
        private const val CONF_ABS_MIN = 0.05
        private const val CONF_ABS_MAX = 0.95
        private const val MAX_STALENESS_MS: Int = 120
        private const val UPDATE_PERIOD_MS: Int = 40
        //endregion

        //region Device Configuration
        private const val LL_CF_NAME: String = "ll"
        private const val LL_PIPELINE_NUM: Int = 3
        private const val LL_POLL_RATE_HZ: Int = 40
        //endregion

        //region Limelight Relative Positioning
        private const val LL_PITCH_DEG: Double = -9.3
        private const val LL_HEIGHT_IN = 9.5
        private const val LL_X_OFFSET = 7.625
        private const val LL_Y_OFFSET = 4.75
        //endregion

        //region Planning Settings
        private const val BALL_HEIGHT_IN = 1.4
        private const val STOP_DIST = 16.0
        private const val MAX_PLAN_RANGE = 60.0
        //endregion
    }
}