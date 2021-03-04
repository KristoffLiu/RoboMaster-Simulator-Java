package com.kristoff.robomaster_simulator.robomasters.robomaster.modules;

import com.kristoff.robomaster_simulator.robomasters.robomaster.RoboMaster;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.pointsimulator.PointSimulator;
import com.kristoff.robomaster_simulator.systems.pointsimulator.StatusPoint;
import com.kristoff.robomaster_simulator.utils.LoopThread;

import static java.lang.Math.*;

public class Actor extends LoopThread {
    public RoboMaster thisRoboMaster;
    public int x;
    public int y;
    public float rotation;
    public float cannonAngle;


    public StatusPoint[][] matrix;
    PointSimulator.PointStatus pointStatus;

    public Actor(RoboMaster roboMaster){
        this.thisRoboMaster = roboMaster;
        isStep = true;
        delta = 1/60f;

        switch (thisRoboMaster.No){
            case 0 -> {
                pointStatus = PointSimulator.PointStatus.Blue1;
            }
            case 1 -> {
                pointStatus = PointSimulator.PointStatus.Blue2;
            }
            case 2 -> {
                pointStatus = PointSimulator.PointStatus.Red1;
            }
            case 3 -> {
                pointStatus = PointSimulator.PointStatus.Red2;
            }
        }

        matrix = new StatusPoint[60][45];
        for(int i = 0; i < 60 ; i++){
            for(int j = 0; j < 45 ; j++){
                matrix[i][j] = new StatusPoint(i+50,j+50,pointStatus);
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
        return (float) (- this.rotation - Math.PI / 2f);
    }

    public float getCannonAngle(){
        //return (float) (- this.thisRoboMaster.RMPhysicalSimulation.cannonBody.getAngle() - Math.PI / 2f);
        return (float) (- this.rotation);
    }

    int centre_x = 30;
    int centre_y = 22;
    float previousRotation = 0;

    @Override
    public void step(){
        StatusPoint centrePoint = matrix[centre_x][centre_y];
        float centreX = x / 10;
        float centreY = y / 10;
        float delta_x = (int)(centreX - centrePoint.x);
        float delta_y = (int)(centreY - centrePoint.y);
        float delta_rotation = rotation - previousRotation;
        previousRotation = rotation;
        for(int i = 0; i < 60 ; i++){
            for(int j = 0; j < 45 ; j++){
                StatusPoint transformedPoint = matrix[i][j];
                Systems.pointSimulator.getMatrix()[transformedPoint.x][transformedPoint.y] = PointSimulator.PointStatus.Empty;
                transformedPoint.x += delta_x;
                transformedPoint.y += delta_y;
                transformedPoint.x = (int) (cos(delta_rotation) * (transformedPoint.x-centreX) - sin(delta_rotation) * (transformedPoint.y-centreY) + centreX);
                transformedPoint.y = (int) (sin(delta_rotation) * (transformedPoint.x-centreX) + cos(delta_rotation) * (transformedPoint.y-centreY) + centreY);
                Systems.pointSimulator.updatePoint(transformedPoint.x, transformedPoint.y, pointStatus);
            }
        }
    }

    public void startToFormMatrix(){
        super.start();
    }
}
