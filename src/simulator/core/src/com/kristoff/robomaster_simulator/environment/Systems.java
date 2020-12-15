package com.kristoff.robomaster_simulator.environment;

import com.kristoff.robomaster_simulator.systems.maps.Map;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMasters;
import com.kristoff.robomaster_simulator.systems.simulators.MatrixSimulator;
import com.kristoff.robomaster_simulator.systems.simulators.PhysicalSimulator;

public class Systems {
    public static Map map;
    public static MatrixSimulator matrixSimulator;
    public static PhysicalSimulator physicalSimulator;

    public Systems(){
        init();
    }

    public void init(){
        map = new Map("CompetitionMap");
        RoboMasters.init();
        physicalSimulator = new PhysicalSimulator();
        matrixSimulator = new MatrixSimulator();
    }

    public static void start(){
        physicalSimulator.start();
        matrixSimulator.start();
    }
}
