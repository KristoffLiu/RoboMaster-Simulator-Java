package com.kristoff.robomaster_simulator.systems.pointsimulator;

public class StatePoint {
    public int x;
    public int y;
    public PointState status;

    public StatePoint(int x, int y, PointState status){
        this.x = x;
        this.y = y;
        this.status = status;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public PointState getPointStatus(){
        return status;
    }
}
