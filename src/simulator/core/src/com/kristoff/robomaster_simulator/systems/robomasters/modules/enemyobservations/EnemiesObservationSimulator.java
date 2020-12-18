package com.kristoff.robomaster_simulator.systems.robomasters.modules.enemyobservations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMasterList;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMasters;
import com.kristoff.robomaster_simulator.utils.BackendThread;
import com.kristoff.robomaster_simulator.utils.Position;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

public class EnemiesObservationSimulator extends BackendThread {
    RoboMaster thisRoboMaster;
    Mode mode;

    public int[][] eoMatrix    = new int[8490][4890];
    int[][] emptyMatrix = new int[8490][4890];

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
        delta = 1f;
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
                safeZone.clear();
                for(int i=0; i<8490; i++){
                    for(int j=0; j<4890; j++){
                        eoMatrix[i][j] = 0;
                    }
                }
                enemyObservationOne.simulate(eoMatrix);
                enemyObservationTwo.simulate(eoMatrix);
                for(int i=0; i<8490; i+=20){
                    for(int j=0; j<4890; j+=20){
                        if(RoboMasters.teamBlue.get(0).enemiesObservationSimulator.eoMatrix[i][j] != 0) {
                            safeZone.add(new Position(i,j));
                        }
                    }
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
        Gdx.app.log("success", "");
    }

    public CopyOnWriteArrayList<Position> getSafeZone(){
        return safeZone;
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
