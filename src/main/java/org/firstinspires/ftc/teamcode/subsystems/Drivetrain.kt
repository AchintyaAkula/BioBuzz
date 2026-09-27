package org.firstinspires.ftc.teamcode.subsystems

import com.pedropathing.api.PoseFactory
import com.pedropathing.follower.Follower
import com.pedropathing.ivy.Command
import com.pedropathing.ivy.behaviors.BlockedBehavior
import com.pedropathing.ivy.behaviors.ConflictBehavior
import com.pedropathing.ivy.behaviors.InterruptedBehavior
import com.pedropathing.ivy.commands.Commands
import com.pedropathing.ivy.pedro.PedroCommands
import com.pedropathing.localization.MotionState
import com.pedropathing.math.Pose
import com.pedropathing.paths.Path
import org.firstinspires.ftc.teamcode.OpMode
import org.firstinspires.ftc.teamcode.pedro.Constants
import java.util.function.Supplier

class Drivetrain {
    private val f: Follower by lazy { Constants.create(OpMode().hardwareMap) }
    val motionData: MotionState
        get() = f.localizer.state()

    var throttle: Double = 1.0


    fun engageDrive(
        forwardSupplier: Supplier<Double>,
        strafeSupplier: Supplier<Double>,
        turnSupplier: Supplier<Double>,
    ) : Command = Commands.infinite {
        f.manual(
            forwardSupplier.get() * throttle,
            strafeSupplier.get() * throttle,
            turnSupplier.get() * throttle
        )
    }.requiring(this)
        .setPriority(-1)
        .setBlockedBehavior(BlockedBehavior.CANCEL)
        .setInterruptedBehavior(InterruptedBehavior.SUSPEND)
        .setConflictBehavior(ConflictBehavior.OVERRIDE)

    fun follow(path: Path): Command = PedroCommands.follow(
        f,
        path.with(Constants.foresight.maxPathSpeed.at(throttle))
    )
        .requiring(this)
        .setPriority(0)
        .setInterruptedBehavior(InterruptedBehavior.END)
        .setConflictBehavior(ConflictBehavior.QUEUE)
        .setBlockedBehavior(BlockedBehavior.QUEUE)

    fun update() {
        f.update()
    }
}