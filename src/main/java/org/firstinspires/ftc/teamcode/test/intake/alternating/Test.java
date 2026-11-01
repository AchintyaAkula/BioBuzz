package org.firstinspires.ftc.teamcode.test.intake.alternating;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class Test extends OpMode {
    private Subsystem intake = new Subsystem();

    @Override
    public void init() {
        intake.init(hardwareMap);
    }

    @Override
    public void loop() {

    }
}
