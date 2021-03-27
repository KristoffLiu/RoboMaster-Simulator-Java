package com.kristoff.robomaster_simulator.robomasters.Strategy;

import com.kristoff.robomaster_simulator.systems.costmap.CostMapGenerator;
import com.kristoff.robomaster_simulator.systems.pointsimulator.PointSimulator;
import com.kristoff.robomaster_simulator.utils.Position;

import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

public class StrategyAnalyzer_2V2GradientDecening extends UniversalAnalyzer {
    public StrategyMaker strategyMaker;

    public SearchNode rootNode;
    public Queue<SearchNode>                      queue;
    public SearchNode resultNode;
    public CopyOnWriteArrayList<SearchNode>                   resultNodes;
    public CopyOnWriteArrayList<SearchNode>                   pathNodes;
    public int maxIteration;

    Position destination = new Position();

    public StrategyAnalyzer_2V2GradientDecening(StrategyMaker strategyMaker){
        this.strategyMaker = strategyMaker;

        this.queue        = this.strategyMaker.queue;
        this.resultNodes  = new CopyOnWriteArrayList<>();
        this.pathNodes    = new CopyOnWriteArrayList<>();
        this.maxIteration = 500;
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
        boolean[][] tempVisitedGrid = new boolean[849][489];

        this.rootNode = new SearchNode(
                currentPosition.x,
                currentPosition.y,
                -1,
                CostMapGenerator.getCost(currentPosition.x, currentPosition.y),
                null);
        this.resultNode = rootNode;
        queue.offer(rootNode);
        tempVisitedGrid[rootNode.position.x][rootNode.position.y] = true;

        boolean is_find = false;
        while (!this.queue.isEmpty()){
            resultNode = this.queue.poll();
            if(isAvailable(resultNode.position)) {
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

    public boolean isAvailable(Position centrePosition){
        return isTheCentreAvailable(centrePosition)
                && isTheSurroundingAreaAvailable(centrePosition);
    }

    //查找并生成子节点，并返回队列对象
    public void generateChildrenNodes(SearchNode node, boolean[][] visitedGrid){
        if(!PointSimulator.isPointInsideMap(node.position.x, node.position.y)) return;
        visitedGrid[node.position.x][node.position.y] = true;
        for(int i=0; i < SearchNode.childrenNodesFindingCost.length; i++){
            int x = node.position.x + SearchNode.childrenNodesFindingCost[i][0] ;
            int y = node.position.y + SearchNode.childrenNodesFindingCost[i][1] ;
            double cost = CostMapGenerator.getCost(x, y);
            double stepCost = Math.sqrt(SearchNode.childrenNodesFindingCost[i][2]);
            if(hasThisNodeNotBeenVisited(x, y, visitedGrid) ){
                SearchNode childNode = new SearchNode(x,y,node.index + 1, cost,node);
                if(childNode.cost <= node.cost && childNode.cost < 400){
                    node.childrenNodes.add(childNode);
                    queue.offer(childNode);
                }
            }
        }
    }
}
