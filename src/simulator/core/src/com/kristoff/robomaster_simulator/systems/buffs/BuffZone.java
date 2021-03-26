package com.kristoff.robomaster_simulator.systems.buffs;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.kristoff.robomaster_simulator.systems.Systems;
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
            case RedHPRecovery -> pathHeader += "HealingRed.png";
            case DisableShooting -> pathHeader += "ShootingForbidden.png";
            case BlueBulletSupply -> pathHeader += "BulletSupplyBlue.png";
            case BlueHPRecovery -> pathHeader += "HealingBlue.png";
            case DisableMovement -> pathHeader += "MovementForbidden.png";
            case RedBulletSupply -> pathHeader += "BulletSupplyRed.png";
        }
        this.buffImage.setTextureRegion(pathHeader);
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

    public static boolean isInBuffZone(int x, int y, BuffZone buffZone){
        return buffZone.getBuffZoneActor().getBounds().contains(x*10, y*10);
    }

    public static void updateBuffZone(int buffZoneNo, int buffType, boolean isActive){
        for(BuffZone buffZone : Systems.refree.getBuffZones()){
            if(buffZone.getName().equals("F" + (buffZoneNo + 1))){
                switch (buffType){
                    case 0 -> buffZone.updateBuff(Buff.NotActivated        );
                    case 1 -> buffZone.updateBuff(Buff.RedHPRecovery       );
                    case 2 -> buffZone.updateBuff(Buff.RedBulletSupply     );
                    case 3 -> buffZone.updateBuff(Buff.BlueHPRecovery      );
                    case 4 -> buffZone.updateBuff(Buff.BlueBulletSupply    );
                    case 5 -> buffZone.updateBuff(Buff.DisableShooting     );
                    case 6 -> buffZone.updateBuff(Buff.DisableMovement      );
                }
                buffZone.isActive = isActive;
                break;
            }
        }
    }
}
