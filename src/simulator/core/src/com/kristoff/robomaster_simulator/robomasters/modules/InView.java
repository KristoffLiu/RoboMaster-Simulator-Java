package com.kristoff.robomaster_simulator.robomasters.modules;

import com.kristoff.robomaster_simulator.robomasters.types.Enemy;
import com.kristoff.robomaster_simulator.utils.LoopThread;

public class InView extends LoopThread {
    Enemy thisEnemy;
    int timer = 0;
    int timerLimit = 0;

    public InView(Enemy enemy, int timerLimit){
        this.thisEnemy = enemy;
        this.timerLimit = timerLimit;
        delta = 1f;
        isStep = true;
    }

    @Override
    public void step() {
        timer ++;
        if(timer > timerLimit) this.thisEnemy.setNotInTheView();
    }

    public void resetTimer(){
        this.timer = 0;
    }
}
