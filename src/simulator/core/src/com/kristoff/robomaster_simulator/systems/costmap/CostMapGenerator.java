package com.kristoff.robomaster_simulator.systems.costmap;

import com.kristoff.robomaster_simulator.robomasters.Strategy.SearchNode;
import com.kristoff.robomaster_simulator.robomasters.Strategy.StrategyAnalyzer;
import com.kristoff.robomaster_simulator.robomasters.Strategy.StrategyMaker;
import com.kristoff.robomaster_simulator.robomasters.Strategy.TacticState;
import com.kristoff.robomaster_simulator.robomasters.types.Enemy;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.buffs.Buff;
import com.kristoff.robomaster_simulator.systems.buffs.BuffZone;
import com.kristoff.robomaster_simulator.systems.pointsimulator.PointSimulator;
import com.kristoff.robomaster_simulator.teams.enemyobservations.EnemiesObservationPoint;
import com.kristoff.robomaster_simulator.teams.enemyobservations.EnemiesObservationSimulator;
import com.kristoff.robomaster_simulator.utils.LoopThread;
import com.kristoff.robomaster_simulator.utils.Position;

import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

public class CostMapGenerator extends LoopThread {
    public StrategyMaker strategyMaker;
    public int[][] costmap;

    public CostMapGenerator(){
        this.strategyMaker = strategyMaker;
        this.costmap = new int[849][489];
        isStep = true;
        delta = 1/20f;
    }

    @Override
    public void step(){
        generateCostMap();
    }

    @Override
    public void start(){
        super.start();
    }

    public void generateCostMap(){
        long  startTime = System.currentTimeMillis();    //获取开始时间
        for(int i = 0; i < 849; i ++){
            for(int j = 0; j < 489; j ++){
                int cost = 127;
                if(Systems.pointSimulator.isPointTheObstacle(i, j)){
                    costmap[i][j] = 999;
                    continue;
                }
                cost += costOfEnemyObservation(i, j);
                cost += costOfBuff(i, j);
                cost += costToTheCentre(i, j);
                costmap[i][j] = cost < 255 ? cost : 254;
            }
        }
        long endTime = System.currentTimeMillis();    //获取结束时间
        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
    }

    public int costOfEnemyObservation(int x, int y){
        int observationCost = 0;
        int costOfLockedEnemyDistance = 0;
        int costOfUnlockedEnemyDistance = 0;

        if(EnemiesObservationSimulator.isInLockedEnemyViewOnly(x, y)) {
            observationCost = 0;
            costOfLockedEnemyDistance = costOfLockedEnemyDistance(x, y);
        }
        else if(EnemiesObservationSimulator.isInUnlockedEnemyViewOnly(x, y)) {
            observationCost = 10;
            costOfUnlockedEnemyDistance = costOfUnlockedEnemyDistance(x, y);
        }
        else if(EnemiesObservationSimulator.isOutOfBothEnemiesView(x, y))
            observationCost = 10;
        else if(EnemiesObservationSimulator.isInBothEnemiesView(x, y)) {
            observationCost = 30;
            costOfUnlockedEnemyDistance = costOfUnlockedEnemyDistance(x, y) * 2;
        }
        return observationCost + costOfLockedEnemyDistance + costOfUnlockedEnemyDistance;
    }

    public int costOfLockedEnemyDistance(int x, int y){
        int maxRange = EnemiesObservationSimulator.getRadius();
        int minShootingRange = 75;
        int maxShootingRange = 275;
        int peekVal = 64;
        int troughVal = - 100;
        float distanceToEnemy = Enemy.getLockedEnemy().getPointPosition().distanceTo(x,y);
        float cost = 0;
        if(distanceToEnemy <= 50){
            cost = 999;
        }
        else if(distanceToEnemy <= minShootingRange){
            cost = peekVal + distanceToEnemy / minShootingRange * (troughVal - peekVal);
        }
        else if(distanceToEnemy <= maxShootingRange){
            cost = troughVal;
        }
        else{
            cost = (distanceToEnemy - maxShootingRange) / (maxRange - maxShootingRange) * (- troughVal) + troughVal;
        }
        return (int) cost;
    }

    public int costOfUnlockedEnemyDistance(int x, int y){
        int maxRange = EnemiesObservationSimulator.getRadius();
        int peekVal = 64;
        float distanceToEnemy = Enemy.getUnlockedEnemy().getPointPosition().distanceTo(x,y);
        float cost = 0;
        cost = (maxRange - distanceToEnemy) / maxRange * peekVal;
        return (int) cost;
    }

    public int costToTheCentre(int x, int y){
        Position centre = new Position(849 / 2 , 489 / 2);
        int peekVal = 127;
        float coefficient = 0.7f;
        float distanceToTheCentre = centre.distanceTo(x,y);
        int costOfDistanceToEnemy = 0;
        if(distanceToTheCentre > 325 && distanceToTheCentre <= 400){
            costOfDistanceToEnemy = (int) ((distanceToTheCentre - 325) / (400 - 325) * peekVal);
        }
        else if(distanceToTheCentre > 400){
            costOfDistanceToEnemy = 254;
        }
        return costOfDistanceToEnemy;
    }

    public int costOfBuff(int x, int y){
        return BuffZone.costOfBuff(x, y);
    }

    public int[][] getCostMap(){
        return this.costmap;
    }

    public static int getCost(int x, int y){
        return Systems.costMapGenerator.costmap[x][y];
    }

    public static int getCostConsideringFriendPosition(Position position, Position friendPosition){
        float distance = position.distanceTo(friendPosition);
        float safeDis = 120;
        float coefficient = 0.75f;
        int cost = 0;
        if(distance < safeDis){
            cost = (int)(Math.abs(distance - safeDis) * coefficient);
        }
        return Systems.costMapGenerator.costmap[position.x][position.y] + cost;
    }
}
