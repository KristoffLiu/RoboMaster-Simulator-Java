package com.kristoff.robomaster_simulator.robomasters.teams.enemyobservations;

import com.badlogic.gdx.utils.Array;
import com.kristoff.robomaster_simulator.robomasters.teams.Team;
import com.kristoff.robomaster_simulator.systems.pointsimulator.StatusPoint;
import com.kristoff.robomaster_simulator.robomasters.teams.RoboMasters;
import com.kristoff.robomaster_simulator.utils.LoopThread;

import java.util.concurrent.CopyOnWriteArrayList;

public class EnemiesObservationSimulator extends LoopThread {
    public static EnemiesObservationSimulator current;
    Team thisTeam;
    Mode mode;

    public Array<StatusPoint> eoArrayList;

    public int[][] matrix = new int[849][489];

    public CopyOnWriteArrayList<EnemiesObservationPoint> dangerousZone = new CopyOnWriteArrayList<>();

    // 0 = none,
    // 1 = enemy1observed,
    // 2 = enemy2observed,
    // 3 = both

    EnemyObservation enemyObservationOne;
    EnemyObservation enemyObservationTwo;

    Runnable runnable;
    Runnable runnable2;

    public EnemiesObservationSimulator(Team team){
        thisTeam = team;
        current = this;
        mode = Mode.self_observation;
        isStep = true;
        delta = 1/60f;

        runnable = () -> {
            enemyObservationOne.simulate(matrix,eoArrayList);
        };
        runnable2 = () -> {
            enemyObservationTwo.simulate(matrix,eoArrayList);
        };
    }

    public enum Mode{
        self_observation,
        global_observation
    }

    @Override
    public void step(){
        synchronized(matrix){
            CopyOnWriteArrayList<EnemiesObservationPoint> arrayList = new CopyOnWriteArrayList<>();
            int[][] temp = new int[849][489];
            enemyObservationOne.simulate(temp,eoArrayList);
            enemyObservationTwo.simulate(temp,eoArrayList);


            for(int i=0; i<849; i+=10){
                for(int j=0; j<489; j+=10){
                    if(this.matrix[i][j] != 0) {
                        arrayList.add(new EnemiesObservationPoint(i,j,temp[i][j]));
                    }
                }
            }
            matrix = temp;
            dangerousZone = arrayList;
        }
    }

    public void clearCache(){
        synchronized(matrix){
            matrix = new int[849][489];
        }
    }

    public CopyOnWriteArrayList<EnemiesObservationPoint> getDangerousZone(){
        return dangerousZone;
    }

    @Override
    public void start(){
        switch (mode){
            case self_observation -> {
                if(thisTeam == RoboMasters.teamBlue)
                {
                    enemyObservationOne = new EnemyObservation(thisTeam.get(0), thisTeam.get(1), RoboMasters.teamRed.get(0),1);
                    enemyObservationTwo = new EnemyObservation(thisTeam.get(0), thisTeam.get(1), RoboMasters.teamRed.get(1),2);
                }
                else{
                    enemyObservationOne = new EnemyObservation(thisTeam.get(0), thisTeam.get(1), RoboMasters.teamBlue.get(0),1);
                    enemyObservationTwo = new EnemyObservation(thisTeam.get(0), thisTeam.get(1), RoboMasters.teamBlue.get(1),2);
                }
            }
            case global_observation -> {
                if(thisTeam == RoboMasters.teamBlue)
                {

                }
                else{

                }
            }
        }
        super.start();
    }

    public static int getEnemiesObservationStatus(int x, int y){ return current.matrix[x][y]; }

    public static boolean isOutOfBothEnemiesView(int x, int y){ return current.matrix[x][y] == 0; }

    public static boolean isInFirstEnemiesView(int x, int y){ return current.matrix[x][y] == 1; }

    public static boolean isInSecondEnemiesView(int x, int y){
        return current.matrix[x][y] == 2;
    }

    public static boolean isInBothEnemiesView(int x, int y){
        return current.matrix[x][y] == 3;
    }
}
