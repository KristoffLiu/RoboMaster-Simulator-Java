package com.kristoff.robomaster_simulator.utils;

public class Position {
    public int x;
    public int y;

    public Position(){
        this.x = 0;
        this.y = 0;
    }

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public float distanceTo(Position position){
        return distanceTo(position.x, position.y);
    }

    public float distanceTo(int x, int y){
        return (float)Math.hypot((this.x - x), (this.y - y));
    }

    public boolean isInsideTheMap(){
        return (x > 205 && x < 8285 && y > 205 && y < 4685);
    }
}
