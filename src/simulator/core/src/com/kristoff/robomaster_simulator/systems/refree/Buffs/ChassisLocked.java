package com.kristoff.robomaster_simulator.systems.refree.Buffs;

import com.kristoff.robomaster_simulator.systems.robomasters.RoboMaster;

public class ChassisLocked extends Buff{

    RoboMaster thisRoboMaster;

    public ChassisLocked(RoboMaster Robomaster) {
        super(Robomaster);
        this.thisRoboMaster = Robomaster;
        act();
    }

        public void act(){
            thisRoboMaster.property.movable = false;
        }
}
