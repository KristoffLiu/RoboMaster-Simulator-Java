package com.kristoff.robomaster_simulator.robomasters;

import com.badlogic.gdx.utils.Array;

public class RoboMasters {
    Array<RoboMaster> all = new Array<>();
    Array<RoboMaster> teamBlue = new Array<>();
    Array<RoboMaster> teamRed = new Array<>();

    public RoboMasters(){
        for(int i = 0; i <= 1; i++){
            RoboMaster roboMaster = new AlexanderMasterII();
            teamBlue.add(roboMaster);
        }
        for(int i = 0; i <= 1; i++){
            teamRed.add(new AlexanderMasterII());
        }
        all.addAll(teamBlue);
        all.addAll(teamRed);
    }

    public Array<RoboMaster> getAll(){
        return all;
    }

    public Array<RoboMaster> getTeamBlue(){
        return teamBlue;
    }

    public Array<RoboMaster> getTeamRed(){
        return teamRed;
    }
}
