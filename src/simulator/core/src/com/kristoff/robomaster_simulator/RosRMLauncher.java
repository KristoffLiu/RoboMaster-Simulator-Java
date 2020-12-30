package com.kristoff.robomaster_simulator;

import com.kristoff.robomaster_simulator.envs.Simulator;
import com.kristoff.robomaster_simulator.envs.SimulatorConfiguration;
import com.kristoff.robomaster_simulator.envs.SimulatorMode;
import com.kristoff.robomaster_simulator.robomasters.robomaster.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.teams.RoboMasters;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.pointsimulator.PointSimulator;
import py4j.GatewayServer;

public class RosRMLauncher {
    public Simulator simulator;
    public SimulatorConfiguration config;
    public RoboMaster roboMaster;

    public RosRMLauncher(){
        this.config = new SimulatorConfiguration();
        this.config.mode = SimulatorMode.realMachine;
        this.simulator = new Simulator(config);
        this.simulator.launch();
        this.simulator.init();
    }

    public static void main(String[] args) {
        GatewayServer gatewayServer = new GatewayServer(new RosRMLauncher());
        gatewayServer.start();
        System.out.println("Gateway Server Started");
    }

    public Simulator getSimulator(){
        return simulator;
    }

    public PointSimulator.PointStatus[][] getMap(){
        return Systems.pointSimulator.getMatrix();
    }

    public void setPosition(String name, int x, int y, float rotation){
        RoboMasters.setPosition(name, x,y,rotation);
    }

    public RoboMaster getRoboMaster(String name){
        roboMaster = RoboMasters.getRoboMaster(name);
        return roboMaster;
    }
}
