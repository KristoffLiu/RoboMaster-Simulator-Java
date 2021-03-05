package com.kristoff.robomaster_simulator.robomasters.robomaster.tactics;

import com.badlogic.gdx.Gdx;
import com.kristoff.robomaster_simulator.robomasters.robomaster.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.robomaster.modules.TacticMaker;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.utils.Position;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

public class OneVSTwoPPTactic implements Tactic{
    public TacticMaker tacticMaker;

    public int[][]                                          enemiesObservationGrid;

    public boolean[][]                                      nodeGrid;
    public OneVSTwoPPTacticNode                             rootNode;
    public Queue<OneVSTwoPPTacticNode>                      queue;
    public OneVSTwoPPTacticNode                             resultNode;
    public CopyOnWriteArrayList<Position>                   results;

    Position destination = new Position();

    public OneVSTwoPPTactic(TacticMaker tacticMaker){
        this.tacticMaker = tacticMaker;

        this.enemiesObservationGrid     = this.tacticMaker.getEnemiesObservationGrid();
        this.nodeGrid                   = new boolean[849][489];
        this.queue                      = new LinkedList<>();
        this.results                    = new CopyOnWriteArrayList<>();
    }

    @Override
    public void decide() {
        getPointAvoidingFacingEnemies();
    }

    @Override
    public Position getDestination() {
        return null;
    }

    @Override
    public boolean[][] getNodeGrid() {
        return nodeGrid;
    }

    @Override
    public CopyOnWriteArrayList<Position> getResults() {
        return results;
    }

    //此节点的子节点查找路径及成本消耗
    public static int[][] childrenNodesFindingCost =
        new int[][]
            {
                { 1, 0, 1},{0, 1, 1},{-1, 0, 1},
                { 0, -1, 1},          {-1, -1, 2},
                {-1, 1, 2},{1, -1, 2},{1, 1, 2}
            };

    public void getPointToTheSafeZone(){
        synchronized(enemiesObservationGrid){
            long startTime = System.currentTimeMillis();//开始时间

            nodeGrid = new boolean[849][489];
            queue.clear();
            Position currentPosition = tacticMaker.getCurrentPosition();
            this.rootNode = new OneVSTwoPPTacticNode(
                    currentPosition.x,
                    currentPosition.y,
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
                                    || Systems.pointSimulator.isPointNotEmpty(x,y,tacticMaker.getPointStatus()))){
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
            OneVSTwoPPTacticNode node = resultNode;
            results.clear();
            while (true && node.parentNode != null){
                results.add(node.position);
                node = node.parentNode;
            }

            long endTime = System.currentTimeMillis();//开始时间
            Gdx.app.log("", String.valueOf(endTime - startTime));
            destination = resultNode.position;
        }
    }


    public void getPointAvoidingFacingEnemies(){
        synchronized(enemiesObservationGrid){
            long startTime = System.currentTimeMillis();//开始时间

            nodeGrid = new boolean[849][489];
            queue.clear();
            Position currentPosition = tacticMaker.getCurrentPosition();
            this.rootNode = new OneVSTwoPPTacticNode(
                    currentPosition.x,
                    currentPosition.y,
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
                                    || Systems.pointSimulator.isPointNotEmpty(x,y,tacticMaker.getPointStatus()))){
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
            OneVSTwoPPTacticNode node = resultNode;
            results.clear();
            while (true && node.parentNode != null){
                results.add(node.position);
                node = node.parentNode;
            }

            destination = resultNode.position;
        }
    }

    //查找并生成子节点，并返回队列对象
    public void generateChildrenNodes(OneVSTwoPPTacticNode node){
        nodeGrid[node.position.x][node.position.y] = true;
        for(int i=0; i < childrenNodesFindingCost.length; i++){
            int x = node.position.x + childrenNodesFindingCost[i][0] ;
            int y = node.position.y + childrenNodesFindingCost[i][1] ;
            double cost = Math.sqrt(childrenNodesFindingCost[i][2]);
            if(hasThisNodeNotBeenVisited(x, y, nodeGrid) && (!Systems.pointSimulator.isPointNotEmpty(x,y,tacticMaker.getPointStatus()))){
                OneVSTwoPPTacticNode childNode = new OneVSTwoPPTacticNode(x,y,node.index + 1, cost,node);
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
