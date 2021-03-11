package com.kristoff.robomaster_simulator.robomasters.robomaster.modules;

import com.kristoff.robomaster_simulator.robomasters.robomaster.types.Enemy;
import com.kristoff.robomaster_simulator.utils.LoopThread;

public class LockingSystem extends LoopThread {
    Enemy thisEnemy;
    int timer = 0;
    int timerLimit = 0;

    public LockingSystem(Enemy enemy, int timerLimit){
        this.thisEnemy = enemy;
        this.timerLimit = timerLimit;
        delta = 1f;
        isStep = true;
    }

    @Override
    public void step() {
        timer ++;
        if(timer > timerLimit) this.thisEnemy.unlock();
    }

    public void resetTimer(){
        this.timer = 0;
    }
}
