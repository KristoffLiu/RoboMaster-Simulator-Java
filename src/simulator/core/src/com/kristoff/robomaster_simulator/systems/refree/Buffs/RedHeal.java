package com.kristoff.robomaster_simulator.systems.refree.Buffs;

import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMaster;

public class RedHeal extends Buff{

    public RedHeal(RoboMaster Robomaster) {
        super(Robomaster);
        for(RoboMaster a : Systems.roboMasters.teamRed){
            a.property.health += 200;
        }
    }
}

