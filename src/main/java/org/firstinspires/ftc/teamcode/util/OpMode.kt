package org.firstinspires.ftc.teamcode.util

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier

object OpMode : OpModeManagerNotifier.Notifications {
    var instance: OpMode? = null

    operator fun invoke(): OpMode = instance ?: error("OpMode instance is not initialized.")

    override fun onOpModePreInit(opMode: OpMode?) {
        opMode ?: return
        instance = opMode
    }

    override fun onOpModePreStart(opMode: OpMode?) {}
    override fun onOpModePostStop(opMode: OpMode?) {}

}