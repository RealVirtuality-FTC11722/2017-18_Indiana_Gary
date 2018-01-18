package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Lyesome on 2018-01-03.
 */

@Autonomous(name="Indiana Gary - Red Left", group="Linear Opmode")
//@Disabled

public class Autonomous_R1 extends LinearOpMode {
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    BotConfig indianaGary = new BotConfig();
    private double jewelOffset = 0;
    private double columnOffset = 0;
    private static double Drive_Power = 0.2;

    @Override
    public void runOpMode() {

        indianaGary.drive.init(hardwareMap);
        indianaGary.myJewelArm.init(hardwareMap);
        indianaGary.myGlyphLifter.init(hardwareMap);
        //indianaGary.myRelicArm.init(hardwareMap);
        //indianaGary.myVuMark.init(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        String Team_Color = "red";
        runtime.reset();

        // run until the end of the match

        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.update();


        //Autonomous Commands


        indianaGary.myGlyphLifter.Capture();
        //columnOffset = indianaGary.myVuMark.DecodeImage();
        indianaGary.myJewelArm.LowerArm();
        jewelOffset = indianaGary.myJewelArm.JewelKnock("blue");
        if (jewelOffset < 0) {
            indianaGary.drive.Backward(this, Drive_Power, -jewelOffset);
        }
        if (jewelOffset > 0) {
            indianaGary.drive.Forward(this, Drive_Power, jewelOffset);
        }
        indianaGary.myJewelArm.RaiseArm();

        indianaGary.drive.Forward(this, Drive_Power, 27.5 - jewelOffset + columnOffset);

        indianaGary.drive.TurnRight(this, 90);

        indianaGary.drive.Forward(this, Drive_Power, 3);

        indianaGary.myGlyphLifter.Release();
        indianaGary.drive.Backward(this, Drive_Power, 2);
    }


}
