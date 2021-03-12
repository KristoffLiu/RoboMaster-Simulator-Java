package com.kristoff.robomaster_simulator.robomasters.teams;

import com.kristoff.robomaster_simulator.robomasters.robomaster.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.robomaster.tactics.SearchNode;
import com.kristoff.robomaster_simulator.robomasters.robomaster.types.Enemy;
import com.kristoff.robomaster_simulator.utils.LoopThread;

import java.util.LinkedList;
import java.util.List;

/***
 *
 */
public class StrategyMaker extends LoopThread {
    public Team thisTeam;
    public Team enemyTeam;

    public RoboMaster blue1;
    public RoboMaster blue2;

    /***
     * -1: Not Working
     * 0: 1 v 1 state
     * 1: 1 v 2 state
     * 2: 2 v 1 state
     * 3: 2 v 2 state
     */
    public int counterState = -1;

    public float lockingWeight = 2.0f;

    /***
     * -1: No decision making
     * 0: countering 对抗模式
     * 1: wandering 逃跑模式
     */
    public int decisionType = -1;

    public StrategyMaker(Team team){
        this.thisTeam = team;

        this.delta = 1f;
        this.isStep = true;
    }

    @Override
    public void start(){
        super.start();
        this.enemyTeam = RoboMasters.teamRed;
        blue1 = RoboMasters.getRoboMaster("Blue1");
        blue2 = RoboMasters.getRoboMaster("Blue2");
        blue1.tacticMaker.start();
        blue2.tacticMaker.start();
    }

    @Override
    public void step(){
        switch (thisTeam.roboMastersLeft()){
            case 1 ->{
                switch (enemyTeam.roboMastersLeft()){
                    case 1 -> counterState = 0;
                    case 2 -> counterState = 1;
                }
            }
            case 2 ->{
                switch (enemyTeam.roboMastersLeft()){
                    case 1 -> counterState = 2;
                    case 2 -> counterState = 3;
                }
            }
            default -> {}
        }

        switch (counterState){
            case 0,2 ->{
                for(RoboMaster roboMaster : this.enemyTeam){
                    ((Enemy)roboMaster).lock();
                }
            }
            case 1 ->{
                for(RoboMaster roboMaster : this.thisTeam){
                    float distance1 = roboMaster.getPosition().distanceTo(this.enemyTeam.get(0).getPosition());
                    float distance2 = roboMaster.getPosition().distanceTo(this.enemyTeam.get(1).getPosition());
                    if(distance1 < distance2) {
                        ((Enemy)this.enemyTeam.get(0)).lock();
                        ((Enemy)this.enemyTeam.get(1)).unlock();
                    }
                    else {
                        ((Enemy)this.enemyTeam.get(1)).lock();
                        ((Enemy)this.enemyTeam.get(0)).unlock();
                    }
                }
            }
            case 3 ->{
                List<Float> distances = new LinkedList<>();
                for(RoboMaster enemy : this.enemyTeam){
                    distances.add(enemy.getPosition().distanceTo(this.thisTeam.get(0).getPosition()) +
                            enemy.getPosition().distanceTo(this.thisTeam.get(1).getPosition()));
                }
                if(distances.get(0) < distances.get(1)) {
                    ((Enemy)this.enemyTeam.get(0)).lock();
                    ((Enemy)this.enemyTeam.get(1)).unlock();
                }
                else {
                    ((Enemy)this.enemyTeam.get(1)).lock();
                    ((Enemy)this.enemyTeam.get(0)).unlock();
                }
            }
        }

        for(RoboMaster roboMaster : this.thisTeam){
            if(roboMaster.isAlive()) roboMaster.tacticMaker.updateCounterState(counterState);
        }
    }
}
