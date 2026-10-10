package org.firstinspires.ftc.teamcode.util

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier.Notifications

object OpMode : Notifications {
    var instance: OpMode? = null

    operator fun invoke(): OpMode = instance ?: error("OpMode attribute accessed before initialization")

    override fun onOpModePreInit(opMode: OpMode?) {
        if (opMode == null) return
        instance = opMode
    }

    override fun onOpModePreStart(opMode: OpMode?) {}
    override fun onOpModePostStop(opMode: OpMode?) {}
}
