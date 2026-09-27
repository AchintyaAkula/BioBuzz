package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier

object OpMode : OpModeManagerNotifier.Notifications {
    private var instance: OpMode? = null

    operator fun invoke(): OpMode = instance ?: error("OpMode has not been initialized yet! Pls check code so you're not using it prematurely")

    override fun onOpModePreInit(opMode: OpMode?) {
        opMode ?: return
        instance = opMode
    }
    override fun onOpModePreStart(opMode: OpMode?) {}
    override fun onOpModePostStop(opMode: OpMode?) {}

}