package com.kristoff.robomaster_simulator.robomasters.robomaster.tactics;

import com.kristoff.robomaster_simulator.robomasters.robomaster.modules.TacticMaker;
import com.kristoff.robomaster_simulator.robomasters.teams.Team;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.pointsimulator.PointSimulator;
import com.kristoff.robomaster_simulator.utils.Position;

import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

public class TwoVSTwoPPTactic implements Tactic{
    public TacticMaker tacticMaker;

    public int[][]                                          enemiesObservationGrid;

    public SearchNode rootNode;
    public Queue<SearchNode>                      queue;
    public SearchNode resultNode;
    public CopyOnWriteArrayList<SearchNode>                   resultNodes;
    public CopyOnWriteArrayList<SearchNode>                   pathNodes;

    Position destination = new Position();

    public TwoVSTwoPPTactic(TacticMaker tacticMaker){
        this.tacticMaker = tacticMaker;

        this.enemiesObservationGrid     = this.tacticMaker.enemiesObservationGrid;
        this.queue                      = this.tacticMaker.queue;
        this.resultNodes                = this.tacticMaker.resultNodes;
        this.pathNodes                  = this.tacticMaker.pathNodes;
    }

    @Override
    public void decide() {
        synchronized(enemiesObservationGrid){
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
    }

    public void AvoidOneVSTwoTactic(Position currentPosition){
        this.rootNode = new SearchNode(
                currentPosition.x,
                currentPosition.y,
                -1,
                0,
                null);
        this.resultNode = rootNode;
        queue.offer(rootNode);
        this.tacticMaker.getNodeGrid()[rootNode.position.x][rootNode.position.y] = true;

        int count = 0;
        while (!this.queue.isEmpty()){
            resultNode = this.queue.poll();
            if(!this.tacticMaker.isInBothEnemiesView(resultNode.position.x, resultNode.position.y)){
                count ++;
            }
            if(count > 2){
                if(isAvailable()) {
                    break;
                }
            }
            generateChildrenNodes(resultNode);
        }
        SearchNode node = resultNode;
        pathNodes.clear();
        while (true && node.parentNode != null){
            pathNodes.add(node);
            node = node.parentNode;
        }

        this.tacticMaker.setDecisionNode(resultNode);
    }

    public boolean isAvailable(){
        for(int i=0;i<45;i++){
            for(int j=0;j<45;j++){
                int x = resultNode.position.x + i - 23;
                int y = resultNode.position.y + j - 23;
                if(   !(x>=0 && x<849)
                        || !(y>=0 && y<489)
                        || Team.me().enemiesObservationSimulator.isInBothEnemiesView(x,y)
                        || !this.tacticMaker.isOnTargetedEnemyView(x,y)
                        || Systems.pointSimulator.isPointNotEmpty(x,y,tacticMaker.getPointStatus())
                        || this.tacticMaker.getUnTargeted().getPointPosition().distanceTo(x,y) < 100
                        || this.tacticMaker.getTargeted().getPointPosition().distanceTo(x,y) < 50
                        || this.tacticMaker.getTargeted().getPointPosition().distanceTo(x,y) > 300
                ){
                    return false;
                }
            }
        }
        return true;
    }

    //查找并生成子节点，并返回队列对象
    public void generateChildrenNodes(SearchNode node){
        if(!PointSimulator.isPoiontInsideMap(node.position.x, node.position.y)) return;
        this.tacticMaker.getNodeGrid()[node.position.x][node.position.y] = true;
        for(int i=0; i < SearchNode.childrenNodesFindingCost.length; i++){
            int x = node.position.x + SearchNode.childrenNodesFindingCost[i][0] ;
            int y = node.position.y + SearchNode.childrenNodesFindingCost[i][1] ;
            double cost = Math.sqrt(SearchNode.childrenNodesFindingCost[i][2]);
            if(this.tacticMaker.getTargeted().getPointPosition().distanceTo(x,y) < 50){
                if(node.position.distanceTo(this.tacticMaker.getTargeted().getPointPosition()) > this.tacticMaker.getTargeted().getPointPosition().distanceTo(x,y)){
                    continue;
                }
            }
            if(this.tacticMaker.getTargeted().getPointPosition().distanceTo(x,y) > 200){
                if(node.position.distanceTo(this.tacticMaker.getTargeted().getPointPosition()) < this.tacticMaker.getTargeted().getPointPosition().distanceTo(x,y)){
                    continue;
                }
            }
            if(this.tacticMaker.getUnTargeted().getPointPosition().distanceTo(x,y) < 100){
                if(node.position.distanceTo(this.tacticMaker.getUnTargeted().getPointPosition()) > this.tacticMaker.getUnTargeted().getPointPosition().distanceTo(x,y)){
                    continue;
                }
            }
            if(hasThisNodeNotBeenVisited(x, y, this.tacticMaker.getNodeGrid()) && (!Systems.pointSimulator.isPointNotEmpty(x,y,tacticMaker.getPointStatus()))){
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
