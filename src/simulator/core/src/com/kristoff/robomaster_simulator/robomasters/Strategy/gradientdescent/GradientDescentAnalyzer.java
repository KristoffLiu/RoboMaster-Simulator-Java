package com.kristoff.robomaster_simulator.robomasters.Strategy.gradientdescent;

import com.kristoff.robomaster_simulator.systems.costmap.UniversalCostMap;
import com.kristoff.robomaster_simulator.utils.Position;

import java.util.LinkedList;
import java.util.List;

public class GradientDescentAnalyzer {
    public DescentStep startStep;
    public int maxIteration = 200;
    public int stepSize = 5;
    public boolean isRanger;
    public List<DescentStep> steps;
    public DescentStep minStep;


    public GradientDescentAnalyzer(Position startPosition,
                                   int maxIteration,
                                   int stepSize,
                                   boolean isRanger){
        this.isRanger = isRanger;
        //this.startStep = new DescentStep(getCost(startPosition), startPosition);
        this.maxIteration = maxIteration;
        this.stepSize = stepSize;
        steps = new LinkedList<>();
        this.steps.add(startStep);
    }

    public void analyze(){
//        int count = maxIteration;
//        int currentX = startStep.x;
//        int currentY = startStep.y;
//        int minimumFound = 999;
//        while (Math.abs(minimumFound - UniversalCostMap.getMinPositionCost().cost) > 20){
//            this.stepSize += 1;
//            this.steps.clear();
//            while(count > 0){
//                int previousX = currentX;
//                int previousY = currentY;
//                DescentStep nextStep = minNeighbours(previousX, previousY);
//                steps.add(nextStep);
//                count --;
//            }
//            if(steps.size() > 0){
//                if(minimumFound > steps.get(steps.size() - 1).value){
//                    minimumFound = steps.get(steps.size() - 1).value;
//                    minStep = steps.get(steps.size() - 1);
//                }
//            }
//        }

    }

//    public int getCost(Position position){
//        return getCost(position.x, position.y);
//    }
//
//    public int getCost(int x, int y){
//        return UniversalCostMap.getCost(x, y);
//    }
//
//    public DescentStep minNeighbours(int previousX, int previousY){
//        Position centre = new Position(previousX, previousY);
//        Position start = new Position(centre.x, centre.y + stepSize);
//        DescentStep minNeighbour = null;
//        for(int i = 0; i < 360; i ++){
//            double radian = Math.toRadians(i);
//            int x = (int)((start.x - centre.x) * Math.cos(radian) - (start.y - centre.y) * Math.sin(radian) + centre.x);
//            int y = (int)((start.x - centre.x) * Math.sin(radian) + (start.y - centre.y) * Math.cos(radian) + centre.y);
//            if(x > 0 && x < 849 && y > 0 && y < 489){
//                int cost = getCost(x, y);
//                if(minNeighbour == null){
//                    minNeighbour = new DescentStep(cost, x, y);
//                }
//                else if(getCost(x, y) < minNeighbour.value){
//                    minNeighbour = new DescentStep(cost, x, y);
//                }
//            }
//        }
//        return minNeighbour;
//    }
}
