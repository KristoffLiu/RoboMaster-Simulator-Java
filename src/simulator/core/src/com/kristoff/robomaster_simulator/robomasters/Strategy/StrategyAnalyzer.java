package com.kristoff.robomaster_simulator.robomasters.Strategy;

import com.kristoff.robomaster_simulator.robomasters.types.Enemy;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.pointsimulator.PointSimulator;
import com.kristoff.robomaster_simulator.teams.enemyobservations.EnemiesObservationSimulator;
import com.kristoff.robomaster_simulator.utils.Position;

import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

public class StrategyAnalyzer implements Strategy {
    public StrategyMaker strategyMaker;

    public SearchNode rootNode;
    public Queue<SearchNode>                      queue;
    public SearchNode resultNode;
    public CopyOnWriteArrayList<SearchNode>                   resultNodes;
    public CopyOnWriteArrayList<SearchNode>                   pathNodes;

    Position destination = new Position();

    public StrategyAnalyzer(StrategyMaker strategyMaker){
        this.strategyMaker = strategyMaker;

        this.queue                      = this.strategyMaker.queue;
        this.resultNodes                = new CopyOnWriteArrayList<>();
        this.pathNodes                  = new CopyOnWriteArrayList<>();
    }

    @Override
    public void analyze(TacticState tacticState) {
        Position currentPosition = strategyMaker.getCurrentPosition();
        if(shouldIMove(currentPosition.x, currentPosition.y)){
            AvoidOneVSTwoTactic(currentPosition);
        }
        else {
            pathNodes.clear();
            return;
        }
    }

    public boolean shouldIMove(int x, int y){
        return !strategyMaker.isSafeNow(x, y);
    }

    public void AvoidOneVSTwoTactic(Position currentPosition){
        boolean[][] visitedGrid = new boolean[849][489];

        this.rootNode = new SearchNode(
                currentPosition.x,
                currentPosition.y,
                -1,
                0,
                null);
        this.resultNode = rootNode;
        queue.offer(rootNode);
        visitedGrid[rootNode.position.x][rootNode.position.y] = true;

        int count = 0;
        while (!this.queue.isEmpty()){
            resultNode = this.queue.poll();
            if(!EnemiesObservationSimulator.isInBothEnemiesView(resultNode.position.x, resultNode.position.y)){
                count ++;
            }
            if(count > 2){
                if(isAvailable()) {
                    break;
                }
            }
            generateChildrenNodes(resultNode, visitedGrid);
        }
        SearchNode node = resultNode;
        pathNodes.clear();
        while (true && node.parentNode != null){
            pathNodes.add(node);
            node = node.parentNode;
        }

        this.strategyMaker.update(resultNode, visitedGrid, resultNodes, pathNodes);
    }

    public boolean isAvailable(){
        for(int i=0;i<45;i++){
            for(int j=0;j<45;j++){
                int x = resultNode.position.x + i - 23;
                int y = resultNode.position.y + j - 23;
                if(   !(x>=0 && x<849)
                        || !(y>=0 && y<489)
                        || EnemiesObservationSimulator.isInBothEnemiesView(x,y)
                        || !this.strategyMaker.isInLockedEnemyViewOnly(x,y)
                        || Systems.pointSimulator.isPointNotEmpty(x,y, strategyMaker.getPointStatus())
                        || Enemy.getLockedEnemy().getPointPosition().distanceTo(x,y) < 30
                        || Enemy.getLockedEnemy().getPointPosition().distanceTo(x,y) > 600
                        || this.strategyMaker.getFriendDecision().position.distanceTo(x,y) < 150
//                        || this.tacticMaker.getAngularSeparation(x, y) < 30
//                        || this.tacticMaker.getAngularSeparation(x, y) > 330
                ){
                    return false;
                }
            }
        }
        return true;
    }

    //查找并生成子节点，并返回队列对象
    public void generateChildrenNodes(SearchNode node, boolean[][] visitedGrid){
        if(!PointSimulator.isPoiontInsideMap(node.position.x, node.position.y)) return;
        visitedGrid[node.position.x][node.position.y] = true;
        for(int i=0; i < SearchNode.childrenNodesFindingCost.length; i++){
            int x = node.position.x + SearchNode.childrenNodesFindingCost[i][0] ;
            int y = node.position.y + SearchNode.childrenNodesFindingCost[i][1] ;
            double cost = Math.sqrt(SearchNode.childrenNodesFindingCost[i][2]);
            if(Systems.pointSimulator.isPointNotEmpty(x,y, strategyMaker.getPointStatus())){
                continue;
            }
            if(Enemy.getLockedEnemy().getPointPosition().distanceTo(x,y) < 50){
                if(node.position.distanceTo(Enemy.getLockedEnemy().getPointPosition()) > Enemy.getLockedEnemy().getPointPosition().distanceTo(x,y)){
                    continue;
                }
            }
            if(Enemy.getLockedEnemy().getPointPosition().distanceTo(x,y) > 7500){
                if(node.position.distanceTo(Enemy.getLockedEnemy().getPointPosition()) < Enemy.getLockedEnemy().getPointPosition().distanceTo(x,y)){
                    continue;
                }
            }
            if(Enemy.getLockedEnemy().getPointPosition().distanceTo(x,y) < 100){
                if(node.position.distanceTo(Enemy.getLockedEnemy().getPointPosition()) > Enemy.getLockedEnemy().getPointPosition().distanceTo(x,y)){
                    continue;
                }
            }
            if(this.strategyMaker.getFriendRoboMaster().getPointPosition().distanceTo(x,y) < 100){
                if(node.position.distanceTo(this.strategyMaker.getFriendRoboMaster().getPointPosition()) > this.strategyMaker.getFriendRoboMaster().getPointPosition().distanceTo(x,y)){
                    continue;
                }
            }
//            if(!isFitIntoThePosition(x, y)) continue;
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
        for(int i=0;i<45;i++){
            for(int j=0;j<45;j++){
                int m = x + i - 23;
                int n = y + j - 23;
                if(Systems.pointSimulator.isPointNotEmpty(m, n, strategyMaker.getPointStatus())){
                    return false;
                }
            }
        }
        return true;
    }
}
