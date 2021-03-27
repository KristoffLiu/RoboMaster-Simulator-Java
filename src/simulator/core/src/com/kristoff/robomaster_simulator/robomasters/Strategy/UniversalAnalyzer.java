package com.kristoff.robomaster_simulator.robomasters.Strategy;

import com.kristoff.robomaster_simulator.robomasters.modules.Property;
import com.kristoff.robomaster_simulator.systems.costmap.CostMapGenerator;
import com.kristoff.robomaster_simulator.utils.Position;

public class UniversalAnalyzer implements StrategyAnalyzer {
    public boolean isTheCentreAvailable(Position centrePosition){
        return CostMapGenerator.getCost(centrePosition.x, centrePosition.y) < 50;
    }

    public boolean isTheCentreAvailable(Position centrePosition, Position friendDecisionPosition){
        return CostMapGenerator.getCostConsideringFriendPosition(centrePosition, friendDecisionPosition) < 50;
    }

    public boolean isTheSurroundingAreaAvailable(Position centrePosition){
        for(int i = 0; i < Property.widthUnit ; i++){
            for(int j = 0; j < Property.heightUnit ; j++){
                int x = centrePosition.x + i - Property.widthUnit / 2;
                int y = centrePosition.y + j - Property.heightUnit / 2;
                int centreCost = CostMapGenerator.getCost(centrePosition.x, centrePosition.y);
                if(   !(x>=0 && x<849)
                        || !(y>=0 && y<489)
                        || CostMapGenerator.getCost(x, y) > 150
//                        || Math.abs(CostMapGenerator.getCost(x, y) - centreCost) > 10
                ){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isTheSurroundingAreaAvailable(Position centrePosition, Position friendDecisionPosition){
        for(int i = 0; i < Property.widthUnit ; i++){
            for(int j = 0; j < Property.heightUnit ; j++){
                int x = centrePosition.x + i - Property.widthUnit / 2;
                int y = centrePosition.y + j - Property.heightUnit / 2;
                if(   !(x>=0 && x<849)
                        || !(y>=0 && y<489)
                        || CostMapGenerator.getCostConsideringFriendPosition(new Position(x, y), friendDecisionPosition)
                        > 150
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

    @Override
    public void analyze(TacticState tacticState) {

    }
}
