package com.kristoff.robomaster_simulator.robomasters.strategies;

import com.badlogic.gdx.Gdx;
import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.utils.LoopThread;
import com.kristoff.robomaster_simulator.utils.Position;
import com.kristoff.robomaster_simulator.utils.TriggeredThread;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

public class OneVsTwoCircumventionPathPlanning {
    public RoboMaster thisRoboMaster;
    public int[][]                                          enemiesObservationGrid;
    public boolean[][]                                      nodeGrid;
    public OneVsTwoCircumventionPathPlanningNode            rootNode;
    public Queue<OneVsTwoCircumventionPathPlanningNode>     queue;
    public OneVsTwoCircumventionPathPlanningNode            resultNode;
    public CopyOnWriteArrayList<Position>                   results;

    public OneVsTwoCircumventionPathPlanning(int[][] enemiesObservationGrid, RoboMaster roboMaster){
        this.thisRoboMaster = roboMaster;
        this.enemiesObservationGrid = enemiesObservationGrid;
        this.nodeGrid = new boolean[849][489];
        this.queue = new LinkedList<>();
        this.results = new CopyOnWriteArrayList<>();
    }

    public void update(){
        synchronized(enemiesObservationGrid){
            long startTime = System.currentTimeMillis();//开始时间

            nodeGrid = new boolean[849][489];
            queue.clear();
            this.rootNode = new OneVsTwoCircumventionPathPlanningNode(
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
                                    || Systems.matrixSimulator.isPointNotEmptyLowResolution(x,y,thisRoboMaster.pointStatus))){
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
                resultNode.generateChildrenNodes(
                        this.enemiesObservationGrid,
                        this.nodeGrid,
                        this.thisRoboMaster)
                        .forEach(x->queue.offer(x));
            }
            OneVsTwoCircumventionPathPlanningNode node = resultNode;
            results.clear();
            while (true && node.parentNode != null){
                results.add(node.position);
                node = node.parentNode;
            }

            long endTime = System.currentTimeMillis();//开始时间
            Gdx.app.log("", String.valueOf(endTime - startTime));
        }
    }
}
