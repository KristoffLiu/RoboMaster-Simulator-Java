package com.kristoff.robomaster_simulator.systems.costmap;

import com.kristoff.robomaster_simulator.utils.Position;

public class PositionCost extends Position {
    public int cost;

    public PositionCost(int value, int x, int y){
        this.cost = value;
        this.x = x;
        this.x = y;
    }

    public PositionCost(int value, Position position){
        this(value, position.x, position.y);
    }
}
