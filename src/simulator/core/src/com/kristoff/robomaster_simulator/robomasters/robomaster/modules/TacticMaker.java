package com.kristoff.robomaster_simulator.robomasters.robomaster.modules;

import com.badlogic.gdx.math.Vector2;
import com.kristoff.robomaster_simulator.robomasters.robomaster.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.robomaster.tactics.*;
import com.kristoff.robomaster_simulator.robomasters.robomaster.types.Enemy;
import com.kristoff.robomaster_simulator.robomasters.robomaster.types.ShanghaiTechMasterIII;
import com.kristoff.robomaster_simulator.robomasters.teams.RoboMasters;
import com.kristoff.robomaster_simulator.robomasters.teams.enemyobservations.EnemiesObservationSimulator;
import com.kristoff.robomaster_simulator.systems.pointsimulator.PointSimulator;
import com.kristoff.robomaster_simulator.utils.LoopThread;
import com.kristoff.robomaster_simulator.utils.Position;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

public class TacticMaker extends LoopThread {
    ShanghaiTechMasterIII roboMaster;
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
    OneVSOnePPTactic oneVSOnePPTactic;
    OneVSTwoPPTactic oneVSTwoPPTactic;
    TwoVSOnePPTactic twoVSOnePPTactic;
    TwoVSTwoPPTactic_Striker twoVSTwoPPTacticStriker;
    TwoVSTwoPPTactic_Roamer twoVSTwoPPTactic_roamer;
    SearchNode friendDecision;

    public boolean[][] visitedGrid;

    public SearchNode                                       rootNode;
    public SearchNode decisionNode;

    public Queue<SearchNode>                                queue;
    public CopyOnWriteArrayList<SearchNode>                 resultNodes;
    public CopyOnWriteArrayList<SearchNode>                 pathNodes;

    public RoboMaster lockedEnemy;
    public RoboMaster unlockedEnemy;

    public TacticMaker(RoboMaster roboMaster){
        this.roboMaster = (ShanghaiTechMasterIII)roboMaster;
        visitedGrid = new boolean[849][489]                 ;
        rootNode               = new SearchNode()                      ;
        decisionNode = new SearchNode()                      ;
        queue                  = new LinkedList<>()                    ;
        resultNodes            = new CopyOnWriteArrayList<SearchNode>();
        pathNodes              = new CopyOnWriteArrayList<SearchNode>();
        friendDecision         = new SearchNode();

        oneVSOnePPTactic = new OneVSOnePPTactic(this);
        oneVSTwoPPTactic = new OneVSTwoPPTactic(this);
        twoVSOnePPTactic = new TwoVSOnePPTactic(this);
        twoVSTwoPPTacticStriker = new TwoVSTwoPPTactic_Striker(this);
        twoVSTwoPPTactic_roamer = new TwoVSTwoPPTactic_Roamer(this);


        counterState = 3;
        this.tactic = twoVSTwoPPTacticStriker;
        this.delta = 1/2f;
        this.isStep = true;
    }

    @Override
    public void step(){
        for(RoboMaster enemy : RoboMasters.teamRed){
            if(((Enemy)enemy).isLocked()) lockedEnemy = enemy;
            else unlockedEnemy = enemy;
        }
        if(lockedEnemy == null) lockedEnemy = RoboMasters.teamRed.get(0);
        switch (counterState){
            case -1 ->  { tactic = null;}
            case 0  ->  { tactic = oneVSOnePPTactic;}
            case 1  ->  { tactic = oneVSTwoPPTactic;}
            case 2  ->  { tactic = twoVSOnePPTactic;}
            case 3  ->  {
                tactic = this.roboMaster.isRoamer() ? twoVSTwoPPTactic_roamer : twoVSTwoPPTacticStriker;
            }
            default ->  { }
        }
        queue.clear();
        tactic.decide();
    }

    public void update(SearchNode decisionNode,
                       boolean[][] visitedGrid,
                       CopyOnWriteArrayList<SearchNode> resultNodes,
                       CopyOnWriteArrayList<SearchNode> pathNodes){
        this.decisionNode = decisionNode;
        this.visitedGrid = visitedGrid;
        this.resultNodes = resultNodes;
        this.pathNodes = pathNodes;
    }

    public RoboMaster getLockedEnemy(){
        return lockedEnemy;
    }

    public RoboMaster getUnlockedEnemy(){
        return unlockedEnemy;
    }

    public void updateDecisionForFriend(SearchNode node){
        this.getFriendRoboMaster().tacticMaker.setFriendDecision(new SearchNode(node.position.x, node.position.y));
    }

    public void setFriendDecision(SearchNode node){
        this.friendDecision.position = node.position;
    }

    public SearchNode getFriendDecision(){
        return this.friendDecision;
    }

    public void updateCounterState(int counterState){
        this.counterState = counterState;
    }

    public void makeDecision(int counterState){

    }

    public void setDecisionNode(SearchNode node){
        this.decisionNode = node;
    }

    public Position getDecisionMade(){
        if(this.roboMaster.name.equals("Blue1")){
            System.out.println(this.decisionNode.position.x + "  " + this.decisionNode.position.y);
        }
        return this.decisionNode.position;
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

    public boolean isOnTargetedEnemyView(int x, int y){
        if(!PointSimulator.isPoiontInsideMap(x, y)) return false;
        int targetedVal = 0;
        if(lockedEnemy.name.equals("Red1"))
            targetedVal = 1;
        if(lockedEnemy.name.equals("Red2"))
            targetedVal = 2;
        return EnemiesObservationSimulator.getEnemiesObservationStatus(x, y) == targetedVal;
    }

    public boolean[][] getVisitedGrid(){
        return visitedGrid;
    }

    public boolean isVisited(int x, int y){
        return visitedGrid[x][y];
    }

    public RoboMaster getFriendRoboMaster() {
        for(RoboMaster roboMaster : RoboMasters.teamBlue){
            if(this.roboMaster != roboMaster) return roboMaster;
        }
        return null;
    }

    public float getAngularSeparation(int x, int y) {
        Position friendPosition = getFriendRoboMaster().tacticMaker.getDecisionMade();
        Position enemyPosition = getLockedEnemy().getPointPosition();
        Vector2 friendSide = new Vector2(friendPosition.x - enemyPosition.x, friendPosition.y - enemyPosition.y);
        Vector2 myside = new Vector2(x - enemyPosition.x, y - enemyPosition.y);
        float result = friendSide.angleDeg(myside);
        System.out.println(result);
        return result;
    }

    public SearchNode getDecisionNode() {
        for(RoboMaster roboMaster : RoboMasters.teamBlue){
            if(this.roboMaster != roboMaster) return roboMaster.tacticMaker.decisionNode;
        }
        return null;
    }
}
