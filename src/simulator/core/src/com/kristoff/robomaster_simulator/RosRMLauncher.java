package com.kristoff.robomaster_simulator;

import com.kristoff.robomaster_simulator.core.Simulator;
import com.kristoff.robomaster_simulator.core.SimulatorConfiguration;
import com.kristoff.robomaster_simulator.core.SimulatorMode;
import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.systems.buffs.BuffZone;
import com.kristoff.robomaster_simulator.systems.pointsimulator.PointState;
import com.kristoff.robomaster_simulator.teams.RoboMasters;
import com.kristoff.robomaster_simulator.systems.Systems;
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

    public PointState[][] getMap(){
        return Systems.pointSimulator.getMatrix();
    }

    public void setPosition(String name, int x, int y, float rotation){
        RoboMasters.setPosition(name, x,y,rotation);
    }

    public RoboMaster getRoboMaster(String name){
        roboMaster = RoboMasters.getRoboMaster(name);
        return roboMaster;
    }

    public void updateBuffZone(int buffZoneNo, int buffType, boolean isActive){
        BuffZone.updateBuffZone(buffZoneNo, buffType, isActive);
    }
}
