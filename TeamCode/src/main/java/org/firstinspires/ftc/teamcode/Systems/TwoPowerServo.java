package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

public class TwoPowerServo extends System{

    CRServo servo;
    public double servoPower;

    public TwoPowerServo(CRServo Servo, double Power){
        servo = Servo;
        servoPower = Power;
    }
    public void update(){}
    public void input(){}

    public void turnOn(){
        servo.setPower(servoPower);
    }
    public void turnOff(){
        servo.setPower(0);
    }

}