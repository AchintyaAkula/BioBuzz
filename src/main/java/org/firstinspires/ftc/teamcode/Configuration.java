package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver.EncoderDirection;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver.GoBildaOdometryPods;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@SuppressWarnings("unused")
public class Configuration {
    // Control Hub
    public static final MotorConfig FRONT_LEFT_MOTOR = new MotorConfig("fl", Direction.FORWARD);
    public static final MotorConfig BACK_LEFT_MOTOR = new MotorConfig("bl", Direction.FORWARD);

    public static final MotorConfig FRONT_FLYWHEEL_MOTOR = new MotorConfig("f_fw", Direction.FORWARD);
    public static final MotorConfig BACK_FLYWHEEL_MOTOR = new MotorConfig("b_fw", Direction.FORWARD);

    public static final PinpointConfig PINPOINT = new PinpointConfig(
            "pinpoint",
            GoBildaOdometryPods.goBILDA_4_BAR_POD,
            EncoderDirection.REVERSED,
            EncoderDirection.FORWARD,
            -4.292755906,
            3.86389764);

    // Expansion Hub
    public static final MotorConfig FRONT_RIGHT_MOTOR = new MotorConfig("fr", Direction.REVERSE);
    public static final MotorConfig BACK_RIGHT_MOTOR = new MotorConfig("br", Direction.REVERSE);

    public static final MotorConfig FRONT_INTAKE_MOTOR = new MotorConfig("f_in", Direction.FORWARD);
    public static final MotorConfig BACK_INTAKE_MOTOR = new MotorConfig("b_in", Direction.FORWARD);

    // Servo Hub
    public static final ServoConfig TRANSFER_SERVO_NAME = new ServoConfig("transfer");

    public static final ServoConfig FRONT_INTAKE_SERVO_NAME = new ServoConfig("f_intake");
    public static final ServoConfig BACK_INTAKE_SERVO_NAME = new ServoConfig("b_intake");

    public static class MotorConfig {
        public final String name;
        public final Direction direction;

        public MotorConfig(String name, Direction dir) {
            this.name = name;
            this.direction = dir;
        }

        public DcMotorEx createMotor(HardwareMap hwMap) {
            DcMotorEx m = hwMap.get(DcMotorEx.class, name);
            m.setDirection(direction);

            return m;
        }
    }

    public static class ServoConfig {
        public final String name;

        public ServoConfig(String name) {
            this.name = name;
        }

        public Servo createServo(HardwareMap hwMap) {
            return hwMap.get(Servo.class, name);
        }
    }

    public static class PinpointConfig {
        public final String name;
        public final GoBildaOdometryPods podType;
        public final EncoderDirection xPodDirection;
        public final EncoderDirection yPodDirection;
        public final double xPodOffset;
        public final double yPodOffset;

        public PinpointConfig(
                String name,
                GoBildaOdometryPods podType,
                EncoderDirection xDir,
                EncoderDirection yDir,
                double xOffset,
                double yOffset) {
            this.name = name;
            this.podType = podType;
            this.xPodDirection = xDir;
            this.yPodDirection = yDir;
            this.xPodOffset = xOffset;
            this.yPodOffset = yOffset;
        }
    }
}
