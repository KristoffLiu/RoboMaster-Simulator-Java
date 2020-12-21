package com.kristoff.robomaster_simulator.robomasters.modules;

import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.matrixsimulation.MatrixSimulator;
import com.kristoff.robomaster_simulator.systems.matrixsimulation.RoboMasterPoint;
import com.kristoff.robomaster_simulator.utils.BackendThread;

import static java.lang.Math.*;

public class Actor extends BackendThread {
    public RoboMaster thisRoboMaster;
    public int x;
    public int y;
    public float rotation;
    public float cannonAngle;


    public RoboMasterPoint[][] matrix;
    MatrixSimulator.MatrixPointStatus pointStatus;

    public Actor(RoboMaster roboMaster){
        this.thisRoboMaster = roboMaster;
        isStep = true;
        delta = 1/60f;

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

        matrix = new RoboMasterPoint[600][450];
        for(int i = 0; i < 600 ; i++){
            for(int j = 0; j < 450 ; j++){
                matrix[i][j] = new RoboMasterPoint(i+500,j+500,pointStatus);
            }
        }
    }

    public void updateX(int x){
        this.x = x;
    }

    public void updateY(int y){
        this.y = y;
    }

    public void updateRotation(float rotation){
        this.rotation = rotation;
    }

    public void update(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void update(int x, int y, float rotation){
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }

//    float currentAngle = thisRoboMaster.RMPhysicalSimulation.body.getTransform().getRotation();
//        if(currentAngle > 2*PI){
//        currentAngle = (float)(currentAngle % (2*PI));
//    }

    public float getFacingAngle(){
        //return (float) (- this.RMPhysicalSimulation.body.getAngle() - Math.PI / 2f)
        return (float) (- this.rotation);
    }

    public float getCannonAngle(){
        //return (float) (- this.thisRoboMaster.RMPhysicalSimulation.cannonBody.getAngle() - Math.PI / 2f);
        return (float) (- this.rotation);
    }

    int centre_x = 300;
    int centre_y = 225;
    float previousRotation = 0;

    @Override
    public void step(){
        RoboMasterPoint centrePoint = matrix[centre_x][centre_y];
        float centreX = x;
        float centreY = y;
        float delta_x = (int)(centreX - centrePoint.x);
        float delta_y = (int)(centreY - centrePoint.y);
        float delta_rotation = rotation - previousRotation;
        previousRotation = rotation;
        for(int i = 0; i < 600 ; i++){
            for(int j = 0; j < 450 ; j++){
                RoboMasterPoint transformedPoint = matrix[i][j];
                Systems.matrixSimulator.getMatrix()[transformedPoint.x][transformedPoint.y] = MatrixSimulator.MatrixPointStatus.Empty;
                transformedPoint.x += delta_x;
                transformedPoint.y += delta_y;
                transformedPoint.x = (int) (cos(delta_rotation) * (transformedPoint.x-centreX) - sin(delta_rotation) * (transformedPoint.y-centreY) + centreX);
                transformedPoint.y = (int) (sin(delta_rotation) * (transformedPoint.x-centreX) + cos(delta_rotation) * (transformedPoint.y-centreY) + centreY);
                Systems.matrixSimulator.getMatrix()[transformedPoint.x][transformedPoint.y] = pointStatus;
            }
        }
    }


}
