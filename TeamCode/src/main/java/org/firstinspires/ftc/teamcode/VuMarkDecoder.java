package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;


/**
 * Created by Lyesome on 2018-01-13.
 * This class controls the phone's camera for detecting and decoding First Relic Recovery VuMarks
 */

public class VuMarkDecoder {

    HardwareMap myHWMap;

    public static final String TAG = "Vuforia VuMark Sample";

    VuforiaLocalizer vuforia;
    //OpenGLMatrix lastLocation = null;
    VuforiaTrackable relicTemplate;
    VuforiaTrackables relicTrackables;


    public void VuMarkDecoder() { //constructor
    }

    //Method to initialize camera decoder
    public void init(HardwareMap myNewHWMap) {
        myHWMap = myNewHWMap;
        int cameraMonitorViewId = myHWMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", myHWMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters vuparameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId); //CAMERA DISPLAY ON
        // OR...  Do Not Activate the Camera Monitor View, to save power
        //VuforiaLocalizer.Parameters vuparameters = new VuforiaLocalizer.Parameters(); //CAMERA DISPLAY OFF
        vuparameters.vuforiaLicenseKey = "AfXDkbT/////AAAAGUWkW5XORUDZk0pzMnL5JlVLvMH8yBho/fstQbUOWSs+KpTGzK7G45wHLlm81SXcl71Youk9yLvlN8hblV/+U0s5aamvYKWA71dh8aiXVKYqoDyF5V70BbEXcfUXOcRphDBLUpnCLgVYPxr837L4Yc8RHPVlEYXAtbYKJAvjnMZurqHTSvQG4G/XV5QcFJaJPFyP9zC/sPlkGgdg/xDxYzkABnxDJFTlIKePvpgxCcednmCT6bG/hE5ZeuBxNtC7kWI0xqrG5L90Pq0UZ64Y87esm7DujazZ9YrRVkpNRXcM80kSm+27BrpPvubNeT1lxpRVAzsxZX5AXPAnrHUO3dMMx66HqXzp6X82OgLcHEL1";
        vuparameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        vuforia = ClassFactory.createVuforiaLocalizer(vuparameters);

        relicTrackables = vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);

        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        relicTrackables.activate();

    }

    //Method to decode vumark and output the distance the correct column is from the center column
    public double DecodeImage(LinearOpMode op){
        //Decode Image and offset final robot position to line up with correct column
        //Return offset distance in inches
        double vuMarkColumnOffset = 0;
        double columnRightOffset = -7.5; //Offset in inches from center column; negative is closer to bot's starting position
        double columnLeftOffset = 7.5; //Offset in inches from center column; negative is closer to bot's starting position
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
            //If VuMark is detected, find out which one
            if (vuMark == RelicRecoveryVuMark.LEFT) {
                vuMarkColumnOffset = columnLeftOffset;
                op.telemetry.addLine("LEFT Column");
            }
            if (vuMark == RelicRecoveryVuMark.RIGHT) {
                vuMarkColumnOffset = columnRightOffset;
                op.telemetry.addLine("RIGHT Column");
            }
            if (vuMark == RelicRecoveryVuMark.CENTER) {
                op.telemetry.addLine("CENTER Column");
            }
        } else {
            op.telemetry.addLine("UNKNOWN Column");
        }
        return vuMarkColumnOffset;
    }

    String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }
}