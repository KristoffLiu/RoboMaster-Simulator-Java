package com.kristoff.robomaster_simulator.robomasters;

import com.badlogic.gdx.utils.Array;
import com.kristoff.robomaster_simulator.robomasters.modules.simulations.RoboMasterPoint;
import com.kristoff.robomaster_simulator.robomasters.types.AlexanderMasterII;
import com.kristoff.robomaster_simulator.simulators.MatrixSimulator;

public class RoboMasters extends Array<RoboMaster> {
    public static RoboMasters all       = new RoboMasters();
    public static RoboMasters teamBlue  = new RoboMasters();
    public static RoboMasters teamRed   = new RoboMasters();

    static Runnable runnable;

    public static void init(){
        if(all.size == 0){
            for(int i = 0; i <= 1; i++){
                new AlexanderMasterII(teamBlue);
            }
            for(int i = 0; i <= 1; i++){
                new AlexanderMasterII(teamRed);
            }
            all.addAll(teamBlue);
            all.addAll(teamRed);
        }
        runnable = new Runnable() {
            @Override
            public void run() {
                for(RoboMaster roboMaster : all){
                    roboMaster.matrix.step();
                }
            }
        };
    }

    public static Array<RoboMasterPoint> getCurrentPoints(){
        synchronized (all){
            Array<RoboMasterPoint> currentPoints = new Array<>();
            for(RoboMaster roboMaster : all){
                currentPoints.addAll(roboMaster.matrix.current);
            }
            return currentPoints;
        }
    }

    public static Array<RoboMasterPoint> getPreviousPoints(){
        synchronized (all){
            Array<RoboMasterPoint> previousPoints = new Array<>();
            for(RoboMaster roboMaster : all){
                previousPoints.addAll(roboMaster.matrix.previous);
            }
            return previousPoints;
        }
    }

    public static void stepMatrix(){
        //runnable.run();
        synchronized (all){
            for(RoboMaster roboMaster : all){
                roboMaster.matrix.step();
            }
        }
    }

    public static void stepObservation(){
//        for(RoboMaster roboMaster : all){
//            roboMaster.observation.step();
//        }
        teamBlue.get(0).observation.step();
    }
}
