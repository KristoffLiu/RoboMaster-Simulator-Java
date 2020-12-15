package com.kristoff.robomaster_simulator.systems.robomasters.modules.simulations;

import com.kristoff.robomaster_simulator.systems.simulators.MatrixSimulator;

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
