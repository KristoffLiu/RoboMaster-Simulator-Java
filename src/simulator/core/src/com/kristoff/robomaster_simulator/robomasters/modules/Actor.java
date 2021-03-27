package com.kristoff.robomaster_simulator.robomasters.modules;

import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.pointsimulator.PointState;
import com.kristoff.robomaster_simulator.utils.LoopThread;
import com.kristoff.robomaster_simulator.utils.Position;

import static java.lang.Math.*;

public class Actor extends LoopThread {
    public RoboMaster thisRoboMaster;
    public int x;
    public int y;
    public float rotation;
    public float cannonAngle;


    Position[][] matrix;
    PointState pointState;

    public Actor(RoboMaster roboMaster){
        this.thisRoboMaster = roboMaster;
        isStep = true;
        delta = 1/60f;

        switch (thisRoboMaster.No){
            case 0 -> {
                pointState = PointState.Blue1;
            }
            case 1 -> {
                pointState = PointState.Blue2;
            }
            case 2 -> {
                pointState = PointState.Red1;
            }
            case 3 -> {
                pointState = PointState.Red2;
            }
        }

        matrix = new Position[Property.widthUnit][Property.heightUnit];
        for(int i = 0; i < Property.widthUnit ; i++){
            for(int j = 0; j < Property.heightUnit ; j++){
                matrix[i][j] = new Position();
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

    int centre_x = Property.widthUnit / 2;
    int centre_y = Property.heightUnit / 2;
    float previousRotation = 0;

    @Override
    public void step(){
        Position centrePoint = matrix[centre_x][centre_y];
        float centreX = x / 10;
        float centreY = y / 10;
        float delta_rotation = rotation;
        for(int i = 0; i < Property.widthUnit ; i++){
            for(int j = 0; j < Property.heightUnit ; j++){
                Position transformedPoint = matrix[i][j];
                Systems.pointSimulator.updatePoint(transformedPoint.x, transformedPoint.y, PointState.Empty);
                float tempX = centreX - Property.widthUnit / 2 + i;
                float tempY = centreY - Property.heightUnit / 2 + j;
                transformedPoint.x = (int) (cos(delta_rotation) * (tempX-centreX) - sin(delta_rotation) * (tempY-centreY) + centreX);
                transformedPoint.y = (int) (sin(delta_rotation) * (tempX-centreX) + cos(delta_rotation) * (tempY-centreY) + centreY);
                Systems.pointSimulator.updatePoint(transformedPoint.x, transformedPoint.y, this.pointState);
            }
        }
    }

    public Position getPoint(int x, int y){
        return matrix[x][y];
    }

    public void startToFormMatrix(){
        super.start();
    }
}
