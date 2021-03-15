package com.kristoff.robomaster_simulator.robomasters.Strategy;

import com.kristoff.robomaster_simulator.systems.pointsimulator.PointState;
import com.kristoff.robomaster_simulator.teams.Team;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.utils.Position;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class RRT implements StrategyAnalyzer {
    public StrategyMaker strategyMaker;

    public Random random;
    public SearchNode start;
    public int minRand = 0;
    public int maxRand = 1;
    public float expandDistance = 10f;
    public float pathResolution = 1f;
    public int goalSampleRate = 0;
    public int maxIteration = 200;

    public List<SearchNode> nodeList;
    public CopyOnWriteArrayList<Position> positions;
    public CopyOnWriteArrayList<Position> results;
    public int numOfMaxResults = 4;
    public PointState pointState;

    public RRT(StrategyMaker strategyMaker){
        this.strategyMaker = strategyMaker;

        this.start = new SearchNode(strategyMaker.getCurrentPosition().x, strategyMaker.getCurrentPosition().y);
        this.minRand = 0;
        this.maxRand = 1;
        this.expandDistance = 3.0f;
        this.goalSampleRate = 5;
        this.maxIteration = 500;
        this.nodeList = new LinkedList<>();
        this.positions = new CopyOnWriteArrayList<>();
        this.results = new CopyOnWriteArrayList<>();

        this.random = new Random();
        this.numOfMaxResults = numOfMaxResults;
        this.pointState = pointState;
    }

    public RRT(StrategyMaker strategyMaker,
               SearchNode start,
               Point point,
               float expandDistance,
               float pathResolution,
               int goalSampleRate,
               int maxIteration,
               int numOfMaxResults,
               PointState pointState){
        this.start = start;
        this.minRand = 0;
        this.maxRand = 1;
        this.expandDistance = expandDistance;
        this.goalSampleRate = goalSampleRate;
        this.maxIteration = maxIteration;
        this.nodeList = new LinkedList<>();
        this.results = new CopyOnWriteArrayList<>();

        this.random = new Random();
        this.numOfMaxResults = numOfMaxResults;
        this.pointState = pointState;
    }

    public boolean checkAvailability(SearchNode resultNode, int[][] enemiesObservationGrid, PointState pointState){
        for(int i=0;i<60;i++){
            for(int j=0;j<45;j++){
                int x = resultNode.position.x + i - 30;
                int y = resultNode.position.y + j - 23;
                if(        x>=0 && x<849
                        && y>=0 && y<489
                        && (enemiesObservationGrid[x][y] == 3
                        || Systems.pointSimulator.isPointNotEmpty(x, y, pointState))){
                    return false;
                }
            }
        }
        return true;
    }

    public SearchNode steer(SearchNode fromNode, Position toNode, float expandDistance){
        SearchNode newNode = new SearchNode(fromNode.position.x, fromNode.position.y);
        float distance = (int)Math.sqrt((toNode.x - fromNode.position.x) ^ 2 + (toNode.y - fromNode.position.y) ^ 2);
        float theta = (float) Math.atan2(toNode.y - fromNode.position.y, toNode.x - fromNode.position.x);

//        if(expandDistance > distance){
//            expandDistance = distance;
//        }

        newNode.position.x += (int)(expandDistance * Math.cos(theta));
        newNode.position.y += (int)(expandDistance * Math.sin(theta));

//        distance = (int)Math.sqrt((toNode.x - fromNode.position.x) ^ 2 + (toNode.y - fromNode.position.y) ^ 2);
//        theta = (float) Math.atan2(toNode.y - fromNode.position.y, toNode.x - fromNode.position.x);

//        if(distance < this.pathResolution){
//            newNode.position.x = toNode.x;
//            newNode.position.y = toNode.y;
//        }

        newNode.parentNode = fromNode;
        return newNode;
    }

    public Position getRandomPoint(){
//        Position rndPosition;
//        if(random.nextInt(100) > this.goalSampleRate - 1){
//            rndPosition = new Position(
//                    random.nextInt(849),
//                    random.nextInt(489));
//        }
//        else{
//            rndPosition = new Position(849 / 2, 489 /2);
//        }
        return new Position(
                random.nextInt(49),
                random.nextInt(89));
    }

    public int getNearestNodeIndex(List<SearchNode> nodeList, Position rndPosition){
        int minIndex = -1;
        int minDistance = Integer.MAX_VALUE;
        for(int i = 0; i < nodeList.size(); i ++){
            int distance = (rndPosition.x - nodeList.get(i).position.x) ^ 2 +
                           (rndPosition.y - nodeList.get(i).position.y) ^ 2;
            if(minDistance > distance) minIndex = i;
        }
        return minIndex;
    }

    @Override
    public void analyze(TacticState tacticState) {
        start = new SearchNode(strategyMaker.getCurrentPosition().x, strategyMaker.getCurrentPosition().y);
        this.nodeList.clear();
        this.nodeList.add(start);
        this.positions.clear();
        this.positions.add(start.position);
        this.results.clear();
        for(int i = 0; i < this.maxIteration; i++){
            while(results.size() < this.numOfMaxResults){
                Position rnd = getRandomPoint();
                int nearestNodeIndex = getNearestNodeIndex(nodeList, rnd);
                SearchNode nearestNode = this.nodeList.get(nearestNodeIndex);

                SearchNode newNode = steer(nearestNode, rnd, this.expandDistance);
                this.positions.add(newNode.position);

                if(checkAvailability(newNode, Team.me().enemiesObservationSimulator.matrix, this.pointState)){
                    results.add(newNode.position);
                }
                nodeList.add(newNode);
            }
        }
    }
}
