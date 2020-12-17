package com.kristoff.robomaster_simulator.systems.robomasters.judgement;

import com.kristoff.robomaster_simulator.utils.BackendThread;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMaster;

public class JudgeModule extends BackendThread {
    RoboMaster thisRoboMaster;

    float judgeFrequency = 1/60f;

    float health;
    boolean isDead;



    public JudgeModule(RoboMaster roboMaster){
        this.thisRoboMaster = roboMaster;
        initData();

        isStep = true;
        delta = judgeFrequency;
    }

    public void initData(){
        this.health = thisRoboMaster.property.health;
    }

    @Override
    public void step(){

    }

    public void minushealth(float value){
        this.health -= value;
    }
}
