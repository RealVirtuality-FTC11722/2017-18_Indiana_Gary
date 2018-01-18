// tested 2017-11-05 AVP
/*
Copyright (c) 2016 Robert Atkinson
All rights reserved.
Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:
Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.
Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.
NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the TeleOp period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive TeleOp for a PushBot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */

@TeleOp(name="Driver Mode", group="Linear OpMode")  // @Autonomous(...) is the other common choice
//@Disabled
public class DriverMode extends LinearOpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();

    private BotConfig indianaGary = new BotConfig();



    @Override
    public void runOpMode() {

        /* eg: Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step (using the FTC Robot Controller app on the phone).
         */
        indianaGary.drive.init(hardwareMap);
        indianaGary.myGlyphLifter.init(hardwareMap);
        indianaGary.myRelicArm.init(hardwareMap);
        indianaGary.myJewelArm.init(hardwareMap); //need to initialize to prevent arm from dropping
        indianaGary.drive.motorBL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        indianaGary.drive.motorBR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //Set preset positions for Glyph Lifter
        int PositionStart = indianaGary.myGlyphLifter.motorLift.getCurrentPosition();
        int PositionMax = PositionStart + 1680;
        int Position1 = PositionStart + 100;
        int Position2 = PositionStart + 750;
        int Position3 = PositionStart + 1350;
        boolean TogglePressed = false;
        boolean ToggleReleased = true;
        boolean autoLift = false; //Used to enable auto motion of Glyph Lifter to preset Positions
        double LifterPower = 0.2;

        telemetry.addData("Status", "Initialized");
        telemetry.update();


        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            //telemetry.addData("Status", "Run Time: " + runtime.toString());
            //telemetry.update();

            double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
            double robotAngle = Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
            double rightX = -gamepad1.right_stick_x;
            final double v1 = r * Math.cos(robotAngle) - rightX;
            final double v2 = r * Math.sin(robotAngle) + rightX;
            final double v3 = r * Math.sin(robotAngle) - rightX;
            final double v4 = r * Math.cos(robotAngle) + rightX;

            indianaGary.drive.motorFL.setPower(v1);
            indianaGary.drive.motorFR.setPower(v2);
            indianaGary.drive.motorBL.setPower(v3);
            indianaGary.drive.motorBR.setPower(v4);

            //Glyph Grabber Control
            if (gamepad2.right_trigger > 0 && !indianaGary.myGlyphLifter.GRAB_LOCKED) {
                indianaGary.myGlyphLifter.grabberR.setPosition(gamepad2.right_trigger * 0.4 + 0.3);
                indianaGary.myGlyphLifter.grabberL.setPosition(gamepad2.right_trigger * 0.4 + 0.3);
            }
            if (TogglePressed) {
                ToggleReleased = false;
            } else {
                ToggleReleased = true;
            }
            TogglePressed = gamepad2.right_bumper;
            if (ToggleReleased){
                if (gamepad2.right_bumper && !indianaGary.myGlyphLifter.GRAB_LOCKED){
                    indianaGary.myGlyphLifter.Grab();
                } else {
                    if (gamepad2.right_bumper && indianaGary.myGlyphLifter.GRAB_LOCKED) {
                        indianaGary.myGlyphLifter.Release();
                    }
                }
            }

            //Glyph Lifter Control
            if (gamepad2.right_stick_y != 0) {
                autoLift = false;
                indianaGary.myGlyphLifter.motorLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                if ((gamepad2.right_stick_y < 0) && (indianaGary.myGlyphLifter.motorLift.getCurrentPosition() <= PositionMax)) {
                    indianaGary.myGlyphLifter.motorLift.setPower(-gamepad2.right_stick_y*LifterPower*1.1);
                }else {
                    if ((gamepad2.right_stick_y > 0) && (indianaGary.myGlyphLifter.motorLift.getCurrentPosition() >= PositionStart)) {
                        indianaGary.myGlyphLifter.motorLift.setPower(-gamepad2.right_stick_y*LifterPower*0.9);
                    } else {
                        indianaGary.myGlyphLifter.motorLift.setPower(0);
                    }
                }
                //Turn off auto motion as soon as left stick is moved
                telemetry.addData("Lifter at Position: ", indianaGary.myGlyphLifter.motorLift.getCurrentPosition());
                telemetry.update();
            }else {
                //Go to preset positions when corresponding button is pressed
                if (gamepad2.a) {
                    autoLift = true;
                    indianaGary.myGlyphLifter.GotoPresetPosition(indianaGary.myGlyphLifter.POS_1);
                }
                if (gamepad2.x) {
                    autoLift = true;
                    indianaGary.myGlyphLifter.GotoPresetPosition(indianaGary.myGlyphLifter.POS_2);
                }
                if (gamepad2.y) {
                    autoLift = true;
                    indianaGary.myGlyphLifter.GotoPresetPosition(indianaGary.myGlyphLifter.POS_3);
                }
                if (!autoLift) {
                    indianaGary.myGlyphLifter.motorLift.setPower(0);
                }

            }

            indianaGary.myRelicArm.ArmExtension(gamepad2.left_stick_y);

        }
    }
}