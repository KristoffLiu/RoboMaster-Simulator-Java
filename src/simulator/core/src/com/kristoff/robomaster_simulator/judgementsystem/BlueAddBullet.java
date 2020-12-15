package com.kristoff.robomaster_simulator.judgementsystem;

import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.RoboMasters;

public class BlueAddBullet extends Buff{
    public BlueAddBullet(RoboMaster Robomaster) {
        super(Robomaster);
        for(RoboMaster a : RoboMasters.teamBlue){
            a.property.numOfBulletsLeft += 100;
            a.property.numOfBulletsOwned += 100;
        }
    }
}
