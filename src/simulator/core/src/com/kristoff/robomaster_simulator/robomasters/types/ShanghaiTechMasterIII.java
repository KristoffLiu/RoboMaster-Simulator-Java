package com.kristoff.robomaster_simulator.robomasters.types;

import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.teams.Team;

public class ShanghaiTechMasterIII extends RoboMaster {
    boolean isRoamer = false;

    public ShanghaiTechMasterIII(Team team, String name){
        super("RoboMasters/AlexanderMaster.png",
                team, name);

    }

    public boolean isRoamer(){
        return isRoamer && Team.friend1.isAlive;
    }
    public void setAsRoamer(){
         isRoamer = true;
    }

    @Override
    public void start(){
        super.start();
    }
}
