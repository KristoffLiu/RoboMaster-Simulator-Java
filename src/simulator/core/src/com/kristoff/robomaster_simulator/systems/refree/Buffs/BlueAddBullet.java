package com.kristoff.robomaster_simulator.systems.refree.Buffs;

import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMaster;

public class BlueAddBullet extends Buff{
    public BlueAddBullet(RoboMaster Robomaster) {
        super(Robomaster);
    }
    public void act(){
        for(RoboMaster a : Systems.roboMasters.teamBlue){
            a.property.numOfBulletsLeft += 100;
            a.property.numOfBulletsOwned += 100;
        }
    }
}
