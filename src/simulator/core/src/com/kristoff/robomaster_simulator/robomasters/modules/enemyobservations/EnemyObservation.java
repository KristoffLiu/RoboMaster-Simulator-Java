package com.kristoff.robomaster_simulator.robomasters.modules.enemyobservations;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.matrixsimulation.RoboMasterPoint;
import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.utils.Position;

public class EnemyObservation{
    RoboMaster self;
    RoboMaster enemy;
    Position position = new Position();
    int weight = 0;

    public EnemyObservation(RoboMaster self, RoboMaster enemy, int value){
        this.self = self;
        this.enemy = enemy;
        this.weight = value;
    }

    public void setPosition(int x, int y){
        setPosition(new Position(x,y));
    }

    public void setPosition(Position position){
        this.position = position;
    }

    public void simulate(int[][] EnemyObservationMapPoints, Array<RoboMasterPoint> eoArrayList){
        int[][] pointsArray = EnemyObservationMapPoints;
        Array<RoboMasterPoint> pointsArrayList = new Array<>();
        this.setPosition(enemy.getLidarPosition());
        int centre_x = position.x;
        int centre_y = position.y;
        float precisionOfDegree = 0.05f;
        int x = 0;
        int y = 0;
        for(float degree = 0;degree < 360; degree += precisionOfDegree){
            float radian = degree * MathUtils.degreesToRadians;
            if(degree == 0 || degree == 180){
                for(y = 0;y <4800; y++){
                    if(degree == 0){
                        if(pointsArray[centre_x][centre_y + y] != this.weight) pointsArray[centre_x][centre_y + y] += this.weight;
                        if(Systems.matrixSimulator.isPointNotEmpty(centre_x, centre_y + y, this.self.pointStatus,this.enemy.pointStatus)){
                            pointsArrayList.add(Systems.matrixSimulator.getRoboMasterPoint(centre_x, centre_y + y));
                            break;
                        }
                    }
                    else {
                        if(pointsArray[centre_x][centre_y - y] != this.weight) pointsArray[centre_x][centre_y - y] += this.weight;
                        if(Systems.matrixSimulator.isPointNotEmpty(centre_x, centre_y - y,this.self.pointStatus,this.enemy.pointStatus)){
                            pointsArrayList.add(Systems.matrixSimulator.getRoboMasterPoint(centre_x, centre_y - y));
                            break;
                        }
                    }
                }
            }
            else if(degree == 90 || degree == 270){
                for(x = 0;x <8400; x++){
                    if(degree == 90){
                        if(pointsArray[centre_x + x][centre_y] != this.weight) pointsArray[centre_x + x][centre_y] += this.weight;
                        if(Systems.matrixSimulator.isPointNotEmpty(centre_x + x, centre_y,this.self.pointStatus,this.enemy.pointStatus)) {
                            pointsArrayList.add(Systems.matrixSimulator.getRoboMasterPoint(centre_x + x, centre_y));
                            break;
                        }
                    }
                    else {
                        if(pointsArray[centre_x - x][centre_y] != this.weight) pointsArray[centre_x - x][centre_y] += this.weight;
                        if(Systems.matrixSimulator.isPointNotEmpty(centre_x - x, centre_y,this.self.pointStatus,this.enemy.pointStatus)) {
                            pointsArrayList.add(Systems.matrixSimulator.getRoboMasterPoint(centre_x - x, centre_y));
                            break;
                        }
                    }
                }
            }
            else if(degree > 315 || degree < 45 || (degree > 135 && degree < 225)){
                for(y = 0;y <4800; y++){
                    int offset_x = (int) (Math.tan(radian) * y);
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
                    if(pointsArray[centre_x + offset_x][centre_y + offset_y] != this.weight) pointsArray[centre_x + offset_x][centre_y + offset_y] += this.weight;
                    if(Systems.matrixSimulator.isPointNotEmpty(centre_x + offset_x, centre_y + offset_y,this.self.pointStatus,this.enemy.pointStatus)) {
                        pointsArrayList.add(Systems.matrixSimulator.getRoboMasterPoint(centre_x + offset_x, centre_y + offset_y));
                        break;
                    }
                }
            }
            else{
                for(x = 0;x <8400; x++){
                    int offset_x = x;
                    int offset_y = (int) (x / Math.tan(radian));
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
                    if(pointsArray[centre_x + offset_x][centre_y + offset_y] != this.weight) pointsArray[centre_x + offset_x][centre_y + offset_y] += this.weight;
                    if(Systems.matrixSimulator.isPointNotEmpty(centre_x + offset_x, centre_y + offset_y,this.self.pointStatus,this.enemy.pointStatus)) {
                        pointsArrayList.add(Systems.matrixSimulator.getRoboMasterPoint(centre_x + offset_x, centre_y + offset_y));
                        break;
                    }
                }
            }
            x = 0;
            y = 0;
        }
    }

    public void simulate2(int[][] EnemyObservationMapPoints, Array<RoboMasterPoint> eoArrayList){
        int[][] pointsArray = EnemyObservationMapPoints;
        Array<RoboMasterPoint> pointsArrayList = new Array<>();
        this.setPosition(enemy.getLidarPosition());
        int centre_x = position.x;
        int centre_y = position.y;
        float precisionOfDegree = 1f;
        int x = 0;
        int y = 0;
        for(float degree = 0;degree < 360; degree += precisionOfDegree){
            float radian = degree * MathUtils.degreesToRadians;
            if(degree == 0 || degree == 180){
                for(y = 0;y <4800; y++){
                    if(degree == 0){
                        if(pointsArray[centre_x][centre_y + y] != this.weight) pointsArray[centre_x][centre_y + y] += this.weight;
                        if(Systems.matrixSimulator.isPointNotEmpty(centre_x, centre_y + y, this.self.pointStatus,this.enemy.pointStatus)){
                            pointsArrayList.add(Systems.matrixSimulator.getRoboMasterPoint(centre_x, centre_y + y));
                            break;
                        }
                    }
                    else {
                        if(pointsArray[centre_x][centre_y - y] != this.weight) pointsArray[centre_x][centre_y - y] += this.weight;
                        if(Systems.matrixSimulator.isPointNotEmpty(centre_x, centre_y - y,this.self.pointStatus,this.enemy.pointStatus)){
                            pointsArrayList.add(Systems.matrixSimulator.getRoboMasterPoint(centre_x, centre_y - y));
                            break;
                        }
                    }
                }
            }
            else if(degree < 180){
                for(x = 0;x <8490; x++){
                    y = (int)(Math.cos(radian) / Math.sin(radian) * x);
                    if(pointsArray[centre_x + x][centre_y + y] != this.weight) pointsArray[centre_x + x][centre_y + y] += this.weight;
                    if(Systems.matrixSimulator.isPointNotEmpty(centre_x + x, centre_y + y,this.self.pointStatus,this.enemy.pointStatus)){
                        pointsArrayList.add(Systems.matrixSimulator.getRoboMasterPoint(centre_x + x, centre_y + y));
                        break;
                    }
                }
            }
            else {
                for(x = 0;x <8490; x++){
                    y = (int)(Math.cos(radian) / Math.sin(radian) * x);
                    if(pointsArray[centre_x - x][centre_y + y] != this.weight) pointsArray[centre_x - x][centre_y + y] += this.weight;
                    if(Systems.matrixSimulator.isPointNotEmpty(centre_x - x, centre_y + y,this.self.pointStatus,this.enemy.pointStatus)){
                        pointsArrayList.add(Systems.matrixSimulator.getRoboMasterPoint(centre_x - x, centre_y + y));
                        break;
                    }
                }
            }
        }
    }

    public void simulate3(int[][] EnemyObservationMapPoints, Array<RoboMasterPoint> eoArrayList){
        int[][] pointsArray = EnemyObservationMapPoints;
        Array<RoboMasterPoint> pointsArrayList = new Array<>();
        this.setPosition(enemy.getLidarPosition());
        int centre_x = position.x / 10;
        int centre_y = position.y / 10;
        float precisionOfDegree = 0.01f;
        int x = 0;
        int y = 0;
        for(float degree = 0;degree < 360; degree += precisionOfDegree){
            float radian = degree * MathUtils.degreesToRadians;
            if(degree == 0 || degree == 180){
                for(y = 0;y <489; y++){
                    if(degree == 0){
                        addPoint(pointsArray,centre_x,centre_y + y);
                        if(Systems.matrixSimulator.isPointNotEmptyLowResolution(centre_x, centre_y + y, this.self.pointStatus,this.enemy.pointStatus)){
                            pointsArrayList.add(Systems.matrixSimulator.getRoboMasterPoint(centre_x, centre_y + y));
                            break;
                        }
                    }
                    else {
                        addPoint(pointsArray,centre_x,centre_y - y);
                        if(Systems.matrixSimulator.isPointNotEmptyLowResolution(centre_x, centre_y - y,this.self.pointStatus,this.enemy.pointStatus)){
                            pointsArrayList.add(Systems.matrixSimulator.getRoboMasterPoint(centre_x, centre_y - y));
                            break;
                        }
                    }
                }
            }
            else if(degree == 90 || degree == 270){
                for(x = 0;x <849; x++){
                    if(degree == 90){
                        addPoint(pointsArray,centre_x + x,centre_y);
                        if(Systems.matrixSimulator.isPointNotEmptyLowResolution(centre_x + x, centre_y,this.self.pointStatus,this.enemy.pointStatus)) {
                            pointsArrayList.add(Systems.matrixSimulator.getRoboMasterPoint(centre_x + x, centre_y));
                            break;
                        }
                    }
                    else {
                        addPoint(pointsArray,centre_x - x,centre_y);
                        if(Systems.matrixSimulator.isPointNotEmptyLowResolution(centre_x - x, centre_y,this.self.pointStatus,this.enemy.pointStatus)) {
                            pointsArrayList.add(Systems.matrixSimulator.getRoboMasterPoint(centre_x - x, centre_y));
                            break;
                        }
                    }
                }
            }
            else if(degree > 315 || degree < 45 || (degree > 135 && degree < 225)){
                for(y = 0;y <480; y++){
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
                    if(Systems.matrixSimulator.isPointNotEmptyLowResolution(centre_x + offset_x, centre_y + offset_y,this.self.pointStatus,this.enemy.pointStatus)) {
                        pointsArrayList.add(Systems.matrixSimulator.getRoboMasterPoint(centre_x + offset_x, centre_y + offset_y));
                        break;
                    }
                }
            }
            else{
                for(x = 0;x <840; x++){
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
                    if(Systems.matrixSimulator.isPointNotEmptyLowResolution(centre_x + offset_x, centre_y + offset_y,this.self.pointStatus,this.enemy.pointStatus)) {
                        pointsArrayList.add(Systems.matrixSimulator.getRoboMasterPoint(centre_x + offset_x, centre_y + offset_y));
                        break;
                    }
                }
            }
            x = 0;
            y = 0;
        }
    }

    public void addPoint(int[][] pointsArray, int x, int y){
        int pointValue = pointsArray[x][y];
        if(pointValue != this.weight){
            if(pointValue != 3){
                pointsArray[x][y] += this.weight;
            }
        }
    }

}
