package com.kristoff.robomaster_simulator.systems.robomasters.modules.enemyobservations;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.matrixsimulation.MatrixSimulator;
import com.kristoff.robomaster_simulator.systems.matrixsimulation.RoboMasterPoint;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.utils.BackendThread;

import java.util.Arrays;

public class EnemyObservation{
    RoboMaster enemy;
    Vector2 position = new Vector2();
    int weight = 0;

    public EnemyObservation(RoboMaster roboMaster, int value){
        this.enemy = roboMaster;
        this.weight = value;
    }

    public void setPosition(float x, float y){
        setPosition(new Vector2(x,y));
    }

    public void setPosition(Vector2 vector2){
        this.position = vector2;
    }

    public void simulate(int[][] EnemyObservationMapPoints, Array<RoboMasterPoint> eoArrayList){
        int[][] pointsArray = EnemyObservationMapPoints;
        Array<RoboMasterPoint> pointsArrayList = new Array<>();
        this.setPosition(enemy.getLidarPosition());
        int centre_x = (int) (position.x * 1000);
        int centre_y = (int) (position.y * 1000);
        float precisionOfDegree = 0.5f;
        int x = 0;
        int y = 0;
        for(float degree = 0;degree < 360; degree += precisionOfDegree){
            float radian = degree * MathUtils.degreesToRadians;
            if(degree == 0 || degree == 180){
                for(y = 0;y <4800; y++){
                    if(degree == 0){
                        if(pointsArray[centre_x][centre_y + y] != this.weight) pointsArray[centre_x][centre_y + y] += this.weight;
                        if(Systems.matrixSimulator.isPointNotEmpty(centre_x, centre_y + y,this.enemy.pointStatus))
                        {
                            pointsArrayList.add(Systems.matrixSimulator.getRoboMasterPoint(centre_x, centre_y + y));
                            break;
                        }

                    }
                    else {
                        if(pointsArray[centre_x][centre_y - y] != this.weight) pointsArray[centre_x][centre_y - y] += this.weight;
                        if(Systems.matrixSimulator.isPointNotEmpty(centre_x, centre_y - y,this.enemy.pointStatus)){
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
                        if(Systems.matrixSimulator.isPointNotEmpty(centre_x + x, centre_y,this.enemy.pointStatus)) {
                            pointsArrayList.add(Systems.matrixSimulator.getRoboMasterPoint(centre_x + x, centre_y));
                            break;
                        }
                    }
                    else {
                        if(pointsArray[centre_x - x][centre_y] != this.weight) pointsArray[centre_x - x][centre_y] += this.weight;
                        if(Systems.matrixSimulator.isPointNotEmpty(centre_x - x, centre_y,this.enemy.pointStatus)) {
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
                    if(Systems.matrixSimulator.isPointNotEmpty(centre_x + offset_x, centre_y + offset_y,this.enemy.pointStatus)) {
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
                    if(Systems.matrixSimulator.isPointNotEmpty(centre_x + offset_x, centre_y + offset_y,this.enemy.pointStatus)) {
                        pointsArrayList.add(Systems.matrixSimulator.getRoboMasterPoint(centre_x + offset_x, centre_y + offset_y));
                        break;
                    }
                }
            }
            x = 0;
            y = 0;
        }
    }

}
