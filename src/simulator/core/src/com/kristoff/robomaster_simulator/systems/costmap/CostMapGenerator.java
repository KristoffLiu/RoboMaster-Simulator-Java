package com.kristoff.robomaster_simulator.systems;

import com.kristoff.robomaster_simulator.robomasters.Strategy.SearchNode;
import com.kristoff.robomaster_simulator.robomasters.Strategy.StrategyAnalyzer;
import com.kristoff.robomaster_simulator.robomasters.Strategy.StrategyMaker;
import com.kristoff.robomaster_simulator.robomasters.Strategy.TacticState;
import com.kristoff.robomaster_simulator.robomasters.types.Enemy;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.buffs.Buff;
import com.kristoff.robomaster_simulator.systems.buffs.BuffZone;
import com.kristoff.robomaster_simulator.systems.pointsimulator.PointSimulator;
import com.kristoff.robomaster_simulator.teams.enemyobservations.EnemiesObservationSimulator;
import com.kristoff.robomaster_simulator.utils.LoopThread;
import com.kristoff.robomaster_simulator.utils.Position;

import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

public class CostMapGenerator extends LoopThread {
    public StrategyMaker strategyMaker;

    public SearchNode rootNode;
    public Queue<SearchNode>                      queue;
    public SearchNode resultNode;
    public CopyOnWriteArrayList<SearchNode>                   resultNodes;
    public CopyOnWriteArrayList<SearchNode>                   pathNodes;
    public int[][] costmap;

    Position destination = new Position();

    public CostMapGenerator(StrategyMaker strategyMaker){
        this.strategyMaker = strategyMaker;

        this.queue                      = this.strategyMaker.queue;
        this.resultNodes                = new CopyOnWriteArrayList<>();
        this.pathNodes                  = new CopyOnWriteArrayList<>();
        this.costmap                    = new int[849][489];
    }

    @Override
    public void step(){
        generateCostMap();
    }

    @Override
    public void start(){
        super.start();
    }

    public void generateCostMap(){
        for(int i = 20; i < 829; i ++){
            for(int j = 20; j < 469; j ++){
                int cost = 0;
                if(Systems.pointSimulator.isPointTheObstacle(i, j)){
                    costmap[i][j] = Integer.MAX_VALUE;
                    continue;
                }
                cost += costOfEnemyObservation(i, j);
                cost += costOfEnemiesDistance(i, j);
                cost += costOfBuff(i, j);
                costmap[i][j] = cost;
            }
        }
    }

    public int costOfEnemyObservation(int x, int y){
        if(EnemiesObservationSimulator.isInLockedEnemyViewOnly(x, y))
            return 0;
        else if(EnemiesObservationSimulator.isInUnlockedEnemyViewOnly(x, y))
            return 10;
        else if(EnemiesObservationSimulator.isInBothEnemiesView(x, y))
            return 30;
        else
            return 20;
    }

    public int costOfEnemiesDistance(int x, int y){
        int safeDistance = 30;
        float deweight = 0.02f;
        float distanceToLockedEnemy = Enemy.getLockedEnemy().getPointPosition().distanceTo(x,y);
        float distanceToUnlockedEnemy = Enemy.getUnlockedEnemy().getPointPosition().distanceTo(x,y);
        return (int)((Math.abs(distanceToLockedEnemy - safeDistance) + Math.abs(distanceToUnlockedEnemy - safeDistance)) * deweight);
    }

    public int costOfBuff(int x, int y){
        return BuffZone.costOfBuff(x, y);
    }

    public int costOfObstacle(int x, int y){
        return Systems.pointSimulator.isPointTheObstacle(x, y) ? 500 : 0;
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
                !(Enemy.getLockedEnemy().getPointPosition().distanceTo(centrePosition) < 45 ||
                Enemy.getLockedEnemy().getPointPosition().distanceTo(centrePosition) > 600) ;
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
        if(!PointSimulator.isPoiontInsideMap(node.position.x, node.position.y)) return;
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
        for(int i=0;i<50;i+=10){
            for(int j=0;j<50;j+=10){
                int m = x + i - 25;
                int n = y + j - 25;
                if(Systems.pointSimulator.isPointNotEmpty(m, n, strategyMaker.getPointStatus())){
                    return false;
                }
            }
        }
        return true;
    }
}
