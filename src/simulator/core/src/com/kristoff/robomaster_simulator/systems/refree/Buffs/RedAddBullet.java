package com.kristoff.robomaster_simulator.systems.refree.Buffs;

import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.teams.RoboMasters;

public class RedAddBullet extends Buff{

    RoboMaster thisRoboMaster;

    public RedAddBullet(RoboMaster Robomaster) {
        super(Robomaster);
        this.thisRoboMaster = Robomaster;
        act();
    }
    public void act(){
        for(RoboMaster a : RoboMasters.teamRed){
            a.property.numOfBulletsLeft += 100;
            a.property.numOfBulletsOwned += 100;
        }
    }
}
