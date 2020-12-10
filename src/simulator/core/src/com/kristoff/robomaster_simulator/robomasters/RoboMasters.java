package com.kristoff.robomaster_simulator.robomasters;

import com.badlogic.gdx.utils.Array;
import com.kristoff.robomaster_simulator.robomasters.types.AlexanderMasterII;
import com.kristoff.robomaster_simulator.robomasters.types.RoboMaster;

public class RoboMasters {
    public static Array<RoboMaster> all = new Array<>();
    public static Array<RoboMaster> teamBlue = new Array<>();
    public static Array<RoboMaster> teamRed = new Array<>();

    public static void init(){
        if(all.size == 0){
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
    }
}
