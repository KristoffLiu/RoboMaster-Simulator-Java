package com.kristoff.robomaster_simulator.robomasters.robomaster.modules;

import com.kristoff.robomaster_simulator.robomasters.robomaster.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.robomaster.tactics.*;
import com.kristoff.robomaster_simulator.robomasters.teams.Team;
import com.kristoff.robomaster_simulator.systems.pointsimulator.PointSimulator;
import com.kristoff.robomaster_simulator.utils.LoopThread;
import com.kristoff.robomaster_simulator.utils.Position;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

public class TacticMaker extends LoopThread {
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
    Position decisionMade;
    OneVSTwoPPTactic oneVSTwoPPTactic;
    TwoVSTwoPPTatic twoVSTwoPPTatic;

    public int[][]                                          enemiesObservationGrid;
    public boolean[][]                                      nodeGrid;

    public SearchNode                                       rootNode;
    public SearchNode                                       decicionNode;


    public Queue<SearchNode>                                queue;


    public CopyOnWriteArrayList<SearchNode>                 resultNodes;
    public CopyOnWriteArrayList<SearchNode>                 pathNodes;



    public TacticMaker(RoboMaster roboMaster){
        this.roboMaster = roboMaster;
        enemiesObservationGrid = Team.getEnemiesObservationGrid()      ;
        nodeGrid               = new boolean[849][489]                 ;
        rootNode               = new SearchNode()                      ;
        decicionNode             = new SearchNode()                      ;
        queue                  = new LinkedList<>()                    ;
        resultNodes            = new CopyOnWriteArrayList<SearchNode>();
        pathNodes              = new CopyOnWriteArrayList<SearchNode>();

        oneVSTwoPPTactic = new OneVSTwoPPTactic(this);
        twoVSTwoPPTatic = new TwoVSTwoPPTatic(this);;

        counterState = 3;
        this.tactic = twoVSTwoPPTatic;
        this.delta = 2f;
        this.isStep = true;
    }

    @Override
    public void step(){
        switch (counterState){
            case -1 ->  { }
            case 0  ->  { }
            case 1  ->  { tactic = oneVSTwoPPTactic;}
            case 2  ->  { }
            case 3  ->  { tactic = twoVSTwoPPTatic;}
            default ->  { }
        }
        tactic.decide();
//        for(int i = 0; i < enemiesObservationGrid.length; i ++){
//            for(int j = 0; j < enemiesObservationGrid[0].length; j ++){
//                this.nodeGrid[i][j] = false;
//            }
//        }
    }

    public void updateCounterState(int counterState){
        this.counterState = counterState;
    }

    public void makeDecision(int counterState){

    }

    public void setDecisionNode(SearchNode node){
        this.decicionNode = node;
    }

    public Position getDecisionMade(){
        return this.decicionNode.position;
    }

    public CopyOnWriteArrayList<SearchNode> getPathNodes(){
        return this.pathNodes;
    }

    public CopyOnWriteArrayList<SearchNode> getResultNodes(){
        return this.resultNodes;
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

    public boolean isOutOfDangerousZone(int x, int y){
        return enemiesObservationGrid[x][y] == 0;
    }

    public boolean isInFirstEnemiesView(int x, int y){
        return enemiesObservationGrid[x][y] == 1;
    }

    public boolean isInSecondEnemiesView(int x, int y){
        return enemiesObservationGrid[x][y] == 2;
    }

    public boolean isInBothEnemiesView(int x, int y){
        return enemiesObservationGrid[x][y] == 3;
    }

    public void clearNodeGrid(){
        nodeGrid = new boolean[849][489];
    }

    public boolean[][] getNodeGrid(){
        return nodeGrid;
    }

    public boolean isVisited(int x, int y){
        return nodeGrid[x][y];
    }
}
