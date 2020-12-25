package com.kristoff.robomaster_simulator.systems.refree.Buffs;

import com.kristoff.robomaster_simulator.robomasters.robomaster.RoboMaster;

public class NoShooting extends Buff{

    RoboMaster thisRoboMaster;

    public NoShooting(RoboMaster Robomaster) {
        super(Robomaster);

        this.thisRoboMaster = Robomaster;
        act();
    }
    public void act(){
        thisRoboMaster.property.shootable = false;

    }
}
