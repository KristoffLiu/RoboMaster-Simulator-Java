package com.kristoff.robomaster_simulator.robomasters.Strategy;

import com.kristoff.robomaster_simulator.robomasters.modules.Property;
import com.kristoff.robomaster_simulator.systems.costmap.CostMapGenerator;
import com.kristoff.robomaster_simulator.utils.Position;

public class UniversalAnalyzer {
    public boolean isAvailable(Position centrePosition){
        return isTheSurroundingAreaAvailable(centrePosition);
    }

    public boolean isTheSurroundingAreaAvailable(Position centrePosition, Position friendDecisionPosition){
        for(int i = 0; i < Property.widthUnit ; i++){
            for(int j = 0; j < Property.heightUnit ; j++){
                int x = centrePosition.x + i - Property.widthUnit / 2;
                int y = centrePosition.y + j - Property.heightUnit / 2;
                if(   !(x>=0 && x<849)
                        || !(y>=0 && y<489)
                        || CostMapGenerator.getCostConsideringFriendPosition(new Position(x, y), friendDecisionPosition)
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
