package com.kristoff.robomaster_simulator.systems;

import com.kristoff.robomaster_simulator.envs.SimulatorConfiguration;
import com.kristoff.robomaster_simulator.systems.maps.Map;
import com.kristoff.robomaster_simulator.systems.matrixsimulation.MatrixSimulator;
import com.kristoff.robomaster_simulator.systems.simulators.PhysicalSimulator;

public class Systems {
    static SimulatorConfiguration config;
    public static Map map;
    public static MatrixSimulator matrixSimulator;
    public static PhysicalSimulator physicalSimulator;

    public Systems(SimulatorConfiguration simulatorConfiguration){
        config = simulatorConfiguration;
        init();
    }

    public void init(){
        switch (config.mode){
            case simulator, simulatorRLlib -> {
                map = new Map("CompetitionMap");
                physicalSimulator = new PhysicalSimulator();
                matrixSimulator = new MatrixSimulator();
            }
            case realMachine -> {
                map = new Map("CompetitionMap");
                matrixSimulator = new MatrixSimulator();
            }
        }
    }

    public void start(){
        switch (config.mode){
            case simulator, simulatorRLlib -> {
                map.start();
                physicalSimulator.start();
                matrixSimulator.start();
            }
            case realMachine -> {
                map.start();
                matrixSimulator.start();
            }
        }
    }
}
