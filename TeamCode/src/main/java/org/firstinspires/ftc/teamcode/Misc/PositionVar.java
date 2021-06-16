package org.firstinspires.ftc.teamcode.Misc;

import org.firstinspires.ftc.robotcore.external.navigation.Position;

public class PositionVar{
    final double Pi = 2*Math.asin(1);
    protected double x;
    protected double y;
    protected double r;

    public PositionVar(double x, double y, double r){
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getR() {
        return r;
    }

    public void setX(double X) {
        x = X;
    }
    public void addToX(double dx){
        x+=dx;
    }
    public void setY(double Y) {
        y = Y;
    }
    public void addToY(double dy){
        y+=dy;
    }
    public void setR(double R) {
        r = R;
        r=loopedInput(r,-Pi,Pi);
    }
    public void addToR(double dr){ r+=dr;
        r=loopedInput(r,-Pi,Pi);
    }
    public void setPos(PositionVar c){
        this.x = c.getX();
        this.y = c.getY();
        this.r = c.getR();
    }
    public void setPos(double X, double Y, double R){
        setPos(new PositionVar(X,Y,R));
    }

    private double loopedInput(double in,double min, double max){
        if(max>min){
            double transitioner = min;
            min=max;
            max=transitioner;
        }
        if(in>0){
            in=((in-max)%(min-max))+max;
        }
        else{
            in=((in-min)%(max-min))+min;
        }
        return in;
    }//loops input value ex: 360deg=0deg and  400deg=40deg
}
