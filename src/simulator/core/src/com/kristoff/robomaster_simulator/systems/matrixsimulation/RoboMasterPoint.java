package com.kristoff.robomaster_simulator.systems.matrixsimulation;

import com.kristoff.robomaster_simulator.systems.matrixsimulation.MatrixSimulator;

public class RoboMasterPoint {
    public int x;
    public int y;
    public MatrixSimulator.MatrixPointStatus status;

    public RoboMasterPoint(int x, int y, MatrixSimulator.MatrixPointStatus status){
        this.x = x;
        this.y = y;
        this.status = status;
    }
}