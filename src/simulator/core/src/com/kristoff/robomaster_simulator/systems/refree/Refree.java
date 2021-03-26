package com.kristoff.robomaster_simulator.systems.refree;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.kristoff.robomaster_simulator.robomasters.judgement.BuffZones;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.buffs.BuffZone;
import com.kristoff.robomaster_simulator.utils.LoopThread;
import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.judgement.BuffZoneList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Refree extends LoopThread {
    List<BuffZone> buffZones = new LinkedList<>();

    public Refree(){
        isStep = true;
        delta = 1/30f;
        buffZones = new LinkedList<>();
    }

    @Override
    public void start(){
        for(TextureMapObject textureMapObject : Systems.map.getBuffZones()){
            buffZones.add(new BuffZone(textureMapObject));
        }
        super.start();
    }

    public void test(){
        BuffZone.updateBuffZone(0,1);
        BuffZone.updateBuffZone(1,2);
        BuffZone.updateBuffZone(2,3);
        BuffZone.updateBuffZone(3,4);
        BuffZone.updateBuffZone(4,5);
        BuffZone.updateBuffZone(5,6);
    }

    public List<BuffZone> getBuffZones(){
        return this.buffZones;
    }

    @Override
    public void step(){

    }

    public void initializeBuffZones(BuffZoneList oldZones){
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        Random random = new Random();
        int n1 = random.nextInt(list.size());
        for (com.kristoff.robomaster_simulator.robomasters.judgement.BuffZone.BuffZone zone : oldZones){
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
