package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier
import com.qualcomm.robotcore.hardware.HardwareMap

object OpMode : OpModeManagerNotifier.Notifications {
    var instance: OpMode? = null

    val hardwareMap: HardwareMap
        get() = this { hardwareMap }

    operator fun <T> invoke(f: OpMode.() -> T): T {
        return instance?.f() ?: error("OpMode not initialized")
    }

    override fun onOpModePreInit(opMode: OpMode?) {
        if (opMode == null) return
        instance = opMode
    }

    override fun onOpModePreStart(opMode: OpMode?) {}
    override fun onOpModePostStop(opMode: OpMode?) {}
}