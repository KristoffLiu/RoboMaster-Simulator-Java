package com.kristoff.robomaster_simulator.robomasters.judgement;

import com.kristoff.robomaster_simulator.utils.LoopThread;
import com.kristoff.robomaster_simulator.robomasters.RoboMaster;

public class JudgeModule extends LoopThread {
    RoboMaster thisRoboMaster;

    float judgeFrequency = 1/60f;
    float cannonHeatJudgeFrequency = 1/10f;

    float health;
    float cannonHeat;
    int bullet;
    boolean isDead;
    float bulletSpeed;



    public JudgeModule(RoboMaster roboMaster){
        this.thisRoboMaster = roboMaster;
        initData();

        isStep = true;
        delta = judgeFrequency;
    }

    public void initData(){
        this.health = thisRoboMaster.property.health;
        this.bullet = thisRoboMaster.property.numOfBulletsOwned;
        this.isDead = false;
        this.cannonHeat = thisRoboMaster.property.cannonHeat;
        this.bulletSpeed = thisRoboMaster.property.bulletSpeed;

    }

    @Override
    public void step(){
        if (this.health <= 0){
            this.isDead = true;
        }

    }

    public void healthChange(float healthChange){
        this.health += healthChange;
    }




    public void bulletHitDamage(String caseName){
        switch (caseName) {
            case "front" -> healthChange(-20);
            case "right", "left" -> healthChange(-40);
            case "back" -> healthChange(-60);
            case "hit" -> healthChange(-10);
            case "30 > bullet speed > 25" -> healthChange(-200);
            case "35 > bullet speed >= 30" -> healthChange(-1000);
            case "bullet speed >= 35" -> healthChange(-2000);
        }
    }


    public void setCannonHeat(float cannonHeat){this.cannonHeat = cannonHeat;}


    public void cannonHeatChange(float HeatChange){
        this.cannonHeat += HeatChange;

    }

    public void cannonHeat(String caseName){
        switch (caseName){
            case "shoot" -> cannonHeatChange(bulletSpeed);
            case "cool down with HP > 400" -> cannonHeatChange(-12);
            case "cool down with HP < 400" -> cannonHeatChange(-24);
            
        }
    }

}
