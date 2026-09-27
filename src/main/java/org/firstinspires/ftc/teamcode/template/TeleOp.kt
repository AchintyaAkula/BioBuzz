package org.firstinspires.ftc.teamcode.template

import com.pedropathing.ivy.Scheduler
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.hardware.lynx.LynxModule.BulkCachingMode
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.teamcode.Robot

class TeleOp : OpMode() {
    private val r: Robot by lazy { Robot() }
    private val hubs: List<LynxModule> by lazy { hardwareMap.getAll(LynxModule::class.java) }

    override fun init() {
        hubs.forEach { it.bulkCachingMode = BulkCachingMode.MANUAL }
        Scheduler.reset()
        r.init()
        hubs.forEach { it.clearBulkCache() }
    }

    override fun loop() {
        r.update()
        checkGamepadBindings()
        Scheduler.execute()
        hubs.forEach { it.clearBulkCache() }
    }

    fun checkGamepadBindings() {
        // if (gamepad1.aWasPressed()) {
        //     blah blah
        // }
        // ...
    }
}