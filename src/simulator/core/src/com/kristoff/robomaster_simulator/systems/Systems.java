package com.kristoff.robomaster_simulator.systems;

import com.kristoff.robomaster_simulator.core.SimulatorConfiguration;
import com.kristoff.robomaster_simulator.systems.costmap.UniversalCostMap;
import com.kristoff.robomaster_simulator.systems.maps.Map;
import com.kristoff.robomaster_simulator.systems.pointsimulator.PointSimulator;
import com.kristoff.robomaster_simulator.systems.refree.Refree;
import com.kristoff.robomaster_simulator.systems.simulators.PhysicalSimulator;

public class Systems {
    static SimulatorConfiguration config;
    public static Map map;
    public static PointSimulator pointSimulator;
    public static PhysicalSimulator physicalSimulator;
    public static Refree refree;

    public Systems(SimulatorConfiguration simulatorConfiguration){
        config = simulatorConfiguration;
        init();
    }

    public void init(){
        switch (config.mode){
            case simulator, simulatorRLlib -> {
                map = new Map("CompetitionMap");
                physicalSimulator = new PhysicalSimulator();
                pointSimulator = new PointSimulator();
                refree = new Refree();
            }
            case realMachine -> {
                map = new Map("CompetitionMap");
                pointSimulator = new PointSimulator();
                refree = new Refree();
            }
        }
    }

    public void start(){
        switch (config.mode){
            case simulator, simulatorRLlib -> {
                map.start();
                physicalSimulator.start();
                pointSimulator.start();
                refree.start();
            }
            case realMachine -> {
                map.start();
                pointSimulator.start();
                refree.start();
            }
        }
    }
}
