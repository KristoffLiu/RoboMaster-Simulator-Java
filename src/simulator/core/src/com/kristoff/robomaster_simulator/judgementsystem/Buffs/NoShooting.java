package com.kristoff.robomaster_simulator.judgementsystem.Buffs;

import com.kristoff.robomaster_simulator.systems.robomasters.RoboMaster;

public class NoShooting extends Buff{
    public NoShooting(RoboMaster Robomaster) {
        super(Robomaster);

        Robomaster.property.shootable = false;

    }
}
