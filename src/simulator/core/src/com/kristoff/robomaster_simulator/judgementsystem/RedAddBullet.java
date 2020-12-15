package com.kristoff.robomaster_simulator.judgementsystem;

import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.RoboMasters;

public class RedAddBullet extends Buff{
    public RedAddBullet(RoboMaster Robomaster) {
        super(Robomaster);
        for(RoboMaster a : RoboMasters.teamRed){
            a.property.numOfBulletsLeft += 100;
            a.property.numOfBulletsOwned += 100;
        }
    }
}
