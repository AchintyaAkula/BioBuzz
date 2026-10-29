package org.firstinspires.ftc.teamcode.subsystems

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.util.OpMode.hwMap

class IntakeSubsystem {
    companion object {
        private const val FRONT_INTAKE_MOTOR_CONFIG = ""
        private const val FRONT_INTAKE_SERVO_CONFIG = ""
        private const val FRONT_INTAKE_ENGAGED_POS = 0.0
        private const val FRONT_INTAKE_DISENGAGED_POS = 0.0

        private const val REAR_INTAKE_MOTOR_CONFIG = ""
        private const val REAR_INTAKE_SERVO_CONFIG = ""
        private const val REAR_INTAKE_ENGAGED_POS = 0.0
        private const val REAR_INTAKE_DISENGAGED_POS = 0.0

        private const val FAST_INTAKE_POWER = 1.0
        private const val SLOW_INTAKE_POWER = 0.8
        private const val PASSIVE_INTAKE_POWER = -0.5

        private const val TRANSFER_SERVO_CONFIG = ""
        private const val TRANSFER_SERVO_ENGAGED_POS = 0.0
        private const val TRANSFER_SERVO_DISENGAGED_POS = 0.0
    }

    private val frontIntakeMotor: DcMotor by lazy { hwMap.get(FRONT_INTAKE_MOTOR_CONFIG) as DcMotor }
    private val frontIntakeServo: Servo by lazy { hwMap.get(FRONT_INTAKE_SERVO_CONFIG) as Servo }

    private val rearIntakeMotor: DcMotor by lazy { hwMap.get(REAR_INTAKE_MOTOR_CONFIG) as DcMotor }
    private val rearIntakeServo: Servo by lazy { hwMap.get(REAR_INTAKE_SERVO_CONFIG) as Servo }

    private val transferServo: Servo by lazy { hwMap.get(TRANSFER_SERVO_CONFIG) as Servo }

    var state: IntakeState = IntakeState.STOPPED
        set(value) {
            when (value) {
                IntakeState.FRONT -> {
                    if ((field) == IntakeState.TRANSFER) { disengageTransfer() }
                    engageForwardIntake()

                    frontIntakeMotor.power = FAST_INTAKE_POWER
                    rearIntakeMotor.power = PASSIVE_INTAKE_POWER
                }
                IntakeState.REAR -> {
                    if ((field) == IntakeState.TRANSFER) { disengageTransfer() }
                    engageRearIntake()

                    frontIntakeMotor.power = PASSIVE_INTAKE_POWER
                    rearIntakeMotor.power = FAST_INTAKE_POWER
                }
                IntakeState.TRANSFER -> {
                    if (frontIntakeServo.position == FRONT_INTAKE_ENGAGED_POS) {
                        frontIntakeMotor.power = FAST_INTAKE_POWER
                        rearIntakeMotor.power = SLOW_INTAKE_POWER
                    } else {
                        frontIntakeMotor.power = SLOW_INTAKE_POWER
                        rearIntakeMotor.power = FAST_INTAKE_POWER
                    }
                }
                IntakeState.STOPPED -> {
                    disengageTransfer()

                    frontIntakeMotor.power = 0.0
                    rearIntakeMotor.power = 0.0
                }
            }
        }

    private fun engageTransfer() { transferServo.position = TRANSFER_SERVO_ENGAGED_POS }
    private fun disengageTransfer() { transferServo.position = TRANSFER_SERVO_DISENGAGED_POS }

    private fun engageForwardIntake() {
        frontIntakeServo.position = FRONT_INTAKE_ENGAGED_POS
        rearIntakeServo.position = REAR_INTAKE_DISENGAGED_POS
    }
    private fun engageRearIntake() {
        frontIntakeServo.position = FRONT_INTAKE_DISENGAGED_POS
        rearIntakeServo.position = REAR_INTAKE_ENGAGED_POS
    }
}

enum class IntakeState {
    FRONT,
    REAR,
    TRANSFER,
    STOPPED
}