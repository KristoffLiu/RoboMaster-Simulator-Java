package com.kristoff.robomaster_simulator.systems.refree.Buffs;

import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.teams.RoboMasters;

public class BlueHeal extends Buff{

    RoboMaster thisRoboMaster;


    public BlueHeal(RoboMaster Robomaster) {
        super(Robomaster);

        this.thisRoboMaster = Robomaster;
        act();
    }

        public void act(){
            for(RoboMaster a : RoboMasters.teamBlue) {
                a.property.health += 200;
            }
        }

}
