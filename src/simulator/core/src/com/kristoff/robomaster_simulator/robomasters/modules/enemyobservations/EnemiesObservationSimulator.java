package com.kristoff.robomaster_simulator.robomasters.modules.enemyobservations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.kristoff.robomaster_simulator.systems.matrixsimulation.RoboMasterPoint;
import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.RoboMasterList;
import com.kristoff.robomaster_simulator.robomasters.RoboMasters;
import com.kristoff.robomaster_simulator.utils.BackendThread;
import com.kristoff.robomaster_simulator.utils.Position;

import java.util.concurrent.CopyOnWriteArrayList;

public class EnemiesObservationSimulator extends BackendThread {
    RoboMaster thisRoboMaster;
    Mode mode;

    public Array<RoboMasterPoint> eoArrayList;
    public int[][] eoMatrix    = new int[8490][4890];
    public int[][] matrixForROS    = new int[849][489];
    int[][] emptyMatrix        = new int[8490][4890];

    public CopyOnWriteArrayList<Position> safeZone = new CopyOnWriteArrayList<>();

    // 0 = none,
    // 1 = enemy1observed,
    // 2 = enemy2observed,
    // 3 = both

    EnemyObservation enemyObservationOne;
    EnemyObservation enemyObservationTwo;

    Runnable runnable;

    public EnemiesObservationSimulator(RoboMaster roboMaster){
        this.thisRoboMaster = roboMaster;
        mode = Mode.self_observation;
        isStep = true;
        delta = 1/60f;

        for(int i=0; i<849; i++){
            for(int j=0; j<489; j++){
                matrixForROS[i][j] = 0;
            }
        }
        for(int i=0; i<8490; i++){
            for(int j=0; j<4890; j++){
                emptyMatrix[i][j] = 0;
            }
        }

        runnable = () -> {

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
                CopyOnWriteArrayList<Position> arrayList = new CopyOnWriteArrayList<>();
                for(int i=0; i<8490; i++){
                    for(int j=0; j<4890; j++){
                        eoMatrix[i][j] = 0;
                    }
                }
                enemyObservationOne.simulate(eoMatrix,eoArrayList);
                enemyObservationTwo.simulate(eoMatrix,eoArrayList);
                for(int i=0; i<8490; i+=20){
                    for(int j=0; j<4890; j+=20){
                        if(RoboMasters.teamBlue.get(0).enemiesObservationSimulator.eoMatrix[i][j] != 0) {
                            arrayList.add(new Position(i,j));
                        }
                    }
                }

                for(int i=0; i<849; i++){
                    for(int j=0; j<489; j++){
                        matrixForROS[i][j] = eoMatrix[i*10][j*10];
                    }
                }
                safeZone = arrayList;
            }
            case global_observation -> {
                if(thisRoboMaster.team == RoboMasters.teamBlue)
                {

                }
                else{

                }
            }
        }
        Gdx.app.log("success", "");
    }

    public CopyOnWriteArrayList<Position> getSafeZone(){
        return safeZone;
    }

    public int[][] getDangerousZone(){
        return eoMatrix;
    }

    @Override
    public void start(){
        switch (mode){
            case self_observation -> {
                if(thisRoboMaster.team == RoboMasters.teamBlue)
                {
                    enemyObservationOne = new EnemyObservation(RoboMasters.teamRed.get(0),1);
                    enemyObservationTwo = new EnemyObservation(RoboMasters.teamRed.get(1),2);
                }
                else{
                    enemyObservationOne = new EnemyObservation(RoboMasters.teamBlue.get(0),1);
                    enemyObservationTwo = new EnemyObservation(RoboMasters.teamBlue.get(1),2);
                }
            }
            case global_observation -> {
                if(thisRoboMaster.team == RoboMasters.teamBlue)
                {

                }
                else{

                }
            }
        }
        super.start();
    }

    public void a(RoboMasterList team){

    }
}
