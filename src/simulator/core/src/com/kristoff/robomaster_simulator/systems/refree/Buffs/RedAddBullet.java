package com.kristoff.robomaster_simulator.systems.refree.Buffs;

import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMaster;

public class RedAddBullet extends Buff{
    public RedAddBullet(RoboMaster Robomaster) {
        super(Robomaster);
        for(RoboMaster a : Systems.roboMasters.teamRed){
            a.property.numOfBulletsLeft += 100;
            a.property.numOfBulletsOwned += 100;
        }
    }
}
