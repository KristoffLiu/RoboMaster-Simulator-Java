package com.kristoff.robomaster_simulator.judgementsystem.Buffs;

import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.RoboMasters;

public class BlueHeal extends Buff{

    public BlueHeal(RoboMaster Robomaster) {
        super(Robomaster);
        for(RoboMaster a : RoboMasters.teamBlue){
            a.property.health += 200;
        }
    }
}
