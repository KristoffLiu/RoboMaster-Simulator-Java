package com.kristoff.robomaster_simulator.teams;

import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.Strategy.CounterState;
import com.kristoff.robomaster_simulator.robomasters.types.Enemy;
import com.kristoff.robomaster_simulator.utils.LoopThread;

import java.util.LinkedList;
import java.util.List;

/***
 *
 */
public class InfoAnalyzer extends LoopThread {
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
    public CounterState counterState = CounterState.TwoVSTwo;

    public float lockingWeight = 1.5f;


    public InfoAnalyzer(Team team){
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
        blue1.costMap.start();
        blue2.costMap.start();
        blue1.strategyMaker.start();
        blue2.strategyMaker.start();
    }

    @Override
    public void step(){
        switch (thisTeam.roboMastersLeft()){
            case 1 ->{
                switch (enemyTeam.roboMastersLeft()){
                    case 1 -> counterState = CounterState.OneVSOne;
                    case 2 -> counterState = CounterState.OneVSTwo;
                }
            }
            case 2 ->{
                switch (enemyTeam.roboMastersLeft()){
                    case 1 -> counterState = CounterState.TwoVSOne;
                    case 2 -> counterState = CounterState.TwoVSTwo;
                }
            }
        }
        Enemy lockedEnemy = Enemy.getLockedEnemy();
        Enemy unlockedEnemy = Enemy.getUnlockedEnemy();
        switch (counterState){
            case OneVSOne, TwoVSOne -> {
                for(RoboMaster roboMaster : this.enemyTeam){
                    if(roboMaster.isAlive){
                        ((Enemy)roboMaster).lock();
                    }
                }
            }
            case OneVSTwo -> {
                for(RoboMaster roboMaster : this.thisTeam){
                    float distance1 = roboMaster.getPosition().distanceTo(Enemy.getLockedEnemy().getPosition());
                    float distance2 = roboMaster.getPosition().distanceTo(Enemy.getUnlockedEnemy().getPosition()) * lockingWeight;
                    if(distance1 < distance2) {
                        ((Enemy)this.enemyTeam.get(0)).lock();
                    }
                    else {
                        ((Enemy)this.enemyTeam.get(1)).lock();
                    }
                }
            }
            case TwoVSTwo ->{
                float lockedEnemyDistance =
                        (lockedEnemy.getPosition().distanceTo(this.thisTeam.get(0).getPosition()) +
                        lockedEnemy.getPosition().distanceTo(this.thisTeam.get(1).getPosition())) ;
                float unlockedEnemyDistance =
                        (unlockedEnemy.getPosition().distanceTo(this.thisTeam.get(0).getPosition()) +
                        unlockedEnemy.getPosition().distanceTo(this.thisTeam.get(1).getPosition())) * 1.6f;
                if(lockedEnemyDistance > unlockedEnemyDistance) {
                    unlockedEnemy.lock();
                }
            }
        }

        for(RoboMaster roboMaster : this.thisTeam){
            if(roboMaster.isAlive()) roboMaster.strategyMaker.updateCounterState(counterState);
        }
    }
}
