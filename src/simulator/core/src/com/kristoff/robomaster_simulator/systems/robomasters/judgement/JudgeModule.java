package com.kristoff.robomaster_simulator.systems.robomasters.judgement;

import com.kristoff.robomaster_simulator.environment.BackendThread;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.systems.robomasters.judgement.BuffZone.BuffZone;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JudgeModule extends BackendThread {
    RoboMaster thisRoboMaster;

    float judgeFrequency = 1/60f;

    float health;
    int bullet;
    boolean isDead;



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

    }

    @Override
    public void step(){

    }

    public void healthChange(float value){
        this.health += value;
    }


    public void initializeBuffZones(BuffZoneList oldZones){
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        Random random = new Random();
        int n1 = random.nextInt(list.size());
        for (BuffZone zone : oldZones){
            if (zone.getZoneID() == n1){
                zone.setBuffCase("BleuAddBullet");
            }else if (zone.getZoneID() == n1 + 3){
                zone.setBuffCase("RedAddBullet");
            }
        }
        list.remove(n1);

        int n2 = random.nextInt(list.size());
        list.remove(n2);
        int n3 = list.get(0);
        list.remove(n3);
        oldZones.forEach(zone->{
            if (zone.getZoneID() == n2){
                zone.setBuffCase("BleuHeal");
            }else if (zone.getZoneID() == n3){
                zone.setBuffCase("ChassisLocked");
            }else if (zone.getZoneID() == n2 + 3){
                zone.setBuffCase("RedHeal");
            }else if (zone.getZoneID() == n3 + 3){
                zone.setBuffCase("NoShooting");
            }
        });
    }

    public void bulletHitDamage(String plateName){
        switch (plateName) {
            case "front" -> healthChange(-20);
            case "right", "left" -> healthChange(-40);
            case "back" -> healthChange(-60);
            case "hit" -> healthChange(-10);
            case "30 > bullet speed > 25" -> healthChange(-200);
            case "35 > bullet speed >= 30" -> healthChange(-1000);
            case "bullet speed >= 35" -> healthChange(-2000);



        }
    }





}
