package com.kristoff.robomaster_simulator.robomasters.teams;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.kristoff.robomaster_simulator.core.SimulatorConfiguration;
import com.kristoff.robomaster_simulator.robomasters.robomaster.modules.Actor;
import com.kristoff.robomaster_simulator.robomasters.robomaster.modules.LidarObservation;
import com.kristoff.robomaster_simulator.robomasters.robomaster.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.robomaster.types.AlexanderMasterII;
import com.kristoff.robomaster_simulator.systems.Systems;

public class RoboMasters{
    static SimulatorConfiguration config;
    public static LidarObservation.LidarMode lidarMode = LidarObservation.LidarMode.list;

    public static Team all       = new Team();
    public static Team teamBlue   = new Team("Blue");
    public static Team teamRed    = new Team("Red");

    public RoboMasters(SimulatorConfiguration simulatorConfiguration){
        config = simulatorConfiguration;
        init();
    }

    public static void init(){
        if(all.size() == 0){
            teamBlue.add(new AlexanderMasterII(teamBlue,"Blue1"  ));
            teamBlue.add(new AlexanderMasterII(teamBlue,"Blue2"  ));
            teamRed.add(new AlexanderMasterII(teamRed,"Red1"  ));
            teamRed.add(new AlexanderMasterII(teamRed,"Red2"  ));

            all.addAll(teamBlue);
            all.addAll(teamRed);
        }
    }

    public void start(){
        initPosition();
        all.forEach(x->{
            x.start();
        });
        teamBlue.get(0).enemiesObservationSimulator.start();
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
