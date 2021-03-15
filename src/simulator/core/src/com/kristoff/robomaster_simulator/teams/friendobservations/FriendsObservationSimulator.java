package com.kristoff.robomaster_simulator.teams.friendobservations;

import com.badlogic.gdx.utils.Array;
import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.types.Enemy;
import com.kristoff.robomaster_simulator.teams.RoboMasters;
import com.kristoff.robomaster_simulator.teams.Team;
import com.kristoff.robomaster_simulator.systems.pointsimulator.StatePoint;
import com.kristoff.robomaster_simulator.teams.enemyobservations.EnemiesObservationSimulator;
import com.kristoff.robomaster_simulator.utils.LoopThread;
import com.kristoff.robomaster_simulator.utils.Position;

import java.util.concurrent.CopyOnWriteArrayList;

public class FriendsObservationSimulator extends LoopThread {
    public static FriendsObservationSimulator current;

    Team thisTeam;
    Mode mode;

    public Array<StatePoint> eoArrayList;
    public int[][] eoMatrix    = new int[849][489];
    int[][] eoMatrix2    = new int[849][489];
    int[][] emptyMatrix = new int[849][489];

    public int[][] matrix    = new int[849][489];

    public CopyOnWriteArrayList<FriendsObservationPoint> dangerousZone = new CopyOnWriteArrayList<>();

    // 0 = none,
    // 1 = enemy1observed,
    // 2 = enemy2observed,
    // 3 = both

    FriendObservation friendObservationOne;
    FriendObservation friendObservationTwo;

    Runnable runnable;
    Runnable runnable2;

    public FriendsObservationSimulator(Team team){
        current = this;
        thisTeam = team;
        mode = Mode.self_observation;
        isStep = true;
        delta = 1/60f;
        for(int i=0; i<849; i++){
            for(int j=0; j<489; j++){
                emptyMatrix[i][j] = 0;
            }
        }
    }

    public enum Mode{
        self_observation,
        global_observation
    }

    @Override
    public void step(){
        synchronized(matrix){
            CopyOnWriteArrayList<FriendsObservationPoint> arrayList = new CopyOnWriteArrayList<>();

            int[][] temp = new int[849][489];

            friendObservationOne.simulate(temp);
            friendObservationTwo.simulate(temp);

            for(int i=0; i<849; i+=10){
                for(int j=0; j<489; j+=10){
                    if(temp[i][j] != 0) {
                        arrayList.add(new FriendsObservationPoint(i,j,temp[i][j]));
                    }
                }
            }
            matrix = temp;
            dangerousZone = arrayList;
        }
    }

    public CopyOnWriteArrayList<FriendsObservationPoint> getDangerousZone(){
        return dangerousZone;
    }

    @Override
    public void start(){
        friendObservationOne = new FriendObservation(RoboMasters.teamRed.get(0), RoboMasters.teamRed.get(1), this.thisTeam.get(0),1);
        friendObservationTwo = new FriendObservation(RoboMasters.teamRed.get(0), RoboMasters.teamRed.get(1), this.thisTeam.get(1),2);
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

    public static boolean canSeeLockedEnemy(int x, int y, RoboMaster currentRoboMaster){
        for(int i=0;i<60;i++){
            for(int j=0;j<45;j++){
                int val = currentRoboMaster.teamIndex + 1;
                Position position = Enemy.getLockedEnemy().actor.getPoint(i, j);
                if(current.matrix[position.x][position.y] == val || current.matrix[position.x][position.y] == 3)
                    return true;
            }
        }
        return false;
    }

    public static boolean canSeeUnlockedEnemy(int x, int y, RoboMaster currentRoboMaster){
        for(int i=0;i<60;i++){
            for(int j=0;j<45;j++){
                int val = currentRoboMaster.teamIndex + 1;
                Position position = Enemy.getUnlockedEnemy().actor.getPoint(i, j);
                if(current.matrix[x][y] == val)
                    return true;
            }
        }
        return false;
    }

    public static boolean isInUnlockedEnemyViewOnly(int x, int y){
        return current.matrix[x][y] == Enemy.getUnlockedEnemy().teamIndex + 1;
    }
}
