package com.kristoff.robomaster_simulator.robomasters.robomaster.tactics;

import com.badlogic.gdx.Gdx;
import com.kristoff.robomaster_simulator.robomasters.robomaster.modules.TacticMaker;
import com.kristoff.robomaster_simulator.robomasters.teams.Team;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.utils.Position;

import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

/***
 * Based On RRT Algorithm 基于快速随机扩展树算法
 */
public class TwoVSTwoPPTatic implements Tactic{
    public TacticMaker tacticMaker;

    public int[][]                                          enemiesObservationGrid;

    public SearchNode rootNode;
    public Queue<SearchNode> queue;
    public SearchNode resultNode;
    public CopyOnWriteArrayList<SearchNode>                 resultNodes;
    public CopyOnWriteArrayList<SearchNode>                 pathNodes;

    Position destination = new Position();

    public TwoVSTwoPPTatic(TacticMaker tacticMaker){
        this.tacticMaker = tacticMaker;

        this.enemiesObservationGrid     = this.tacticMaker.enemiesObservationGrid;
        this.queue                      = this.tacticMaker.queue;
        this.resultNodes                = this.tacticMaker.resultNodes;
        this.pathNodes = this.tacticMaker.pathNodes;
    }

    @Override
    public void decide() {
        synchronized(enemiesObservationGrid){
            long startTime = System.currentTimeMillis();//开始时间
            Position currentPosition = tacticMaker.getCurrentPosition();
            if(!Team.me().enemiesObservationSimulator.isInBothEnemiesView(currentPosition.x,currentPosition.y)){
                return;
            }

            this.rootNode = new SearchNode(
                    currentPosition.x,
                    currentPosition.y,
                    -1,
                    0,
                    null);
            resultNode = rootNode;
            queue.offer(rootNode);
            this.tacticMaker.getNodeGrid()[rootNode.position.x][rootNode.position.y] = true;

            int count = 0;
            int limit = 10;
            while (!this.queue.isEmpty() && resultNodes.size() < 5){
                resultNode = this.queue.poll();

                if(!tacticMaker.isInBothEnemiesView(resultNode.position.x, resultNode.position.y)){
                    count ++;
                }
                if(count > limit){
                    if(isAvailable()) {
                        resultNodes.add(resultNode);
                        count = 0;
                    }
                }
                if(resultNodes.size() < 4) generateChildrenNodes(resultNode);
            }
            pathNodes.clear();
            for(SearchNode node : resultNodes){
                while (true && node.parentNode != null){
                    pathNodes.add(node);
                    node = node.parentNode;
                }
            }
            long endtime = System.currentTimeMillis();//开始时间
            System.out.println(endtime - startTime);
        }
    }

    public boolean isAvailable(){
        for(int i=0;i<70;i++){
            for(int j=0;j<70;j++){
                int x = resultNode.position.x + i - 35;
                int y = resultNode.position.y + j - 35;
                if(   !(x>=20 && x<829)
                        || !(y>=20 && y<469)
                        || tacticMaker.isInBothEnemiesView(x,y)
                        || Systems.pointSimulator.isPointNotEmpty(x,y,tacticMaker.getPointStatus())
                        || !isFarAwayFromExistingResults(x, y)){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isFarAwayFromExistingResults(int x, int y){
        if(resultNodes.size() == 0) return true;
        for(SearchNode result : resultNodes){
            if(result.position.distanceTo(x, y) < 60) return false;
        }
        return true;
    }

    //查找并生成子节点，并返回队列对象
    public void generateChildrenNodes(SearchNode node){
        this.tacticMaker.getNodeGrid()[node.position.x][node.position.y] = true;
        for(int i=0; i < SearchNode.childrenNodesFindingCost.length; i++){
            int x = node.position.x + SearchNode.childrenNodesFindingCost[i][0] ;
            int y = node.position.y + SearchNode.childrenNodesFindingCost[i][1] ;
            if(    !(x>=20 && x<829)
                || !(y>=20 && y<469)){
                return;
            }
            double cost = Math.sqrt(SearchNode.childrenNodesFindingCost[i][2]);
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
