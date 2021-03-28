package com.kristoff.robomaster_simulator.robomasters.Strategy.gradientdescent;

import com.kristoff.robomaster_simulator.utils.Position;

public class DescentStep extends Position {
    public int value;

    public DescentStep(int value, int x, int y){
        this.value = value;
        this.x = x;
        this.x = y;
    }

    public DescentStep(int value, Position position){
        this(value, position.x, position.y);
    }
}
