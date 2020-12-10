package com.kristoff.robomaster_simulator.robomasters.modules.simulations;

import com.badlogic.gdx.math.Vector2;
import com.kristoff.robomaster_simulator.robomasters.types.RoboMaster;

import static java.lang.Math.*;
import static java.lang.Math.atan;

public class RoboMasterPointMatrix {
    RoboMaster thisRoboMaster;
    boolean[][]  ;

    Runnable runnable;

    public RoboMasterPointMatrix(RoboMaster roboMaster){
        this.thisRoboMaster = roboMaster;
        roboMasterMatrix = new boolean[8490][4890];

        runnable = new Runnable() {
            @Override
            public void run() {
                roboMasterMatrix = new boolean[8490][4890];
                double angle = thisRoboMaster.mainBody.body.getTransform().getRotation();
                if(angle > 2*PI){
                    angle = angle % (2*PI);
                }
                addRoboMaster(
                        angle,
                        new Vector2(
                                thisRoboMaster.getPosition().x * 1000,
                                thisRoboMaster.getPosition().y * 1000),
                        (int) (thisRoboMaster.property.width * 1000),
                        (int) (thisRoboMaster.property.height * 1000),
                        roboMasterMatrix
                );
            }
        };
    }

    public void step(){
        runnable.run();
    }

    public boolean[][] getMatrix(){
        return roboMasterMatrix;
    }

    private void addRoboMaster(double angle, Vector2 center, int width, int height, boolean[][] matrix){

        Vector2 a = getVertex(angle, new Vector2(center.x - width/2,center.y + height/2), center);
        Vector2 b = getVertex(angle, new Vector2(center.x + width/2,center.y + height/2), center);
        Vector2 c = getVertex(angle, new Vector2(center.x - width/2,center.y - height/2), center);
        Vector2 d = getVertex(angle, new Vector2(center.x + width/2,center.y - height/2), center);
//        matrix[(int)a.x][(int)a.y] = true;
//        matrix[(int)b.x][(int)b.y] = true;
//        matrix[(int)c.x][(int)d.y] = true;
//        matrix[(int)d.x][(int)d.y] = true;
        addLineByTwoPoint(a, b, matrix);
        addLineByTwoPoint(b, d, matrix);
        addLineByTwoPoint(c, d, matrix);
        addLineByTwoPoint(c, a, matrix);
    }

    private Vector2 getVertex(double rotatedAngle, Vector2 orginalPosition, Vector2 centre_p){
        return new Vector2(
                (float) (cos(rotatedAngle) * (orginalPosition.x-centre_p.x) - sin(rotatedAngle) * (orginalPosition.y-centre_p.y) + centre_p.x),
                (float) (sin(rotatedAngle) * (orginalPosition.x-centre_p.x) + cos(rotatedAngle) * (orginalPosition.y-centre_p.y) + centre_p.y)
        );
    }

    private Vector2 roboSidePoint(double angle, Vector2 center, int width, int height){
        double anglein = atan((double)height/(double)width);
        double halfcross = width * width + height * height;
        double resultx = center.x + sin(angle + anglein) * Math.sqrt(halfcross);
        double resulty = center.y + cos(angle + anglein) * Math.sqrt(halfcross);
        return new Vector2((int)resultx, (int) resulty);
    }

    private void addLineByTwoPoint(Vector2 a, Vector2 b, boolean[][] matrix){
//        int middletan = (int) ((a.y - b.y)/(a.x - b.x));
//        if(a.x<b.x){
//            for(int i = (int) a.x ;i<b.x;i++){
//                matrix[i][(int)(a.y + middletan * (i - a.x))] = true;
//            }
//        }
//        else{
//            for(int i = (int) b.x ;i<a.x;i++){
//                matrix[i][(int)(b.y + middletan * (i - a.x))] = true;
//            }
//        }

        float gradient = (a.y - b.y)/(a.x - b.x);
        if(a.x < b.x){
            for(int i = 0 ; i < b.x - a.x; i++ ){
                matrix[(int)a.x + i][(int)(a.y + gradient * i)] = true;
            }
        }
        else{
            for(int i = 0 ; i < a.x - b.x; i++ ){
                matrix[(int)b.x + i][(int)(b.y + gradient * i)] = true;
            }
        }
    }
}
