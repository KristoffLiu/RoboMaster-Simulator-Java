package com.kristoff.robomaster_simulator.robomasters.modules.enemyobservations;

import com.badlogic.gdx.utils.Array;
import com.kristoff.robomaster_simulator.robomasters.strategies.OneVsTwoCircumventionPathPlanning;
import com.kristoff.robomaster_simulator.systems.matrixsimulation.RoboMasterPoint;
import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.RoboMasterList;
import com.kristoff.robomaster_simulator.robomasters.RoboMasters;
import com.kristoff.robomaster_simulator.utils.LoopThread;
import com.kristoff.robomaster_simulator.utils.Position;

import java.util.concurrent.CopyOnWriteArrayList;

public class EnemiesObservationSimulator extends LoopThread {
    RoboMaster thisRoboMaster;
    Mode mode;

    public Array<RoboMasterPoint> eoArrayList;
    public int[][] eoMatrix    = new int[8490][4890];
    int[][] eoMatrix2    = new int[8490][4890];
    int[][] emptyMatrix = new int[8490][4890];

    public int[][] matrix    = new int[849][489];


    public CopyOnWriteArrayList<EnemiesObservationPoint> dangerousZone = new CopyOnWriteArrayList<>();

    public OneVsTwoCircumventionPathPlanning oneVsTwoCircumventionPathPlanning;

    // 0 = none,
    // 1 = enemy1observed,
    // 2 = enemy2observed,
    // 3 = both

    EnemyObservation enemyObservationOne;
    EnemyObservation enemyObservationTwo;

    Runnable runnable;
    Runnable runnable2;

    public EnemiesObservationSimulator(RoboMaster roboMaster){
        this.thisRoboMaster = roboMaster;
        mode = Mode.self_observation;
        isStep = true;
        delta = 1/60f;
        for(int i=0; i<8490; i++){
            for(int j=0; j<4890; j++){
                emptyMatrix[i][j] = 0;
            }
        }
        oneVsTwoCircumventionPathPlanning = new OneVsTwoCircumventionPathPlanning(matrix, thisRoboMaster);

        runnable = () -> {
            enemyObservationOne.simulate3(matrix,eoArrayList);
        };
        runnable2 = () -> {
            enemyObservationTwo.simulate3(matrix,eoArrayList);
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
                    long startTime = System.currentTimeMillis();//开始时间
                    CopyOnWriteArrayList<EnemiesObservationPoint> arrayList = new CopyOnWriteArrayList<>();

                    for(int i=0; i<8490; i++){
                        for(int j=0; j<4890; j++){
                            eoMatrix2[i][j] = eoMatrix[i][j];
                            eoMatrix[i][j] = 0;
                        }
                    }

                    for(int i=0; i<849; i++){
                        for(int j=0; j<489; j++){
                            matrix[i][j] = 0;
                        }
                    }

                    runnable.run();
                    runnable2.run();

                    long endTime = System.currentTimeMillis();//开始时间
                    //Gdx.app.log("", String.valueOf(endTime - startTime));
                    for(int i=0; i<849; i+=10){
                        for(int j=0; j<489; j+=10){
                            if(this.matrix[i][j] != 0) {
                                arrayList.add(new EnemiesObservationPoint(i,j,matrix[i][j]));
                            }
                        }
                    }
                    dangerousZone = arrayList;


                    for(int i=0; i<849; i+=1){
                        for(int j=0; j<489; j+=1){
                            boolean isBreak = false;
                            for(int k=0; k<10; k+=1){
                                for(int c=0; c<10; c+=1){
                                    if(eoMatrix[i*10+k][j*10+c] != 0){
                                        matrix[i][j] = eoMatrix[i*10+k][j*10+c];
                                        isBreak = true;
                                        break;
                                    }
                                }
                                if (isBreak){
                                    break;
                                }
                            }
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
    }

    public CopyOnWriteArrayList<EnemiesObservationPoint> getDangerousZone(){
        return dangerousZone;
    }

//    public int[][] getDangerousZone(){
//        return eoMatrix;
//    }

    @Override
    public void start(){
        switch (mode){
            case self_observation -> {
                if(thisRoboMaster.team == RoboMasters.teamBlue)
                {
                    enemyObservationOne = new EnemyObservation(thisRoboMaster, RoboMasters.teamRed.get(0),1);
                    enemyObservationTwo = new EnemyObservation(thisRoboMaster, RoboMasters.teamRed.get(1),2);
                }
                else{
                    enemyObservationOne = new EnemyObservation(thisRoboMaster, RoboMasters.teamBlue.get(0),1);
                    enemyObservationTwo = new EnemyObservation(thisRoboMaster, RoboMasters.teamBlue.get(1),2);
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

    public Position getNextPredictedPosition(){
        oneVsTwoCircumventionPathPlanning.update();
        return oneVsTwoCircumventionPathPlanning.resultNode.position;
    }

    public void getPlan(){
        oneVsTwoCircumventionPathPlanning.update();
    }
}
