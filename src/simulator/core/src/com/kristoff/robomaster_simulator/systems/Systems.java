package com.kristoff.robomaster_simulator.systems;

import com.kristoff.robomaster_simulator.systems.maps.Map;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMasters;
import com.kristoff.robomaster_simulator.systems.matrixsimulation.MatrixSimulator;
import com.kristoff.robomaster_simulator.systems.simulators.PhysicalSimulator;

public class Systems {
    public static Map map;
    public static MatrixSimulator matrixSimulator;
    public static PhysicalSimulator physicalSimulator;
    public static RoboMasters roboMasters;

    public Systems(){
        init();
    }

    public void init(){
        map = new Map("CompetitionMap");
        roboMasters = new RoboMasters();
        physicalSimulator = new PhysicalSimulator();
        matrixSimulator = new MatrixSimulator();
    }

    public static void start(){
        physicalSimulator.start();
        matrixSimulator.start();
        roboMasters.start();
    }
}
