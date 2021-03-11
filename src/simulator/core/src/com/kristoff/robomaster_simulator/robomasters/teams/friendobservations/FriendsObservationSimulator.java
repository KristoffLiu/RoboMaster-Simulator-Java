package com.kristoff.robomaster_simulator.robomasters.teams.friendobservations;

import com.badlogic.gdx.utils.Array;
import com.kristoff.robomaster_simulator.robomasters.teams.RoboMasters;
import com.kristoff.robomaster_simulator.robomasters.teams.Team;
import com.kristoff.robomaster_simulator.systems.pointsimulator.StatusPoint;
import com.kristoff.robomaster_simulator.utils.LoopThread;

import java.util.concurrent.CopyOnWriteArrayList;

public class FriendsObservationSimulator extends LoopThread {
    Team thisTeam;
    Mode mode;

    public Array<StatusPoint> eoArrayList;
    public int[][] eoMatrix    = new int[849][489];
    int[][] eoMatrix2    = new int[849][489];
    int[][] emptyMatrix = new int[849][489];

    public int[][] matrix    = new int[849][489];

    public CopyOnWriteArrayList<FriendsObservationPoint> dangerousZone = new CopyOnWriteArrayList<>();

    // 0 = none,
    // 1 = enemy1observed,
    // 2 = enemy2observed,
    // 3 = both

    FriendsObservation friendsObservationOne;
    FriendsObservation friendsObservationTwo;

    Runnable runnable;
    Runnable runnable2;

    public FriendsObservationSimulator(Team team){
        thisTeam = team;
        mode = Mode.self_observation;
        isStep = false;
        delta = 1/60f;
        for(int i=0; i<849; i++){
            for(int j=0; j<489; j++){
                emptyMatrix[i][j] = 0;
            }
        }

        runnable = () -> {
            friendsObservationOne.simulate(matrix,eoArrayList);
        };
        runnable2 = () -> {
            friendsObservationTwo.simulate(matrix,eoArrayList);
        };
    }

    public enum Mode{
        self_observation,
        global_observation
    }

    @Override
    public void step(){
        switch (mode){
            case self_observation -> {
                synchronized(matrix){
                    CopyOnWriteArrayList<FriendsObservationPoint> arrayList = new CopyOnWriteArrayList<>();

                    for(int i=0; i<849; i++){
                        for(int j=0; j<489; j++){
                            eoMatrix2[i][j] = eoMatrix[i][j];
                            eoMatrix[i][j] = 0;
                        }
                    }

                    for(int i=0; i<849; i++){
                        for(int j=0; j<489; j++){
                            matrix[i][j] = 0;
                        }
                    }

                    friendsObservationOne.simulate(matrix,eoArrayList);
                    friendsObservationTwo.simulate(matrix,eoArrayList);

                    for(int i=0; i<849; i+=10){
                        for(int j=0; j<489; j+=10){
                            if(this.matrix[i][j] != 0) {
                                arrayList.add(new FriendsObservationPoint(i,j,matrix[i][j]));
                            }
                        }
                    }
                    dangerousZone = arrayList;
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
    }

    public CopyOnWriteArrayList<FriendsObservationPoint> getDangerousZone(){
        return dangerousZone;
    }

    @Override
    public void start(){
        switch (mode){
            case self_observation -> {
                if(thisTeam == RoboMasters.teamBlue)
                {
                    friendsObservationOne = new FriendsObservation(RoboMasters.teamRed.get(0), RoboMasters.teamRed.get(1), this.thisTeam.get(0),1);
                    friendsObservationTwo = new FriendsObservation(RoboMasters.teamRed.get(0), RoboMasters.teamRed.get(1), this.thisTeam.get(1),2);
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

    public boolean isOutOfDangerousZone(int x, int y){
        return matrix[x][y] == 0;
    }

    public boolean isInFirstEnemiesView(int x, int y){
        return matrix[x][y] == 1;
    }

    public boolean isInSecondEnemiesView(int x, int y){
        return matrix[x][y] == 2;
    }

    public boolean isInBothEnemiesView(int x, int y){
        return matrix[x][y] == 3;
    }
}
