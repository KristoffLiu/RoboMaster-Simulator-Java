package com.kristoff.robomaster_simulator.robomasters.robomaster.modules;

import com.kristoff.robomaster_simulator.robomasters.robomaster.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.robomaster.tactics.OneVSTwoPPTactic;
import com.kristoff.robomaster_simulator.robomasters.robomaster.tactics.OneVSTwoPPTacticNode;
import com.kristoff.robomaster_simulator.robomasters.robomaster.tactics.Tactic;
import com.kristoff.robomaster_simulator.robomasters.teams.StrategyMaker;
import com.kristoff.robomaster_simulator.systems.pointsimulator.PointSimulator;
import com.kristoff.robomaster_simulator.utils.LoopThread;
import com.kristoff.robomaster_simulator.utils.Position;

import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

public class TacticMaker {
    RoboMaster roboMaster;
    Tactic tactic;

    /***
     * -1: Not Working
     * 0: 1 v 1 state
     * 1: 1 v 2 state
     * 2: 2 v 1 state
     * 3: 2 v 2 state
     */
    int counterState = -1;
    Position DecisionMade;

    public TacticMaker(RoboMaster roboMaster){
        this.roboMaster = roboMaster;
        this.tactic = new OneVSTwoPPTactic(this);
    }

    public void makeDecision(int counterState){
        switch (counterState){
            case -1 ->  { }
            case 0  ->  { }
            case 1  ->  { }
            case 2  ->  { }
            case 3  ->  { }
            default ->  { }
        }
        tactic.decide();
    }

    public Position getDecisionMade(){
        return DecisionMade;
    }

    public void setCounterState(int counterState){
        this.counterState = counterState;
    }

    public int[][] getEnemiesObservationGrid(){
        return roboMaster.team.getEnemiesObservationGrid();
    }

    public Position getCurrentPosition(){
        return new Position(roboMaster.actor.x / 10, roboMaster.actor.y / 10);
    }

    public PointSimulator.PointStatus getPointStatus(){
        return this.roboMaster.pointStatus;
    }

    public boolean[][] getNodeGrid(){
        return this.tactic.getNodeGrid();
    }

    public CopyOnWriteArrayList<Position> getResults(){
        return this.tactic.getResults();
    }
}
