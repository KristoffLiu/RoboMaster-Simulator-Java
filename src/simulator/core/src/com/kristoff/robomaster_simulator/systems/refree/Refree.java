package com.kristoff.robomaster_simulator.systems.refree;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.buffs.BuffZone;
import com.kristoff.robomaster_simulator.utils.LoopThread;
import com.kristoff.robomaster_simulator.robomasters.RoboMaster;

import java.util.LinkedList;
import java.util.List;

public class Refree extends LoopThread {
    public GameStatus gameStatus;
    public int remainingTime;
    public int buffRoundIndex = 0;
    List<BuffZone> buffZones = new LinkedList<>();

    public Refree(){
        isStep = true;
        delta = 1f;
        buffZones = new LinkedList<>();
    }

    @Override
    public void start(){
        for(TextureMapObject textureMapObject : Systems.map.getBuffZones()){
            buffZones.add(new BuffZone(textureMapObject));
        }
        super.start();
    }



    public void updateGameStatus(int gameStatus){
        switch (gameStatus){
            case 0 -> this.gameStatus = GameStatus.READY;
            case 1 -> this.gameStatus = GameStatus.PREPARATION;
            case 2 -> this.gameStatus = GameStatus.INITIALIZE;
            case 3 -> this.gameStatus = GameStatus.FIVE_SEC_CD;
            case 4 -> this.gameStatus = GameStatus.GAME;
            case 5 -> this.gameStatus = GameStatus.END;
        }
    }

    public void updateRemainTime(int remainingTime){
        this.remainingTime = remainingTime;
        int timePassed = 300 - this.remainingTime;
        buffRoundIndex = timePassed / 60;
    }

    public List<BuffZone> getBuffZones(){
        return this.buffZones;
    }

    @Override
    public void step(){
        BuffZone.setHPRecoveryNeeded();
        BuffZone.setBulletSupplyNeeded();
        BuffZone.setEnemyHPRecoveryNeeded();
        System.out.println(remainingTime);
    }

    public boolean isRoboMasterAlive(RoboMaster Robomaster){
        return Robomaster.property.isAlive;
    }

    public boolean isRoboMasterMoveable(RoboMaster Robomaster){
        return Robomaster.property.movable;
    }

    public boolean isRoboMasterShootable(RoboMaster Robomaster){
        return Robomaster.property.shootable;
    }

    public int bulletLeft(RoboMaster Robomaster){
        return Robomaster.property.numOfBulletsLeft;
    }

    public float RoboMasterHealth(RoboMaster Robomaster){
        return Robomaster.property.health;
    }

}
