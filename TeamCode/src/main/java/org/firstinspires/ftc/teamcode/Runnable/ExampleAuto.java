package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Example",group = "Test")
// @Disabled
public class ExampleAuto extends BaseAuto{
    public void runOpMode(){

        int loopCount = 0;

        initSystems();
        enableLoop();
        startingPosition(0,0,0);

        addCoordinate("First", Coordinate(-1000,0,-Pi/2), 1.0, 10);

        addMovement(0, 1000, 1.1,7);

        addCoordinate(Coordinate(0, 1000, 0));

        addMovement("Last", -1000, 0, 1d);

        waitForStart();
        // waitWithCam();


        startMoveSequence();

        while(opModeIsActive()){
            // runMoveSequence();
            // runMoveSequence(gamepad1.b);
            
            odom.here();

            if(before("First")){
                loopCount++;
            }

            telemetry.addLine("Move Number: " + moveCount);
            telemetry.addLine("Move Name: " + currentMovement.name);
            telemetry.addLine();
            telemetry.addLine("Cycles: " + loopCount);
            telemetry.update();
            
            
            telemetry.addLine("Is Stopped: "+Boolean.toString(odom.isStopped()));
            telemetry.addLine("At Target: "+Boolean.toString(odom.atTarget()));
            telemetry.addLine("Prec Count: "+odom.getPrecisionCounter());
            telemetry.addLine("AddOnce: "+Boolean.toString(moveCountOnce));
            
            telemetry.addLine();
            telemetry.addLine("Target");
            telemetry.addLine("X: "+odom.getTarget().getX());
            telemetry.addLine("Y: "+odom.getTarget().getY());
            telemetry.addLine("R: "+odom.getTarget().getR());
            
            telemetry.addLine();
            telemetry.addLine("Current");
            telemetry.addLine("X: "+odom.getPos().getX());
            telemetry.addLine("Y: "+odom.getPos().getY());
            telemetry.addLine("R: "+odom.getPos().getR());

            telemetry.addLine();
            telemetry.addLine("Odometers: ");
            telemetry.addLine("Left: "+odom.leftOdometer.getCurrentPosition());
            telemetry.addLine("Side: "+odom.perpindicularOdometer.getCurrentPosition());
            telemetry.addLine("Right: "+odom.rightOdometer.getCurrentPosition());


            if(isStopRequested()){
                odom.stope();
                return;
            }
        }
        return;
    }
}