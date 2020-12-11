package com.kristoff.robomaster_simulator.judgementsystem;

import com.kristoff.robomaster_simulator.robomasters.RoboMaster;

public class Heal extends Buff{

    public Heal(RoboMaster Robomaster) {
        super(Robomaster);
        Robomaster.property.health += 200;
    }
}
