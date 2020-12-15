package com.kristoff.robomaster_simulator.judgementsystem.Buffs;

import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.RoboMasters;

public class RedHeal extends Buff{

    public RedHeal(RoboMaster Robomaster) {
        super(Robomaster);
        for(RoboMaster a : RoboMasters.teamRed){
            a.property.health += 200;
        }
    }
}

