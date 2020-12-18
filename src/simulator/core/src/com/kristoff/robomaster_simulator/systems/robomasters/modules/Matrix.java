package com.kristoff.robomaster_simulator.systems.robomasters.modules;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.matrixsimulation.MatrixSimulator;
import com.kristoff.robomaster_simulator.systems.matrixsimulation.RoboMasterPoint;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMasters;

import static java.lang.Math.*;

public class Matrix {
    RoboMaster thisRoboMaster;

    public Array<RoboMasterPoint> current;
    public Array<RoboMasterPoint> previous;
    MatrixSimulator.MatrixPointStatus pointStatus;

    Runnable runnable;

    public Matrix(RoboMaster roboMaster){
        this.thisRoboMaster = roboMaster;
        current = new Array<>();
        previous = new Array<>();

        switch (thisRoboMaster.No){
            case 0 -> {
                pointStatus = MatrixSimulator.MatrixPointStatus.Blue1;
            }
            case 1 -> {
                pointStatus = MatrixSimulator.MatrixPointStatus.Blue2;
            }
            case 2 -> {
                pointStatus = MatrixSimulator.MatrixPointStatus.Red1;
            }
            case 3 -> {
                pointStatus = MatrixSimulator.MatrixPointStatus.Red2;
            }
        }
    }

    public void step(){
        previous = current;
        current = new Array<>();
        double angle = thisRoboMaster.mainBody.body.getTransform().getRotation();
        if(angle > 2*PI){
            angle = angle % (2*PI);
        }
        updateMatrix(
                angle,
                new Vector2(
                        thisRoboMaster.getPosition().x * 1000,
                        thisRoboMaster.getPosition().y * 1000),
                (int) (thisRoboMaster.property.width * 1000),
                (int) (thisRoboMaster.property.height * 1000),
                current
        );
    }

    private void updateMatrix(double angle, Vector2 center, int width, int height, Array<RoboMasterPoint> matrix){
        Vector2 a = getVertex(angle, new Vector2(center.x - width/2,center.y + height/2), center);
        Vector2 b = getVertex(angle, new Vector2(center.x + width/2,center.y + height/2), center);
        Vector2 c = getVertex(angle, new Vector2(center.x - width/2,center.y - height/2), center);
        Vector2 d = getVertex(angle, new Vector2(center.x + width/2,center.y - height/2), center);
        //add lines only
//        addLineByTwoPoint(a, b, matrix);
//        addLineByTwoPoint(b, d, matrix);
//        addLineByTwoPoint(c, d, matrix);
//        addLineByTwoPoint(c, a, matrix);

        //add the whole plane
        addPlaneByFourPoints(a, c, b, d, matrix);
    }

    private Vector2 getVertex(double rotatedAngle, Vector2 orginalPosition, Vector2 centre_p){
        return new Vector2(
                (float) (cos(rotatedAngle) * (orginalPosition.x-centre_p.x) - sin(rotatedAngle) * (orginalPosition.y-centre_p.y) + centre_p.x),
                (float) (sin(rotatedAngle) * (orginalPosition.x-centre_p.x) + cos(rotatedAngle) * (orginalPosition.y-centre_p.y) + centre_p.y)
        );
    }

    private void addLineByTwoPoint(Vector2 a, Vector2 b, Array<RoboMasterPoint> matrix){
        float gradient = (a.y - b.y)/(a.x - b.x);
        MatrixSimulator.MatrixPointStatus pointStatus = MatrixSimulator.MatrixPointStatus.TeamBlue;
        if(thisRoboMaster.team == RoboMasters.teamRed){
            pointStatus = MatrixSimulator.MatrixPointStatus.TeamRed;
        }
        if( Math.abs(a.x - b.x) >= Math.abs(a.y - b.y)){
            if(a.x < b.x){
                for(int i = 0 ; i < b.x - a.x; i++ ){
                    matrix.add(new RoboMasterPoint((int)a.x + i, (int)(a.y + gradient * i),pointStatus));
                }
            }
            else{
                for(int i = 0 ; i < a.x - b.x; i++ ){
                    matrix.add(new RoboMasterPoint((int)b.x + i, (int)(b.y + gradient * i),pointStatus));
                }
            }
        }
        else {
            if(a.y < b.y){
                for(int i = 0 ; i < b.y - a.y; i++ ){
                    matrix.add(new RoboMasterPoint((int)(a.x + i / gradient), (int)(a.y+i),pointStatus));
                }
            }
            else{
                for(int i = 0 ; i < a.y - b.y; i++ ){
                    matrix.add(new RoboMasterPoint((int)(b.x + i / gradient), (int)(b.y+i),pointStatus));
                }
            }
        }
    }


    private void addPlaneByFourPoints(Vector2 leftUp, Vector2 leftDown, Vector2 rightUp, Vector2 rightDown, Array<RoboMasterPoint> matrix){
//        for all four points
//        float leftGradient = (leftUp.y - leftDown.y)/(leftUp.x - leftDown.x);
//        float rightGradient = (rightUp.y - rightDown.y)/(rightUp.x - rightDown.x);
//        if(leftUp.y - leftDown.y < rightUp.y - rightDown.y){
//            for(int i = 0; i <= leftUp.y - leftDown.y; i++){
//                addLineByTwoPoint(new Vector2(leftDown.x + i * leftGradient, leftDown.y + i), new Vector2(rightDown.x + i * rightGradient, rightDown.y + i), matrix);
//            }
//            for(int i = 0; i <= rightUp.y - rightDown.y - leftUp.y + leftDown.y; i++){
//                addLineByTwoPoint(leftUp, new Vector2(rightUp.x + i * rightGradient, rightUp.y + i), matrix);
//            }
//        }else if(leftUp.y - leftDown.y > rightUp.y - rightDown.y){
//            for(int i = 0; i <= rightUp.y - rightDown.y; i++){
//                addLineByTwoPoint(new Vector2(rightDown.x + i * rightGradient, rightDown.y + i), new Vector2(leftDown.x + i * leftGradient, leftDown.y + i), matrix);
//            }
//            for(int i = 0; i <= leftUp.y - leftDown.y - rightUp.y + rightDown.y; i++){
//                addLineByTwoPoint(rightUp, new Vector2(leftUp.x + i * leftGradient, leftUp.y + i), matrix);
//            }
//        }else{
//            for(int i = 0; i <= leftUp.y - leftDown.y; i++){
//                addLineByTwoPoint(new Vector2(leftDown.x + i * leftGradient, leftDown.y + i), new Vector2(rightDown.x + i * rightGradient, rightDown.y + i), matrix);
//            }
//        }
        //for Rectangle only
        if(leftUp.y == rightUp.y){
            for(int i = 0; i <= rightUp.x - leftUp.x; i++){
                Vector2 upMovingPoint = new Vector2(leftUp.x + i, leftUp.y);
                Vector2 downMovingPoint = new Vector2(leftDown.x + i, leftDown.y);
                addLineByTwoPoint(upMovingPoint, downMovingPoint, matrix);
            }
        }else if(leftUp.x == leftDown.x){
            for(int i = 0; i <= leftUp.y - leftDown.y; i++){
                Vector2 upMovingPoint = new Vector2(leftUp.x, leftUp.y + i);
                Vector2 downMovingPoint = new Vector2(rightUp.x, rightUp.y + i);
                addLineByTwoPoint(upMovingPoint, downMovingPoint, matrix);
            }
        }else{
            float gradient = (leftUp.y - rightUp.y)/(leftUp.x - rightUp.x);
            for(int i = 0; i <= rightUp.x - leftUp.x; i++){
                Vector2 upMovingPoint = new Vector2(leftUp.x + i, (int)(leftUp.y + i * gradient));
                Vector2 downMovingPoint = new Vector2(leftDown.x + i, (int)(leftDown.y + i * gradient));
                addLineByTwoPoint(upMovingPoint, downMovingPoint, matrix);
            }
        }

    }
}
