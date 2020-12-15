package com.kristoff.robomaster_simulator.systems.judgement.Buffs;

import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMasters;

public class RedAddBullet extends Buff{
    public RedAddBullet(RoboMaster Robomaster) {
        super(Robomaster);
        for(RoboMaster a : Systems.roboMasters.teamRed){
            a.property.numOfBulletsLeft += 100;
            a.property.numOfBulletsOwned += 100;
        }
    }
}
