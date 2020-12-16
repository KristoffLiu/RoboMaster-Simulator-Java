package com.kristoff.robomaster_simulator.systems.refree.Buffs;

import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMaster;

public class BlueHeal extends Buff{

    public BlueHeal(RoboMaster Robomaster) {
        super(Robomaster);
        for(RoboMaster a : Systems.roboMasters.teamBlue){
            a.property.health += 200;
        }
    }
}
