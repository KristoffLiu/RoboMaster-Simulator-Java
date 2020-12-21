package com.kristoff.robomaster_simulator.launchers.py_entrypoints.rosrmentrypoint;

import com.kristoff.robomaster_simulator.envs.Simulator;
import com.kristoff.robomaster_simulator.envs.SimulatorConfiguration;
import com.kristoff.robomaster_simulator.envs.SimulatorMode;
import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.RoboMasters;
import com.kristoff.robomaster_simulator.robomasters.modules.RMPhysicalSimulation;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.matrixsimulation.MatrixSimulator;
import py4j.GatewayServer;

public class RosRMEntryPoint{
    public Simulator simulator;
    public SimulatorConfiguration config;
    public RoboMaster roboMaster;

    public RosRMEntryPoint(){
        this.config = new SimulatorConfiguration();
        this.config.mode = SimulatorMode.realMachine;
        this.simulator = new Simulator(config);
        this.simulator.launch();
        this.simulator.init();
    }

    public static void main(String[] args) {
        GatewayServer gatewayServer = new GatewayServer(new RosRMEntryPoint());
        gatewayServer.start();
        System.out.println("Gateway Server Started");
    }

    public Simulator getSimulator(){
        return simulator;
    }

    public MatrixSimulator.MatrixPointStatus[][] getMap(){
        return Systems.matrixSimulator.getMatrix();
    }

    public void setPosition(String name, int x, int y, float rotation){
        RoboMasters.setPosition(name, x,y,rotation);
    }

    public RoboMaster getRoboMaster(String name){
        roboMaster = RoboMasters.getRoboMaster(name);
        return roboMaster;
    }
}
