package com.kristoff.robomaster_simulator.robomasters.modules.simulations;

import com.badlogic.gdx.math.Vector2;
import com.kristoff.robomaster_simulator.simulators.MatrixSimulator;

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
