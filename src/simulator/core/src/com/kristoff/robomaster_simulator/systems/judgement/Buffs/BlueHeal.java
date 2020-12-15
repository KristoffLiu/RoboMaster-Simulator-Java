package com.kristoff.robomaster_simulator.systems.judgement.Buffs;

import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMasters;

public class BlueHeal extends Buff{

    public BlueHeal(RoboMaster Robomaster) {
        super(Robomaster);
        for(RoboMaster a : Systems.roboMasters.teamBlue){
            a.property.health += 200;
        }
    }
}
