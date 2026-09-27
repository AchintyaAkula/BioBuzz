package org.firstinspires.ftc.teamcode.opmode.teleop

import com.pedropathing.ivy.Command
import com.pedropathing.ivy.Scheduler
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.hardware.lynx.LynxModule.BulkCachingMode
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.Robot

@TeleOp(name = "Limelight Path Test", group = "TeleOp")
class DynamicPathTest : OpMode() {
    private val r: Robot by lazy { Robot() }
    private val hubs: List<LynxModule> by lazy { hardwareMap.getAll(LynxModule::class.java) }

    override fun init() {
        hubs.forEach { it.bulkCachingMode = BulkCachingMode.MANUAL }
        Scheduler.reset()
        r.init()
        hubs.forEach { it.clearBulkCache() }
    }

    override fun start() {
        r.dt.engageDrive(
            { -gamepad1.left_stick_y.toDouble() },
            { gamepad1.left_stick_x.toDouble() },
            { gamepad1.right_stick_x.toDouble() },
        )
    }

    override fun loop() {
        r.update()
        checkGamepadBindings()
        Scheduler.execute()
        hubs.forEach { it.clearBulkCache() }
    }

    fun checkGamepadBindings() {
        if (gamepad1.rightBumperWasPressed()) {
            (r.goToTarget() ?: Command.NOOP).schedule()
        }
    }
}
