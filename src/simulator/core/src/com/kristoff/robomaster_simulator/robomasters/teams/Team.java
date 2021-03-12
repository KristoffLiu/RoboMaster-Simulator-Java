package com.kristoff.robomaster_simulator.robomasters.teams;

import com.kristoff.robomaster_simulator.robomasters.robomaster.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.robomaster.types.AlexanderMasterII;
import com.kristoff.robomaster_simulator.robomasters.teams.enemyobservations.EnemiesObservationSimulator;
import com.kristoff.robomaster_simulator.robomasters.teams.friendobservations.FriendsObservationSimulator;

import java.util.concurrent.CopyOnWriteArrayList;

public class Team extends CopyOnWriteArrayList<RoboMaster> {
    String name;
    public FriendsObservationSimulator friendsObservationSimulator; //敌军视野模拟
    public EnemiesObservationSimulator enemiesObservationSimulator; //敌军视野模拟
    public StrategyMaker               strategyMaker;

    public Team(){}

    public Team(String teamName){
        this.name = teamName;
        if(teamName == "Blue"){
            friendsObservationSimulator = new FriendsObservationSimulator(this);
            enemiesObservationSimulator = new EnemiesObservationSimulator(this);
            strategyMaker = new StrategyMaker(this);
        }
    }

    public void start(){
        friendsObservationSimulator.start();
        enemiesObservationSimulator.start();
        strategyMaker.start();
    }

    public int roboMastersLeft(){
        int count = 0;
        for(RoboMaster roboMaster : this){
            if(roboMaster.isAlive()) count++;
        }
        return count;
    }

    public static Team me(){
        return RoboMasters.teamBlue;
    }

    public static Team enemy(){
        return RoboMasters.teamBlue;
    }

    public static int[][] getEnemiesObservationGrid(){
        return me().enemiesObservationSimulator.matrix;
    }

}
