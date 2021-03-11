package com.kristoff.robomaster_simulator.robomasters.robomaster.types;

import com.kristoff.robomaster_simulator.robomasters.robomaster.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.teams.Team;

public class ShanghaiTechMasterIII extends RoboMaster {
    boolean isRoamer = false;

    public ShanghaiTechMasterIII(Team team, String name){
        super("RoboMasters/AlexanderMaster.png",
                team, name);
    }

    public boolean isRoamer(){
        return isRoamer;
    }
    public void setAsRoamer(){
         isRoamer = true;
    }
}
