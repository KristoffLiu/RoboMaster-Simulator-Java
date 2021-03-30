package com.kristoff.robomaster_simulator.robomasters.Strategy;

import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.Strategy.gradientdescent.GradientDescentAnalyzer;
import com.kristoff.robomaster_simulator.systems.costmap.PositionCost;
import com.kristoff.robomaster_simulator.systems.costmap.UniversalCostMap;
import com.kristoff.robomaster_simulator.systems.pointsimulator.PointSimulator;
import com.kristoff.robomaster_simulator.utils.Position;

import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

public class StrategyAnalyzer_2V2AStar extends UniversalAnalyzer {
    public StrategyAnalyzer_2V2AStar(StrategyMaker strategyMaker){
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

        boolean is_find = false;
        while (!this.queue.isEmpty()){
            resultNode = this.queue.poll();
            if(isAvailable(resultNode.position, target)) {
                is_find = true;
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

    public boolean isAvailable(Position centre, PositionCost target){
//        return isTheCentreAvailable(centrePosition)
//                && isTheSurroundingAreaAvailable(centrePosition);
        return Math.abs(getCostMap().getCost(centre.getX(), centre.getY()) - target.cost) < 30 ||
                centre.x == target.x && centre.y == target.y;
    }

    //查找并生成子节点，并返回队列对象
    public void generateChildrenNodes(SearchNode node, boolean[][] visitedGrid){
        if(!PointSimulator.isPointInsideMap(node.position.x, node.position.y)) return;
        visitedGrid[node.position.x][node.position.y] = true;
        for(int i=0; i < SearchNode.childrenNodesFindingCost.length; i++){
            int x = node.position.x + SearchNode.childrenNodesFindingCost[i][0] ;
            int y = node.position.y + SearchNode.childrenNodesFindingCost[i][1] ;
            double currentCost = getCostMap().getCost(node.position.x, node.position.y);
            double nextCost = getCostMap().getCost(x, y);
            double delta = nextCost - currentCost;
            double stepCost = Math.sqrt(SearchNode.childrenNodesFindingCost[i][2]);
            double totalCost = node.cost + delta + stepCost;
            if(hasThisNodeNotBeenVisited(x, y, visitedGrid) ){
                SearchNode childNode = new SearchNode(x,y,node.index + 1, totalCost,node);
                if(nextCost < 400){
                    node.childrenNodes.add(childNode);
                    queue.offer(childNode);
                }
            }
        }
    }
}
