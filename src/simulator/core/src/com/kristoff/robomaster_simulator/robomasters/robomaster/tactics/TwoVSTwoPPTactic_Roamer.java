package com.kristoff.robomaster_simulator.robomasters.robomaster.tactics;

import com.kristoff.robomaster_simulator.robomasters.robomaster.modules.TacticMaker;
import com.kristoff.robomaster_simulator.robomasters.teams.enemyobservations.EnemiesObservationSimulator;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.pointsimulator.PointSimulator;
import com.kristoff.robomaster_simulator.utils.Position;

import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

public class TwoVSTwoPPTactic_Roamer implements Tactic{
    public TacticMaker tacticMaker;

    public SearchNode rootNode;
    public Queue<SearchNode>                      queue;
    public SearchNode resultNode;
    public CopyOnWriteArrayList<SearchNode>                   resultNodes;
    public CopyOnWriteArrayList<SearchNode>                   pathNodes;

    Position destination = new Position();

    public TwoVSTwoPPTactic_Roamer(TacticMaker tacticMaker){
        this.tacticMaker = tacticMaker;

        this.queue                      = this.tacticMaker.queue;
        this.resultNodes                = new CopyOnWriteArrayList<>();
        this.pathNodes                  = new CopyOnWriteArrayList<>();
    }

    @Override
    public void decide() {
        long startTime = System.currentTimeMillis();//开始时间
        Position currentPosition = tacticMaker.getCurrentPosition();
        if(!tacticMaker.isOnTargetedEnemyView(currentPosition.x,currentPosition.y)){
            AvoidOneVSTwoTactic(currentPosition);
        }
        else {
            pathNodes.clear();
            return;
        }
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

        this.tacticMaker.update(resultNode, visitedGrid, resultNodes, pathNodes);

    }

    public boolean isAvailable(){
        for(int i=0;i<45;i++){
            for(int j=0;j<45;j++){
                int x = resultNode.position.x + i - 23;
                int y = resultNode.position.y + j - 23;
                if(   !(x>=0 && x<849)
                        || !(y>=0 && y<489)
                        || EnemiesObservationSimulator.isInBothEnemiesView(x,y)
                        || !this.tacticMaker.isOnTargetedEnemyView(x,y)
                        || Systems.pointSimulator.isPointNotEmpty(x,y, tacticMaker.getPointStatus())
                        || this.tacticMaker.getLockedEnemy().getPointPosition().distanceTo(x,y) < 50
                        || this.tacticMaker.getLockedEnemy().getPointPosition().distanceTo(x,y) > 500
                        || this.tacticMaker.getFriendDecision().position.distanceTo(x,y) < 100
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
            if(this.tacticMaker.getLockedEnemy().getPointPosition().distanceTo(x,y) < 50){
                if(node.position.distanceTo(this.tacticMaker.getLockedEnemy().getPointPosition()) > this.tacticMaker.getLockedEnemy().getPointPosition().distanceTo(x,y)){
                    continue;
                }
            }
            if(this.tacticMaker.getLockedEnemy().getPointPosition().distanceTo(x,y) > 200){
                if(node.position.distanceTo(this.tacticMaker.getLockedEnemy().getPointPosition()) < this.tacticMaker.getLockedEnemy().getPointPosition().distanceTo(x,y)){
                    continue;
                }
            }
            if(this.tacticMaker.getUnlockedEnemy().getPointPosition().distanceTo(x,y) < 100){
                if(node.position.distanceTo(this.tacticMaker.getUnlockedEnemy().getPointPosition()) > this.tacticMaker.getUnlockedEnemy().getPointPosition().distanceTo(x,y)){
                    continue;
                }
            }
            if(this.tacticMaker.getFriendRoboMaster().getPointPosition().distanceTo(x,y) < 200){
                if(node.position.distanceTo(this.tacticMaker.getFriendRoboMaster().getPointPosition()) > this.tacticMaker.getFriendRoboMaster().getPointPosition().distanceTo(x,y)){
                    continue;
                }
            }
            if(hasThisNodeNotBeenVisited(x, y, visitedGrid) && (!Systems.pointSimulator.isPointNotEmpty(x,y, tacticMaker.getPointStatus()))){
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
}
