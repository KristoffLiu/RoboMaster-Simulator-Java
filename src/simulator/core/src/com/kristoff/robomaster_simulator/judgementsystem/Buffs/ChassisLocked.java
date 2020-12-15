package com.kristoff.robomaster_simulator.judgementsystem.Buffs;

import com.kristoff.robomaster_simulator.systems.robomasters.RoboMaster;

public class ChassisLocked extends Buff{

    public ChassisLocked(RoboMaster Robomaster) {
        super(Robomaster);

        Robomaster.property.movable = false;
    }
}
