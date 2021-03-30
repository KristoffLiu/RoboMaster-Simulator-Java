package com.kristoff.robomaster_simulator.robomasters.Strategy;

import com.badlogic.gdx.math.Vector2;
import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.modules.CostMap;
import com.kristoff.robomaster_simulator.robomasters.types.Enemy;
import com.kristoff.robomaster_simulator.robomasters.types.ShanghaiTechMasterIII;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.pointsimulator.PointState;
import com.kristoff.robomaster_simulator.teams.RoboMasters;
import com.kristoff.robomaster_simulator.teams.enemyobservations.EnemiesObservationSimulator;
import com.kristoff.robomaster_simulator.systems.pointsimulator.PointSimulator;
import com.kristoff.robomaster_simulator.utils.LoopThread;
import com.kristoff.robomaster_simulator.utils.Position;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

public class StrategyMaker extends LoopThread {
    public ShanghaiTechMasterIII roboMaster;
    StrategyAnalyzer strategyAnalyzer;

    /***
     * -1: Not Working
     * 0: 1 v 1 state
     * 1: 1 v 2 state
     * 2: 2 v 1 state
     * 3: 2 v 2 state
     */
    //int counterState = -1;
    Position decisionMade;

    StrategyAnalyzer_2V2Master strategyAnalyzer_2V2Master;
    StrategyAnalyzer_2V2Ranger strategyAnalyzer_2V2Ranger;
    StrategyAnalyzer_2V2MasterObsolete strategyAnalyzer_2V2MasterObsolete;
    StrategyAnalyzer_2V2RangerObsolete strategyAnalyzer_2V2RangerObsolete;

    SearchNode friendDecision;

    public boolean[][] visitedGrid;
    public CostMap costMap;

    public SearchNode                                       rootNode;
    public SearchNode                                       decisionNode;
    public SearchNode                                       resultNode;

    public Queue<SearchNode>                                queue;
    public CopyOnWriteArrayList<SearchNode>                 resultNodes;
    public CopyOnWriteArrayList<SearchNode>                 pathNodes;

    public CounterState counterState = CounterState.TwoVSTwo;
    public TacticState tacticState = TacticState.MOVING;

    public StrategyMaker(RoboMaster roboMaster){
        this.roboMaster = (ShanghaiTechMasterIII)roboMaster;
        visitedGrid = new boolean[849][489];

        rootNode                = new SearchNode();
        decisionNode            = new SearchNode();
        resultNode              = new SearchNode();
        friendDecision          = new SearchNode();

        queue                  = new LinkedList<>();
        resultNodes            = new CopyOnWriteArrayList<SearchNode>();
        pathNodes              = new CopyOnWriteArrayList<SearchNode>();

        strategyAnalyzer_2V2Master = new StrategyAnalyzer_2V2Master(this);
        strategyAnalyzer_2V2Ranger = new StrategyAnalyzer_2V2Ranger(this);
        strategyAnalyzer_2V2MasterObsolete = new StrategyAnalyzer_2V2MasterObsolete(this);
        strategyAnalyzer_2V2RangerObsolete = new StrategyAnalyzer_2V2RangerObsolete(this);

        costMap = roboMaster.costMap;

        this.strategyAnalyzer = strategyAnalyzer_2V2MasterObsolete;
        this.delta = 1/5f;
        this.isStep = true;
    }

    @Override
    public void step(){
        makeDecision();
    }

    public void makeDecision(){
        visitedGrid = new boolean[849][489];
        queue.clear();
        switchAnalyzer();
        strategyAnalyzer.analyze(tacticState);
    }

    public void switchAnalyzer(){
        strategyAnalyzer = strategyAnalyzer_2V2Master;
//        switch (counterState){
//            case OneVSOne -> strategyAnalyzer = strategyAnalyzer_2V2Master;
//            case OneVSTwo -> strategyAnalyzer = strategyAnalyzer_2V2Master;
//            case TwoVSOne -> strategyAnalyzer = this.roboMaster.isRoamer() ? strategyAnalyzer_2V2Ranger : strategyAnalyzer_2V2Master;
//            case TwoVSTwo -> strategyAnalyzer = this.roboMaster.isRoamer() ? strategyAnalyzer_2V2Ranger : strategyAnalyzer_2V2Master;
//        }
    }

    public void update(SearchNode resultNode,
                       boolean[][] visitedGrid,
                       CopyOnWriteArrayList<SearchNode> resultNodes,
                       CopyOnWriteArrayList<SearchNode> pathNodes){
        this.resultNode = resultNode;
        this.visitedGrid = visitedGrid;
        this.resultNodes = resultNodes;
        this.pathNodes = pathNodes;
        changeTacticState();
        decide();
    }

    private void changeTacticState(){
        if(EnemiesObservationSimulator.isInBothEnemiesView(this.resultNode.getX(), this.resultNode.getY())){
            tacticState = TacticState.STATIC;
        }
        else{
            tacticState = TacticState.MOVING;
        }
        if(this.roboMaster.name == "Blue2") System.out.println(this.tacticState);
    }

    private void decide(){
        switch (tacticState){
            case STATIC -> {
                pathNodes.clear();
                decisionNode = new SearchNode(this.getCurrentPosition().x, this.getCurrentPosition().y);
            }
            case MOVING -> {
                decisionNode = resultNode;
            }
        }
        if(!this.roboMaster.isRoamer()){
            this.getFriendRoboMaster().strategyMaker.setFriendDecision(new SearchNode(decisionNode.position.x, decisionNode.position.y));
        }
    }

    public void setFriendDecision(SearchNode node){
        this.friendDecision.position = node.position;
    }

    public SearchNode getFriendDecision(){
        return this.friendDecision;
    }

    public void updateCounterState(CounterState counterState){
        this.counterState = counterState;
    }

    public void makeDecision(int counterState){

    }

    public void setDecisionNode(SearchNode node){
        this.decisionNode = node;
    }

    public Position getDecisionMade(){
        if(this.roboMaster.name.equals("Blue1")){
            //System.out.println(this.decisionNode.position.x + "  " + this.decisionNode.position.y);
        }
        return this.decisionNode.position;
    }

    public CopyOnWriteArrayList<SearchNode> getPathNodes(){
        return this.pathNodes;
    }

    public CopyOnWriteArrayList<SearchNode> getResultNodes(){
        return this.resultNodes;
    }

    public int[][] getEnemiesObservationGrid(){
        return roboMaster.team.getEnemiesObservationGrid();
    }

    public Position getCurrentPosition(){
        return new Position(roboMaster.actor.x / 10, roboMaster.actor.y / 10);
    }

    public PointState getPointStatus(){
        return this.roboMaster.pointState;
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
        Position friendPosition = getFriendRoboMaster().strategyMaker.getDecisionMade();
        Position enemyPosition = Enemy.getLockedEnemy().getPointPosition();
        Vector2 friendSide = new Vector2(friendPosition.x - enemyPosition.x, friendPosition.y - enemyPosition.y);
        Vector2 myside = new Vector2(x - enemyPosition.x, y - enemyPosition.y);
        float result = friendSide.angleDeg(myside);

        return result;
    }

    public SearchNode getDecisionNode() {
        for(RoboMaster roboMaster : RoboMasters.teamBlue){
            if(this.roboMaster != roboMaster) return roboMaster.strategyMaker.decisionNode;
        }
        return null;
    }


    public boolean isPointInsideMap(Position position){
        return  PointSimulator.isPointInsideMap(position.x, position.y);
    }

    public boolean isSafeNow(int x, int y){
        for(int i=0;i<60;i++){
            for(int j=0;j<45;j++){
                Position position = roboMaster.actor.getPoint(i, j);
                if(EnemiesObservationSimulator.isInUnlockedEnemyViewOnly(position.x, position.y))
                    return true;
            }
        }
        return false;
    }

    public boolean isCollidingWithObstacle(int centreX, int centreY){
        for(int i=0;i<45;i++){
            for(int j=0;j<45;j++){
                int x = centreX + i - 23;
                int y = centreY + j - 23;
                if(!Systems.pointSimulator.isPointEmpty(x, y, getPointStatus())
                ){
                    return false;
                }
            }
        }
        return false;
    }

    public boolean isInLockedEnemyViewOnly(int x, int y){
        return EnemiesObservationSimulator.isInLockedEnemyViewOnly(x, y);
    }

    public Position getMinCostPosition(){
        return this.roboMaster.costMap.minPositionCost;
    }


}
