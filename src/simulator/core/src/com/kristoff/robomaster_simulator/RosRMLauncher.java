package com.kristoff.robomaster_simulator;

import com.kristoff.robomaster_simulator.core.Simulator;
import com.kristoff.robomaster_simulator.core.SimulatorConfiguration;
import com.kristoff.robomaster_simulator.core.SimulatorMode;
import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.types.Enemy;
import com.kristoff.robomaster_simulator.systems.buffs.BuffZone;
import com.kristoff.robomaster_simulator.systems.pointsimulator.PointState;
import com.kristoff.robomaster_simulator.teams.RoboMasters;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.teams.Team;
import py4j.GatewayServer;

import java.util.ArrayList;
import java.util.List;

public class RosRMLauncher {
    public Simulator simulator;
    public SimulatorConfiguration config;
    public RoboMaster roboMaster;
    public List<Integer> demo;

    public RosRMLauncher(){
        this.config = new SimulatorConfiguration();
        this.config.mode = SimulatorMode.realMachine;
        this.simulator = new Simulator(config);
        this.simulator.launch();
        this.simulator.init();
        demo = new ArrayList<>();
        for(int i = 0; i < 500; i ++){
            demo.add(i);
        }
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

    public List<Integer> getDemoList(){
        return this.demo;
    }

    public void buffZoneDemoTest(){
        BuffZone.updateBuffZone(0,1, false);
        BuffZone.updateBuffZone(1,5, false);
        BuffZone.updateBuffZone(2,4, false);
        BuffZone.updateBuffZone(3,2, false);
        BuffZone.updateBuffZone(4,6, false);
        BuffZone.updateBuffZone(5,3, false);
    }

    public void setAsRoamer(String roboName){
        if(roboName.equals("blue1")){
            Team.friend1.setAsRoamer();
        }
        else{
            Team.friend2.setAsRoamer();
        }
    }

    public void isOurTeamBlue(boolean bool){
        Team.isOurTeamBlue = bool;
    }

    public Enemy getLockedEnemy(){
        return Enemy.getLockedEnemy();
    }

    public void updateRemainingTime(int remainTime){
        Systems.refree.remainingTime = remainTime;
    }

    public void updateGameStatus(int gameStatus){
        Systems.refree.updateGameStatus(gameStatus);
    }
}
