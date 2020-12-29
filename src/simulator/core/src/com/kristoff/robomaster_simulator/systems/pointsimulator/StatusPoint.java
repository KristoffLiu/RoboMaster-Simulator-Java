package com.kristoff.robomaster_simulator.systems.pointsimulator;

public class StatusPoint {
    public int x;
    public int y;
    public PointSimulator.PointStatus status;

    public StatusPoint(int x, int y, PointSimulator.PointStatus status){
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

    public PointSimulator.PointStatus getPointStatus(){
        return status;
    }
}
