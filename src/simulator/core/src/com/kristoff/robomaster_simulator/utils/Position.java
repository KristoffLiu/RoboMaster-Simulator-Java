package com.kristoff.robomaster_simulator.utils;

import java.util.Objects;

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

    public boolean isInsideTheMap(boolean isCentiMeter){
        return (x > 20 && x < 829 && y > 20 && y < 469);
    }

    public boolean equals(Position position) {
        return equals(position.x, position.y);
    }

    public boolean equals(int x, int y) {
        return this.x == x && this.y == y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x &&
                y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
