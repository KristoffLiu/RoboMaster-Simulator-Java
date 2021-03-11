package com.kristoff.robomaster_simulator.robomasters.teams.friendobservations;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.kristoff.robomaster_simulator.robomasters.robomaster.RoboMaster;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.pointsimulator.StatusPoint;
import com.kristoff.robomaster_simulator.utils.Position;
import org.lwjgl.Sys;

public class FriendsObservation {
    RoboMaster self1;
    RoboMaster self2;
    RoboMaster friend;
    Position position = new Position();
    int radius = 450;
    int weight = 0;

    public FriendsObservation(RoboMaster self1, RoboMaster self2, RoboMaster friend, int value){
        this.self1 = self1;
        this.self2 = self2;
        this.friend = friend;
        this.weight = value;
    }

    public void setPosition(int x, int y){
        setPosition(new Position(x,y));
    }

    public void setPosition(Position position){
        this.position = position;
    }
    
    public void simulate(int[][] EnemyObservationMapPoints, Array<StatusPoint> eoArrayList){
        float currentFacingDegree = (float)Math.toDegrees(friend.getRotation()) - 90;
        System.out.println(currentFacingDegree);
        int[][] pointsArray = EnemyObservationMapPoints;
        Array<StatusPoint> pointsArrayList = new Array<>();
        this.setPosition(friend.getLidarPosition());
        int centre_x = position.x / 10;
        int centre_y = position.y / 10;
        float precisionOfDegree = 0.01f;
        int x = 0;
        int y = 0;
        for(float degree = currentFacingDegree - 30;degree < currentFacingDegree + 30; degree += precisionOfDegree){
            float radian = degree * MathUtils.degreesToRadians;
            if(degree == 0 || degree == 180){
                for(y = 0;y < radius; y++){
                    if(degree == 0){
                        addPoint(pointsArray,centre_x,centre_y + y);
                        if(Systems.pointSimulator.isPointNotEmpty(centre_x, centre_y + y, self1.pointStatus, self2.pointStatus, this.friend.pointStatus)){
                            pointsArrayList.add(Systems.pointSimulator.getRoboMasterPoint(centre_x, centre_y + y));
                            break;
                        }
                    }
                    else {
                        addPoint(pointsArray,centre_x,centre_y - y);
                        if(Systems.pointSimulator.isPointNotEmpty(centre_x, centre_y - y, self1.pointStatus, self2.pointStatus, this.friend.pointStatus)){
                            pointsArrayList.add(Systems.pointSimulator.getRoboMasterPoint(centre_x, centre_y - y));
                            break;
                        }
                    }
                }
            }
            else if(degree == 90 || degree == 270){
                for(x = 0;x < radius; x++){
                    if(degree == 90){
                        addPoint(pointsArray,centre_x + x,centre_y);
                        if(Systems.pointSimulator.isPointNotEmpty(centre_x + x, centre_y, self1.pointStatus, self2.pointStatus, this.friend.pointStatus)) {
                            pointsArrayList.add(Systems.pointSimulator.getRoboMasterPoint(centre_x + x, centre_y));
                            break;
                        }
                    }
                    else {
                        addPoint(pointsArray,centre_x - x,centre_y);
                        if(Systems.pointSimulator.isPointNotEmpty(centre_x - x, centre_y, self1.pointStatus, self2.pointStatus, this.friend.pointStatus)) {
                            pointsArrayList.add(Systems.pointSimulator.getRoboMasterPoint(centre_x - x, centre_y));
                            break;
                        }
                    }
                }
            }
            else if(degree > 315 || degree < 45 || (degree > 135 && degree < 225)){
                for(y = 0;y < Math.abs(Math.cos(radian) * radius); y++){
                    int offset_x = (int) (Math.round(Math.tan(radian) * y));
                    int offset_y = y;
                    if(degree > 135 && degree < 180){
                        offset_x = - offset_x;
                        offset_y = - offset_y;
                    }
                    else if(degree > 180 && degree < 225){
                        offset_x = - offset_x;
                        offset_y = - offset_y;
                    }
                    else{
                        offset_x = - offset_x;
                    }
                    addPoint(pointsArray,centre_x + offset_x,centre_y + offset_y);
                    if(Systems.pointSimulator.isPointNotEmpty(centre_x + offset_x, centre_y + offset_y, self1.pointStatus, self2.pointStatus, this.friend.pointStatus)) {
                        pointsArrayList.add(Systems.pointSimulator.getRoboMasterPoint(centre_x + offset_x, centre_y + offset_y));
                        break;
                    }
                }
            }
            else{
                for(x = 0;x < Math.abs(Math.sin(radian) * radius); x++){
                    int offset_x = x;
                    int offset_y = (int) (Math.round(x / Math.tan(radian)));
                    if(degree <= 135){
                        offset_y = - offset_y;
                    }
                    else if(degree >= 225){
                        offset_x = - offset_x;
                        offset_y = - offset_y;
                    }
                    else if(degree <= 315){
                        offset_x = - offset_x;
                    }
                    addPoint(pointsArray,centre_x + offset_x,centre_y + offset_y);
                    if(Systems.pointSimulator.isPointNotEmpty(centre_x + offset_x, centre_y + offset_y, self1.pointStatus, self2.pointStatus, this.friend.pointStatus)) {
                        pointsArrayList.add(Systems.pointSimulator.getRoboMasterPoint(centre_x + offset_x, centre_y + offset_y));
                        break;
                    }
                }
            }
            x = 0;
            y = 0;
        }
    }

    private void addPoint(int[][] pointsArray, int x, int y){
        int pointValue = pointsArray[x][y];
        if(pointValue != this.weight){
            if(pointValue != 3){
                pointsArray[x][y] += this.weight;
            }
        }
    }
}
