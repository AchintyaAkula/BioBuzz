package org.firstinspires.ftc.teamcode.util

import android.content.Context
import com.qualcomm.ftccommon.FtcEventLoop
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier.Notifications
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.ftccommon.external.OnCreateEventLoop

object OpMode : Notifications {
    var instance: OpMode? = null
    val hwMap: HardwareMap
        get() = this().hardwareMap

    operator fun invoke(): OpMode = instance ?: error("OpMode attribute accessed before initialization")

    override fun onOpModePreInit(opMode: OpMode?) {
        if (opMode == null) return
        instance = opMode
    }

    override fun onOpModePreStart(opMode: OpMode?) {}
    override fun onOpModePostStop(opMode: OpMode?) {}

    @OnCreateEventLoop
    @JvmStatic
    fun register(context: Context, eventLoop: FtcEventLoop) {
        eventLoop.opModeManager.registerListener(this)
    }
}