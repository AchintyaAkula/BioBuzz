package org.firstinspires.ftc.teamcode.template

import com.pedropathing.ivy.Command
import com.pedropathing.ivy.Scheduler
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.hardware.lynx.LynxModule.BulkCachingMode
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.teamcode.Robot

class Autonomous : OpMode() {
    private val r: Robot by lazy { Robot() }
    private val hubs: List<LynxModule> by lazy { hardwareMap.getAll(LynxModule::class.java) }

    private val sequence: Command = Command.NOOP

    override fun init() {
        hubs.forEach { it.bulkCachingMode = BulkCachingMode.MANUAL }
        Scheduler.reset()
        r.init()
        hubs.forEach { it.clearBulkCache() }
    }

    override fun start() {
        sequence.schedule()
    }

    override fun loop() {
        r.update()
        Scheduler.execute()
        hubs.forEach { it.clearBulkCache() }
    }
}