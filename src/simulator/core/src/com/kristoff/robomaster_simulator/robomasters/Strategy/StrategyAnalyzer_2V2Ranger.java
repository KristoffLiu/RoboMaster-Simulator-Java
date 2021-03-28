package com.kristoff.robomaster_simulator.robomasters.Strategy;

import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.modules.Property;
import com.kristoff.robomaster_simulator.systems.costmap.PositionCost;
import com.kristoff.robomaster_simulator.systems.costmap.UniversalCostMap;
import com.kristoff.robomaster_simulator.systems.pointsimulator.PointSimulator;
import com.kristoff.robomaster_simulator.utils.Position;

import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

public class StrategyAnalyzer_2V2Ranger extends UniversalAnalyzer {
    public Position friendDecisionPosition;


    public StrategyAnalyzer_2V2Ranger(StrategyMaker strategyMaker){
        super(strategyMaker);
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
        //boolean[][] tempVisitedGrid = new boolean[849][489];
        boolean[][] tempVisitedGrid = strategyMaker.visitedGrid;

        friendDecisionPosition = strategyMaker.getFriendDecision().position;
        this.rootNode = new SearchNode(
                currentPosition.x,
                currentPosition.y,
                -1,
                getCostMap().getCost(currentPosition.x, currentPosition.y),
                null);
        this.resultNode = rootNode;
        PositionCost target = getCostMap().minPositionCost;

        queue.offer(rootNode);
        tempVisitedGrid[rootNode.position.x][rootNode.position.y] = true;

        while (!this.queue.isEmpty()){
            resultNode = this.queue.poll();
            if(isAvailable(resultNode.position, target)) break;
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

    //查找并生成子节点，并返回队列对象
    public void generateChildrenNodes(SearchNode node, boolean[][] visitedGrid){
        if(!PointSimulator.isPointInsideMap(node.position.x, node.position.y)) return;
        visitedGrid[node.position.x][node.position.y] = true;
        for(int i=0; i < SearchNode.childrenNodesFindingCost.length; i++){
            int x = node.position.x + SearchNode.childrenNodesFindingCost[i][0] ;
            int y = node.position.y + SearchNode.childrenNodesFindingCost[i][1] ;
            double currentCost = getCostMap().getCost(node.position.getX(), node.position.getY());
            double nextCost = getCostMap().getCost(x, y);
            double delta = nextCost - currentCost;
            double stepCost = Math.sqrt(SearchNode.childrenNodesFindingCost[i][2]);
            double totalCost = node.cost + delta + stepCost;
            if(hasThisNodeNotBeenVisited(x, y, visitedGrid) ){
                SearchNode childNode = new SearchNode(
                        x,
                        y,
                        node.index + 1,
                        totalCost,
                        node);
                if(nextCost < 400){
                    node.childrenNodes.add(childNode);
                    queue.offer(childNode);
                }
            }
        }
    }

    public boolean isAvailable(Position centre, PositionCost target){
//        return isTheCentreAvailable(centrePosition, friendDecisionPosition)
//                && isTheSurroundingAreaAvailable(centrePosition, friendDecisionPosition);
        return Math.abs(getCostMap().getCost(centre.getX(), centre.getY()) - target.cost) < 30 ||
                centre.x == target.x && centre.y == target.y;
    }

    public boolean isTheSurroundingAreaAvailable(Position centrePosition){
        for(int i = 0; i < Property.widthUnit ; i++){
            for(int j = 0; j < Property.heightUnit ; j++){
                int x = centrePosition.x + i - Property.widthUnit / 2;
                int y = centrePosition.y + j - Property.heightUnit / 2;
                if(   !(x>=0 && x<849)
                        || !(y>=0 && y<489)
                        || getCostMap().getCost(x, y)
                            > 200
                        //|| strategyMaker.getFriendRoboMaster().getPointPosition().distanceTo(x, y) < 50
                ){
                    return false;
                }
            }
        }
        return true;
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
