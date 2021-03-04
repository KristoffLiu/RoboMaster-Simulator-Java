package com.kristoff.robomaster_simulator.robomasters.robomaster.modules;

import com.kristoff.robomaster_simulator.utils.LoopThread;

public class DecisionMaker extends LoopThread {

    /***
     * -1: Not Working
     * 0: 2 v 2 state
     * 1: 2 v 1 state
     * 2: 1 v 2 state
     * 3: 1 v 1 state
     */
    public int counterState = -1;

    /***
     * -1: No decision making
     * 0: countering 对抗模式
     * 1: wandering 逃跑模式
     */
    public int decisionType = -1;

    public void step(){

    }
}
