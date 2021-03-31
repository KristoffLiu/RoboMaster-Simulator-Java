package com.kristoff.robomaster_simulator.teams;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.kristoff.robomaster_simulator.core.SimulatorConfiguration;
import com.kristoff.robomaster_simulator.robomasters.modules.Actor;
import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.types.Enemy;
import com.kristoff.robomaster_simulator.robomasters.types.ShanghaiTechMasterIII;
import com.kristoff.robomaster_simulator.systems.Systems;

public class RoboMasters{
    static SimulatorConfiguration config;

    public static Team all       = new Team();
    public static Team teamBlue   = new Team("Blue");
    public static Team teamRed    = new Team("Red");

    public RoboMasters(SimulatorConfiguration simulatorConfiguration){
        config = simulatorConfiguration;
        init();
    }

    public static void init(){
        if(all.size() == 0){
            Team.friend1 = new ShanghaiTechMasterIII(teamBlue,"Blue1");
            Team.friend2 = new ShanghaiTechMasterIII(teamBlue,"Blue2");
            teamBlue.add(Team.friend1);
            teamBlue.add(Team.friend2);
            Enemy lockedEnemy = new Enemy(teamRed,"Red1");
            lockedEnemy.lock();
            teamRed.add(lockedEnemy);
            teamRed.add(new Enemy(teamRed,"Red2"));
            //((ShanghaiTechMasterIII)teamBlue.get(1)).setAsRoamer();
            all.addAll(teamBlue);
            all.addAll(teamRed);
        }
    }

    public void start(){
        initPosition();
        all.forEach(x->{
            x.start();
        });
        teamBlue.start();
    }

    public void initPosition(){
        int i = 0;
        int j = 0;
        for(TextureMapObject textureMapObject : Systems.map.getBirthZones()){
            float halfWidth = textureMapObject.getTextureRegion().getRegionWidth() / 2f;
            float halfHeight = textureMapObject.getTextureRegion().getRegionHeight() / 2f;
            int x = (int)(textureMapObject.getX() + halfWidth);
            int y = (int)(textureMapObject.getY() + halfHeight);
            if(textureMapObject.getProperties().containsKey("blue")){
                RoboMasters.teamBlue.get(i).actor.update(x, y, (float) (Math.PI));
                i ++;
            }
            else if(textureMapObject.getProperties().containsKey("red")){
                RoboMasters.teamRed.get(j).actor.update(x, y, 0f);
                j ++;
            }
        }
    }

    public static void setPosition(String name, int x, int y, float rotation){
        for (RoboMaster roboMaster : RoboMasters.all){
            if(roboMaster.name.equals(name)){
                roboMaster.actor.update(x,y,rotation);
                break;
            }
        }
    }

    public static Actor getPosition(String name){
        for (RoboMaster roboMaster : RoboMasters.all){
            if(roboMaster.name.equals(name)){
                return roboMaster.actor;
            }
        }
        return null;
    }

    public static RoboMaster getRoboMaster(String name){
        for (RoboMaster roboMaster : RoboMasters.all){
            if(roboMaster.name.equals(name)){
                return roboMaster;
            }
        }
        return null;
    }
}
