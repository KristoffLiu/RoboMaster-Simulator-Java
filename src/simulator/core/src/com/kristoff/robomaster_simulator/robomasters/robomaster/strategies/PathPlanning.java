package com.kristoff.robomaster_simulator.robomasters.robomaster.strategies;

import com.badlogic.gdx.Gdx;
import com.kristoff.robomaster_simulator.robomasters.robomaster.RoboMaster;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.utils.Position;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

public class PathPlanning {
    public RoboMaster thisRoboMaster;
    public int[][]                                          enemiesObservationGrid;
    public boolean[][]                                      nodeGrid;
    public PathPlanningNode rootNode;
    public Queue<PathPlanningNode>     queue;
    public PathPlanningNode resultNode;
    public CopyOnWriteArrayList<Position>                   results;

    public PathPlanning(int[][] enemiesObservationGrid, RoboMaster roboMaster){
        this.thisRoboMaster = roboMaster;
        this.enemiesObservationGrid = enemiesObservationGrid;
        this.nodeGrid = new boolean[849][489];
        this.queue = new LinkedList<>();
        this.results = new CopyOnWriteArrayList<>();
    }

    //此节点的子节点查找路径及成本消耗
    public static int[][] childrenNodesFindingCost =
            new int[][]
                    {
                            { 1, 0, 1},{0, 1, 1},{-1, 0, 1},
                            { 0, -1,1},{-1,-1,2},
                            {-1, 1, 2},{1,-1, 2},{1, 1, 2}
                    };

    public Position getPointToTheSafeZone(){
        synchronized(enemiesObservationGrid){
            long startTime = System.currentTimeMillis();//开始时间

            nodeGrid = new boolean[849][489];
            queue.clear();
            this.rootNode = new PathPlanningNode(
                    thisRoboMaster.actor.x / 10,
                    thisRoboMaster.actor.y / 10,
                    -1,
                    0,
                    null);
            resultNode = rootNode;
            queue.offer(rootNode);
            nodeGrid[rootNode.position.x][rootNode.position.y] = true;

            int count = 0;
            while (!this.queue.isEmpty()){
                resultNode = this.queue.poll();
                if(enemiesObservationGrid[resultNode.position.x][resultNode.position.y] == 0){
                    count ++;
                }
                if(count > 10){
                    boolean canStop = true;
                    for(int i=0;i<60;i++){
                        for(int j=0;j<45;j++){
                            int x = resultNode.position.x + i - 30;
                            int y = resultNode.position.y + j - 23;
                            if(        x>=0 && x<849
                                    && y>=0 && y<489
                                    && (enemiesObservationGrid[x][y] != 0
                                    || Systems.pointSimulator.isPointNotEmpty(x,y,thisRoboMaster.pointStatus))){
                                canStop = false;
                                break;
                            }
                        }
                        if (!canStop){
                            break;
                        }
                    }
                    if(canStop) {
                        break;
                    }
                }
                generateChildrenNodes(resultNode);
            }
            PathPlanningNode node = resultNode;
            results.clear();
            while (true && node.parentNode != null){
                results.add(node.position);
                node = node.parentNode;
            }

            long endTime = System.currentTimeMillis();//开始时间
            Gdx.app.log("", String.valueOf(endTime - startTime));
            return resultNode.position;
        }
    }


    public Position getPointAvoidingFacingEnemies(){
        synchronized(enemiesObservationGrid){
            long startTime = System.currentTimeMillis();//开始时间

            nodeGrid = new boolean[849][489];
            queue.clear();
            this.rootNode = new PathPlanningNode(
                    thisRoboMaster.actor.x / 10,
                    thisRoboMaster.actor.y / 10,
                    -1,
                    0,
                    null);
            resultNode = rootNode;
            queue.offer(rootNode);
            nodeGrid[rootNode.position.x][rootNode.position.y] = true;

            int count = 0;
            while (!this.queue.isEmpty()){
                resultNode = this.queue.poll();
                if(enemiesObservationGrid[resultNode.position.x][resultNode.position.y] != 3){
                    count ++;
                }
                if(count > 10){
                    boolean canStop = true;
                    for(int i=0;i<60;i++){
                        for(int j=0;j<45;j++){
                            int x = resultNode.position.x + i - 30;
                            int y = resultNode.position.y + j - 23;
                            if(        x>=0 && x<849
                                    && y>=0 && y<489
                                    && (enemiesObservationGrid[x][y] == 3
                                    || Systems.pointSimulator.isPointNotEmpty(x,y,thisRoboMaster.pointStatus))){
                                canStop = false;
                                break;
                            }
                        }
                        if (!canStop){
                            break;
                        }
                    }
                    if(canStop) {
                        break;
                    }
                }
                generateChildrenNodes(resultNode);
            }
            PathPlanningNode node = resultNode;
            results.clear();
            while (true && node.parentNode != null){
                results.add(node.position);
                node = node.parentNode;
            }

            long endTime = System.currentTimeMillis();//开始时间
            Gdx.app.log("", String.valueOf(endTime - startTime));
            return resultNode.position;
        }
    }

    //查找并生成子节点，并返回队列对象
    public void generateChildrenNodes(PathPlanningNode node){
        nodeGrid[node.position.x][node.position.y] = true;
        for(int i=0; i < childrenNodesFindingCost.length; i++){
            int x = node.position.x + childrenNodesFindingCost[i][0] ;
            int y = node.position.y + childrenNodesFindingCost[i][1] ;
            double cost = Math.sqrt(childrenNodesFindingCost[i][2]);
            if(hasThisNodeNotBeenVisited(x, y, nodeGrid) && (!Systems.pointSimulator.isPointNotEmpty(x,y,this.thisRoboMaster.pointStatus))){
                PathPlanningNode childNode = new PathPlanningNode(x,y,node.index + 1, cost,node);
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
