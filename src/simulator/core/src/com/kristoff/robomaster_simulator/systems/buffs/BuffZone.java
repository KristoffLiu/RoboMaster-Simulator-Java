package com.kristoff.robomaster_simulator.systems.buffs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.types.Enemy;
import com.kristoff.robomaster_simulator.robomasters.types.ShanghaiTechMasterIII;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.teams.Team;
import com.kristoff.robomaster_simulator.utils.Position;
import com.kristoff.robomaster_simulator.view.actors.CustomActor;
import com.kristoff.robomaster_simulator.view.ui.controls.Image;
import com.kristoff.robomaster_simulator.view.ui.controls.UIElement;

public class BuffZone {
    Buff buff;
    boolean isActive;
    String name;
    CustomActor actor;
    Image buffImage;
    TextureMapObject textureMapObject;
    Position centrePosition;


    public static int isHPRecoveryNeeded = 0;
    public static int isBulletSupplyNeeded = 0;

    static BuffZone NotActivated    ;
    static BuffZone RedHPRecovery   ;
    static BuffZone DisableShooting ;
    static BuffZone BlueBulletSupply;
    static BuffZone BlueHPRecovery  ;
    static BuffZone DisableMovement ;
    static BuffZone RedBulletSupply ;

    public BuffZone(TextureMapObject textureMapObject){
        this.name = textureMapObject.getName();
        this.isActive = false;
        buffImage = new Image();
        updateBuff(Buff.NotActivated);
        this.textureMapObject = textureMapObject;
        float scale = 1f / 1000f;
        this.actor = new CustomActor(textureMapObject.getTextureRegion());
        actor.setX(textureMapObject.getX() * scale);
        actor.setY(textureMapObject.getY() * scale);
        actor.setWidth(textureMapObject.getTextureRegion().getRegionWidth() * scale);
        actor.setHeight(textureMapObject.getTextureRegion().getRegionHeight() * scale);
        centrePosition = new Position(
                (int)((textureMapObject.getX() + textureMapObject.getTextureRegion().getRegionWidth() / 2f) / 10f),
                (int)((textureMapObject.getY() + textureMapObject.getTextureRegion().getRegionHeight() / 2f) / 10f));

        buffImage.setScale(0.006f);
        buffImage.setRelativePosition(
                (textureMapObject.getX() + 75f) * scale,
                (textureMapObject.getY() + 40f) * scale,
                UIElement.HorizontalAlignment.LEFT_ALIGNMENT, UIElement.VerticalAlignment.BOTTOM_ALIGNMENT);
    }

    public void updateBuff(Buff buff){
        this.buff = buff;
        String pathHeader = "Systems/BuffZones/";
        switch (buff){
            case NotActivated      -> pathHeader += "NotActivated.png";
            case RedHPRecovery     -> pathHeader += "HealingRed.png";
            case DisableShooting   -> pathHeader += "ShootingForbidden.png";
            case BlueBulletSupply  -> pathHeader += "BulletSupplyBlue.png";
            case BlueHPRecovery    -> pathHeader += "HealingBlue.png";
            case DisableMovement   -> pathHeader += "MovementForbidden.png";
            case RedBulletSupply   -> pathHeader += "BulletSupplyRed.png";
        }
        String finalPathHeader = pathHeader;
        Gdx.app.postRunnable(new Runnable()
        {
            @Override
            public void run()
            {
                buffImage.setTextureRegion(finalPathHeader);
            }
        });
    }

    public String getName(){
        return this.name;
    }

    public Buff getBuff(){
        return this.buff;
    }

    public CustomActor getBuffZoneActor() {
        return this.actor;
    }

    public Image getBuffImage() {
        return this.buffImage;
    }

    public static boolean isInDebuffZone(int x, int y){
        for(BuffZone buffZone : Systems.refree.getBuffZones()){
            if(buffZone.getBuff() != Buff.NotActivated &&
                    buffZone.getBuff() != Buff.BlueBulletSupply &&
                buffZone.getBuff() != Buff.BlueHPRecovery){
                if(isInBuffZone(x, y, buffZone)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isInBuffZone(int x, int y){
        Rectangle bounds = this.getBuffZoneActor().getBounds();
        bounds.x -= 0.25f;
        bounds.y -= 0.25f;
        bounds.width += 0.5f;
        bounds.height += 0.5f;
        return bounds.contains(x / 100f, y / 100f);
    }

    public static int costOfBuff(int x, int y, ShanghaiTechMasterIII roboMaster){
        int cost = 0;

        for(BuffZone buffZone : Systems.refree.getBuffZones()){
            if(buffZone.buff == Buff.BlueHPRecovery && !isHPRecoveryNeeded(roboMaster)){
                continue;
            }
            else if(buffZone.buff == Buff.BlueBulletSupply && !isBulletSupplyNeeded(roboMaster)){
                continue;
            }
            if(buffZone.isInBuffZone(x, y)){
                switch (buffZone.buff){
                    case NotActivated     -> cost = 0;
                    case RedHPRecovery    -> {
                        if(!buffZone.isActive){
                            if(roboMaster == Team.blue2 &&
                                    Enemy.getLockedEnemy().getHealth() >= 1900 &&
                                    Enemy.getUnlockedEnemy().getHealth() >= 1900)
                                cost = -100;
                            else{
                                cost = 999;
                            }
                        }
                        else {
                            cost = 0;
                        }
                    }
                    case RedBulletSupply  ->
                            cost = buffZone.isActive ? 0 : 999;
                    case BlueHPRecovery   ->
                            cost = buffZone.isActive ? 0 : -100;
                    case BlueBulletSupply ->
                            cost = buffZone.isActive ? 0 : -100;
                    case DisableShooting  -> cost = buffZone.isActive ? 0 : 999;
                    case DisableMovement  -> cost = buffZone.isActive ? 0 : 999;
                }
                return cost;
            }
            else{
                float distance = buffZone.centrePosition.distanceTo(x,y);
                float maxDis = 150;
                if(distance <= maxDis){
                    switch (buffZone.buff){
                        case NotActivated     -> cost += 0;
                        case RedHPRecovery    -> cost += 0;
                        case RedBulletSupply  -> cost += 0;
                        case BlueHPRecovery   -> cost += (maxDis - distance) / maxDis * -100;
                        case BlueBulletSupply -> cost += (maxDis - distance) / maxDis * -100;
                        case DisableShooting  -> cost += 0;
                        case DisableMovement  -> cost += 0;
                    }
                }
            }
        }
        return cost;
    }

    public boolean isBulletSupplyNeeded(RoboMaster roboMaster){
        return true;
    }

    public static boolean isHPRecoveryNeeded(ShanghaiTechMasterIII roboMaster){
        if(roboMaster == Team.blue1){
            return isHPRecoveryNeeded == 1;
        }
        else {
            return isHPRecoveryNeeded == 2;
        }
    }

    public static void setHPRecoveryNeeded(){
        float distanceToBlue1 = getBlueHPRecovery().centrePosition.distanceTo(Team.blue1.getPointPosition());
        float distanceToBlue2 = getBlueHPRecovery().centrePosition.distanceTo(Team.blue2.getPointPosition());
        if(distanceToBlue1 > distanceToBlue2){
            isHPRecoveryNeeded = 1;
        }
        else {
            isHPRecoveryNeeded = 2;
        }
    }

    public static boolean isBulletSupplyNeeded(ShanghaiTechMasterIII roboMaster){
        if(roboMaster == Team.blue1){
            return isBulletSupplyNeeded == 1;
        }
        else {
            return isBulletSupplyNeeded == 2;
        }
    }

    public static void setBulletSupplyNeeded(){
        float distanceToBlue1 = getBlueBulletSupply().centrePosition.distanceTo(Team.blue1.getPointPosition());
        float distanceToBlue2 = getBlueBulletSupply().centrePosition.distanceTo(Team.blue2.getPointPosition());
        if(distanceToBlue1 > distanceToBlue2){
            isBulletSupplyNeeded = 1;
        }
        else {
            isBulletSupplyNeeded = 2;
        }
    }

    public static boolean isInBuffZone(int x, int y, BuffZone buffZone){
        return buffZone.getBuffZoneActor().getBounds().contains(x / 100f, y / 100f);
    }

    public static void updateBuffZone(int buffZoneNo, int buffType, boolean isActive){
        for(BuffZone buffZone : Systems.refree.getBuffZones()){
            if(buffZone.getName().equals("F" + (buffZoneNo + 1))){
                switch (buffType){
                    case 0 -> {
                        buffZone.updateBuff(Buff.NotActivated);
                        NotActivated = buffZone;
                    }
                    case 1 -> {
                        buffZone.updateBuff(Buff.RedHPRecovery);
                        RedHPRecovery = buffZone;
                    }
                    case 2 -> {
                        buffZone.updateBuff(Buff.RedBulletSupply);
                        RedBulletSupply = buffZone;
                    }
                    case 3 -> {
                        buffZone.updateBuff(Buff.BlueHPRecovery);
                        BlueHPRecovery = buffZone;
                    }
                    case 4 -> {
                        buffZone.updateBuff(Buff.BlueBulletSupply);
                        BlueHPRecovery = buffZone;
                    }
                    case 5 -> {
                        buffZone.updateBuff(Buff.DisableShooting);
                        DisableShooting = buffZone;
                    }
                    case 6 -> {
                        buffZone.updateBuff(Buff.DisableMovement);
                        DisableMovement = buffZone;
                    }
                }
                buffZone.isActive = isActive;
                break;
            }
        }
    }

    public static BuffZone getNotActivated(){
        return NotActivated;
    }
    public static BuffZone getRedHPRecovery(){
        return RedHPRecovery;
    }
    public static BuffZone getDisableShooting(){
        return DisableShooting;
    }
    public static BuffZone getBlueBulletSupply(){
        return BlueBulletSupply;
    }
    public static BuffZone getBlueHPRecovery(){
        return BlueHPRecovery;
    }
    public static BuffZone getDisableMovement(){
        return DisableMovement;
    }
    public static BuffZone getRedBulletSupply(){
        return RedBulletSupply;
    }
}
