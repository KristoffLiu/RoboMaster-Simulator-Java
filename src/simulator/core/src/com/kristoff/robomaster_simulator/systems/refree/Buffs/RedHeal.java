package com.kristoff.robomaster_simulator.systems.refree.Buffs;

import com.kristoff.robomaster_simulator.robomasters.robomaster.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.teams.RoboMasters;

public class RedHeal extends Buff{

    RoboMaster thisRoboMaster;

    public RedHeal(RoboMaster Robomaster) {
        super(Robomaster);
        this.thisRoboMaster = Robomaster;
        act();
    }

    public void act(){
        for(RoboMaster a : RoboMasters.teamRed){
            a.property.health += 200;
        }
    }
}

