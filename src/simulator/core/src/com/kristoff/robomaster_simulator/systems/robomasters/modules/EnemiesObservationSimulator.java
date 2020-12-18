package com.kristoff.robomaster_simulator.systems.robomasters.modules;

import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMasterList;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMasters;
import com.kristoff.robomaster_simulator.utils.BackendThread;

public class EnemiesObservationSimulator extends BackendThread {
    RoboMaster thisRoboMaster;
    Mode mode;

    public EnemiesObservationSimulator(RoboMaster roboMaster){
        this.thisRoboMaster = roboMaster;
        mode = Mode.global_observation;
        isStep = true;
        delta = 1f;
    }

    public enum Mode{
        self_observation,
        global_observation
    }

    public enum EnemyObservationMapPoint{
        none,
        enemy1,
        enemy2,
        both
    }

    @Override
    public void step(){
        switch (mode){
            case self_observation -> {

            }
            case global_observation -> {
                if(thisRoboMaster.team == RoboMasters.teamBlue)
                {

                }
                else{

                }
            }
        }
    }

    public void a(RoboMasterList team){
        
    }
}
