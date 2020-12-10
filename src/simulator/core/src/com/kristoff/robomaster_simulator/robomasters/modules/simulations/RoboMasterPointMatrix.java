package com.kristoff.robomaster_simulator.robomasters.properties;

import com.badlogic.gdx.math.Vector2;
import com.kristoff.robomaster_simulator.robomasters.RoboMaster;

import static java.lang.Math.*;
import static java.lang.Math.atan;

public class RoboMasterPointMatrix {
    boolean[][] roboMasterMatrix;

    public RoboMasterPointMatrix(){
        roboMasterMatrix = new boolean[8490][4890];
    }

    private Vector2 roboSidePoint(double angle, Vector2 center, int width, int height){
        double anglein = atan(height/width);
        double halfcross = width * width + height * height;
        double resultx = center.x + sin(angle + anglein) * halfcross;
        double resulty = center.y + cos(angle + anglein) * halfcross;
        return new Vector2(Integer.parseInt(new java.text.DecimalFormat("0").format(resultx)),Integer.parseInt(new java.text.DecimalFormat("0").format(resulty)));
    }

    private void addLineByTwoPoint(Vector2 a, Vector2 b, boolean[][] basematric){
        int middletan = Integer.parseInt(new java.text.DecimalFormat("0").format((a.y - b.y)/(a.x - b.x)));
        if(a.x<b.x){
            for(int i=Integer.parseInt(new java.text.DecimalFormat("0").format(a.x));i<b.x;i++){
                basematric[i][(Integer.parseInt(new java.text.DecimalFormat("0").format(a.y)) + middletan)] = true;
            }
        }
        else{
            for(int i=Integer.parseInt(new java.text.DecimalFormat("0").format(b.x));i<a.x;i++){
                basematric[i][(Integer.parseInt(new java.text.DecimalFormat("0").format(b.y)) + middletan)] = true;
            }
        }
    }

    private void addRoboMaster(double angle, Vector2 center, int width, int height, boolean[][] basematric){
        double crossAngle = atan(height/width);
        Vector2 a = roboSidePoint(angle, center, width, height);
        double AngleB = angle + 3.1415 - 2 * crossAngle;
        Vector2 b = roboSidePoint(angle + AngleB, center, width, height);
        double AngleC = crossAngle + 3.1415;
        Vector2 c = roboSidePoint(AngleC, center, width, height);
        double AngleD = AngleB + 3.1415;
        Vector2 d = roboSidePoint(AngleD, center, width, height);


        addLineByTwoPoint(a, b, basematric);
        addLineByTwoPoint(b, c, basematric);
        addLineByTwoPoint(c, d, basematric);
        addLineByTwoPoint(d, a, basematric);
    }
}
