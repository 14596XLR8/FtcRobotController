package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

import org.firstinspires.ftc.teamcode.Misc.*;
import org.firstinspires.ftc.teamcode.Systems.*;
import org.firstinspires.ftc.teamcode.Toggles.ToggleUpdater;

import java.util.ArrayList;

public abstract class BaseAuto extends LinearOpMode {
    final double Pi = 2*Math.asin(1);
    protected int moveCount=0;
    protected boolean moveCountOnce = false;
    protected boolean loop = false;
    String fieldPos = "";
    SystemUpdater su = new SystemUpdater();
    ToggleUpdater tu = new ToggleUpdater();

    protected DcMotor leftOdom;
    protected DcMotor rightOdom;
    protected DcMotor perpOdom;

    public DcMotor FrontLeft, FrontRight, BackLeft, BackRight;

    public Movement currentMovement = null;
    public Movement lastMovement = null;

    HolonomicDriveTrain driveTrain;
    Odometry1 odom;
    NewCamera camera;

    ArrayList<Movement> movements = new ArrayList<Movement>();

    public void enableLoop(){
        loop=true;
    }

    public void initSystems(){
        FrontLeft = hardwareMap.dcMotor.get("frontLeft");
        FrontRight = hardwareMap.dcMotor.get("frontRight");
        BackLeft = hardwareMap.dcMotor.get("backLeft");
        BackRight = hardwareMap.dcMotor.get("backRight");
        leftOdom = hardwareMap.dcMotor.get("backRight");
        perpOdom = hardwareMap.dcMotor.get("frontRight");
        rightOdom = hardwareMap.dcMotor.get("backLeft");
        
        resetEncoder(FrontLeft);
        resetEncoder(FrontRight);
        resetEncoder(BackLeft);
        resetEncoder(BackRight);
        
        driveTrain = new HolonomicDriveTrain(FrontLeft, FrontRight, BackLeft, BackRight);
        odom = new Odometry1(leftOdom, perpOdom, rightOdom, driveTrain);
        su.addSystem(driveTrain);
        su.addSystem(odom);
    }
    public void resetEncoder(DcMotor c){
        c.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        c.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void runMoveSequence(){
        if(odom.isStopped()){
            if(moveCountOnce){
                moveCount++;
                moveCountOnce = false;
                if(moveCount!=0){
                    lastMovement = movements.get(moveCount-1);
                }
                nextMove();
            }
        }
        else{
            moveCountOnce=true;
        }
        su.update();
    }
    public void runMoveSequence(boolean WaitFor){
        if(odom.isStopped()){
            if(moveCountOnce){
                moveCount++;
                moveCountOnce = false;
                if(!(moveCount==0 && lastMovement == null)){
                    lastMovement = movements.get(moveCount-1);
                }
                if(WaitFor){
                    nextMove();
                }
            }
        }
        else{
            moveCountOnce=true;
        }
        su.update();
    }
    public void startMoveSequence(){
        currentMovement = movements.get(moveCount);
        odom.setTarget(currentMovement.coordinate);
        odom.setPrecision(currentMovement.precision);
        odom.setSpeed(currentMovement.speed);
    }
    public void nextMove(){
        if(movements.size()!=0 && !(moveCount > movements.size())) {
            currentMovement = movements.get(moveCount);
            odom.setTarget(currentMovement.coordinate);
            odom.setPrecision(currentMovement.precision);
            odom.setSpeed(currentMovement.speed);
        }
        else if(loop && moveCount > movements.size()){
            moveCount=0;
        }
        else{
            odom.stope();
        }
    }
    public void startingPosition(PositionVar Start){
        odom.initialPos.setPos(Start);
    }
    public void startingPosition(int X, int Y, double R){
        odom.initialPos.setPos(new PositionVar(X,Y,R));
    }

    public boolean afterMovement(String Name){
        if(lastMovement==null)return false;
        if(lastMovement.name==Name)lastMovement.checked=true;
        else lastMovement.checked = false;
        return lastMovement.name==Name && !lastMovement.checked;
    }
    public boolean after(String Name){
        if(lastMovement==null)return false;
        if(lastMovement.name==Name)lastMovement.checked=true;
        else lastMovement.checked = false;
        return lastMovement.name==Name && !lastMovement.checked;
    }

    public boolean beforeMovement(String Name){
        if(currentMovement==null)return false;
        if(currentMovement.name==Name)currentMovement.checked = true;
        else currentMovement.checked = false;
        return currentMovement.name==Name && !currentMovement.checked;
    }
    public boolean before(String Name){
        if(currentMovement==null)return false;
        if(currentMovement.name==Name)currentMovement.checked = true;
        else currentMovement.checked = false;
        return currentMovement.name==Name && !currentMovement.checked;
    }


    public PositionVar Coordinate(int X, int Y, double R){
        return new PositionVar(X,Y,R);
    }

    public void addCoordinate(String Name, PositionVar Coordinate, double Speed, int Precision){
        movements.add(new Movement(Name,Coordinate,Speed,Precision));
    }
    public void addCoordinate(String Name, PositionVar Coordinate, double Speed){
        movements.add(new Movement(Name,Coordinate,Speed));
    }
    public void addCoordinate(String Name, PositionVar Coordinate, int Precision){
        movements.add(new Movement(Name,Coordinate,Precision));
    }
    public void addCoordinate(PositionVar Coordinate, double Speed, int Precision){
        movements.add(new Movement(Coordinate,Speed,Precision));
    }
    public void addCoordinate(PositionVar Coordinate, int Precision){
        movements.add(new Movement(Coordinate,Precision));
    }
    public void addCoordinate(PositionVar Coordinate, double Speed){
        movements.add(new Movement(Coordinate,Speed));
    }
    public void addCoordinate(String Name, PositionVar Coordinate){
        movements.add(new Movement(Name,Coordinate));
    }
    public void addCoordinate(PositionVar Coordinate){
        movements.add(new Movement(Coordinate));
    }
    public void addCoordinate(Movement movement){
        movements.add(movement);
    }
    public void addMovement(String Name, int Forward, int Sideward, double Speed, int Precision){
        movements.add(new Movement(Name,odom.getPositionByDists(Forward, Sideward),Speed,Precision));
    }
    public void addMovement(String Name, int Forward, int Sideward, double Speed){
        movements.add(new Movement(Name,odom.getPositionByDists(Forward, Sideward),Speed));
    }
    public void addMovement(String Name, int Forward, int Sideward, int Precision){
        movements.add(new Movement(Name,odom.getPositionByDists(Forward, Sideward),Precision));
    }
    public void addMovement(int Forward, int Sideward, double Speed, int Precision){
        movements.add(new Movement(odom.getPositionByDists(Forward, Sideward),Speed,Precision));
    }
    public void addMovement(int Forward, int Sideward, int Precision){
        movements.add(new Movement(odom.getPositionByDists(Forward, Sideward),Precision));
    }
    public void addMovement(int Forward, int Sideward, double Speed){
        movements.add(new Movement(odom.getPositionByDists(Forward, Sideward),Speed));
    }
    public void addMovement(String Name, int Forward, int Sideward){
        movements.add(new Movement(Name,odom.getPositionByDists(Forward, Sideward)));
    }
    public void addMovement(int Forward, int Sideward){
        movements.add(new Movement(odom.getPositionByDists(Forward, Sideward)));
    }
    public void addMovement(Movement movement){
        movements.add(movement);
    }
    

    
    public void waitWithCam(){
        while(!opModeIsActive()&&!isStopRequested()) {
            camera.updateCamera();
            fieldPos = camera.getFieldPos();
            telemetry.addLine("Detect: "+fieldPos);
            telemetry.update();
        }
        camera.endTfod();

        fieldPos = camera.getFieldPos();
    }
}