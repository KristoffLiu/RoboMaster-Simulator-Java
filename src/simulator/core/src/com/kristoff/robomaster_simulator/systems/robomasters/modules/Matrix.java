package com.kristoff.robomaster_simulator.systems.robomasters.modules;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.matrixsimulation.MatrixSimulator;
import com.kristoff.robomaster_simulator.systems.matrixsimulation.RoboMasterPoint;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMasters;
import com.kristoff.robomaster_simulator.utils.BackendThread;

import static java.lang.Math.*;

public class Matrix extends BackendThread {
    RoboMaster thisRoboMaster;
    MatrixSimulator.MatrixPointStatus pointStatus;

    public RoboMasterPoint[][] matrix;

    Runnable runnable;

    public Matrix(RoboMaster roboMaster){
        this.thisRoboMaster = roboMaster;
        isStep = true;
        delta_x = 1/60f;

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

    int centre_x = 300;
    int centre_y = 225;
    float delta_x = 0;
    float delta_y = 0;
    @Override
    public void step(){
        RoboMasterPoint centrePoint = matrix[centre_x][centre_y];
        delta_x = (int)(thisRoboMaster.getPosition().x * 1000 - centrePoint.x);
        delta_y = (int)(thisRoboMaster.getPosition().y * 1000 - centrePoint.y);
        for(int i = 0; i < 600 ; i++){
            for(int j = 0; j < 450 ; j++){
                RoboMasterPoint transformedPoint = matrix[i][j];
                Systems.matrixSimulator.getMatrix()[transformedPoint.x][transformedPoint.y] = MatrixSimulator.MatrixPointStatus.Empty;
                transformedPoint.x += delta_x;
                transformedPoint.y += delta_y;
                Systems.matrixSimulator.getMatrix()[transformedPoint.x][transformedPoint.y] = pointStatus;
            }
        }
    }
}
