package com.kristoff.robomaster_simulator.robomasters.Strategy;

import com.kristoff.robomaster_simulator.robomasters.modules.Property;
import com.kristoff.robomaster_simulator.robomasters.types.Enemy;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.buffs.BuffZone;
import com.kristoff.robomaster_simulator.systems.pointsimulator.PointSimulator;
import com.kristoff.robomaster_simulator.teams.enemyobservations.EnemiesObservationSimulator;
import com.kristoff.robomaster_simulator.utils.Position;

import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

public class StrategyAnalyzer_2V2RangerObsolete implements StrategyAnalyzer {
    public StrategyMaker strategyMaker;

    public SearchNode                               rootNode;
    public Queue<SearchNode>                        queue;
    public SearchNode                               resultNode;
    public CopyOnWriteArrayList<SearchNode>         resultNodes;
    public CopyOnWriteArrayList<SearchNode>         pathNodes;

    Position destination = new Position();

    public StrategyAnalyzer_2V2RangerObsolete(StrategyMaker strategyMaker){
        this.strategyMaker = strategyMaker;

        this.queue                      = this.strategyMaker.queue;
        this.resultNodes                = new CopyOnWriteArrayList<>();
        this.pathNodes                  = new CopyOnWriteArrayList<>();
    }

    @Override
    public void analyze(TacticState tacticState) {
        Position currentPosition = strategyMaker.getCurrentPosition();
        if(shouldIMove(currentPosition.x, currentPosition.y)){
            scanMap(currentPosition);
        }
        else {
            pathNodes.clear();
            return;
        }
    }

    public boolean shouldIMove(int x, int y){
        //return !strategyMaker.isSafeNow(x, y);
        return true;
    }

    public void scanMap(Position currentPosition){
        boolean[][] tempVisitedGrid = new boolean[849][489];

        this.rootNode = new SearchNode(
                currentPosition.x,
                currentPosition.y,
                -1,
                0,
                null);
        this.resultNode = rootNode;
        queue.offer(rootNode);
        tempVisitedGrid[rootNode.position.x][rootNode.position.y] = true;

        while (!this.queue.isEmpty()){
            resultNode = this.queue.poll();
            if(isAvailable(resultNode.position)) {
                break;
            }
            generateChildrenNodes(resultNode, tempVisitedGrid);
        }
        SearchNode node = resultNode;
        pathNodes.clear();
        while (true && node.parentNode != null){
            pathNodes.add(node);
            node = node.parentNode;
        }
        this.strategyMaker.update(resultNode, tempVisitedGrid, resultNodes, pathNodes);
    }

    public boolean isAvailable(Position centrePosition){
        return isTheSurroundingAreaAvailable(centrePosition) &&
                Enemy.getLockedEnemy().getPointPosition().distanceTo(centrePosition) > 45 &&
                Enemy.getLockedEnemy().getPointPosition().distanceTo(centrePosition) < 600 &&
                this.strategyMaker.getFriendDecision().position.distanceTo(centrePosition) > 150;
    }

    public boolean isTheSurroundingAreaAvailable(Position centrePosition){
        for(int i=0;i<45;i++){
            for(int j=0;j<45;j++){
                int x = centrePosition.x + i - 23;
                int y = centrePosition.y + j - 23;
                if(   !(x>=0 && x<849)
                        || !(y>=0 && y<489)
                        || !EnemiesObservationSimulator.isInLockedEnemyViewOnly(x, y)
                        || Systems.pointSimulator.isPointNotEmpty(x,y, strategyMaker.getPointStatus())
                        || BuffZone.isInDebuffZone(centrePosition.x, centrePosition.y)
                ){
                    return false;
                }
            }
        }
        return true;
    }

    //查找并生成子节点，并返回队列对象
    public void generateChildrenNodes(SearchNode node, boolean[][] visitedGrid){
        if(!PointSimulator.isPointInsideMap(node.position.x, node.position.y)) return;
        visitedGrid[node.position.x][node.position.y] = true;
        for(int i=0; i < SearchNode.childrenNodesFindingCost.length; i++){
            int x = node.position.x + SearchNode.childrenNodesFindingCost[i][0] ;
            int y = node.position.y + SearchNode.childrenNodesFindingCost[i][1] ;
            double cost = Math.sqrt(SearchNode.childrenNodesFindingCost[i][2]);
            if(Systems.pointSimulator.isPointNotEmpty(x,y, strategyMaker.getPointStatus())){
                continue;
            }
//            if(Enemy.getLockedEnemy().getPointPosition().distanceTo(x,y) < 50){
//                if(node.position.distanceTo(Enemy.getLockedEnemy().getPointPosition()) > Enemy.getLockedEnemy().getPointPosition().distanceTo(x,y)){
//                    continue;
//                }
//            }
//            if(Enemy.getLockedEnemy().getPointPosition().distanceTo(x,y) > 7500){
//                if(node.position.distanceTo(Enemy.getLockedEnemy().getPointPosition()) < Enemy.getLockedEnemy().getPointPosition().distanceTo(x,y)){
//                    continue;
//                }
//            }
//            if(Enemy.getLockedEnemy().getPointPosition().distanceTo(x,y) < 100){
//                if(node.position.distanceTo(Enemy.getLockedEnemy().getPointPosition()) > Enemy.getLockedEnemy().getPointPosition().distanceTo(x,y)){
//                    continue;
//                }
//            }
//            if(this.strategyMaker.getFriendRoboMaster().getPointPosition().distanceTo(x,y) < 100){
//                if(node.position.distanceTo(this.strategyMaker.getFriendRoboMaster().getPointPosition()) > this.strategyMaker.getFriendRoboMaster().getPointPosition().distanceTo(x,y)){
//                    continue;
//                }
//            }
            if(!isFitIntoThePosition(x, y)) continue;
            if(hasThisNodeNotBeenVisited(x, y, visitedGrid)){
                SearchNode childNode = new SearchNode(x,y,node.index + 1, cost,node);
                node.childrenNodes.add(childNode);
                queue.offer(childNode);
            }
        }
    }

    //检查节点可访问性
    public boolean hasThisNodeNotBeenVisited(int x, int y, boolean[][] nodeGrid){
        if(x>=0 && x<849 && y>=0 && y<489){
            if(nodeGrid[x][y]){
                return false;
            }
            else {
                nodeGrid[x][y] = true;
                return true;
            }
        }
        else {
            return true;
        }
    }

    public boolean isFitIntoThePosition(int x, int y){
        for(int i = 0; i< Property.widthUnit; i+=10){
            for(int j=0;j< Property.heightUnit;j+=10){
                int m = x + i - Property.widthUnit / 2;
                int n = y + j - Property.heightUnit / 2;
                if(Systems.pointSimulator.isPointNotEmpty(m, n, strategyMaker.getPointStatus())){
                    return false;
                }
            }
        }
        return true;
    }
}
