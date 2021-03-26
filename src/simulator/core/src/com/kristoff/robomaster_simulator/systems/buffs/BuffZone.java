package com.kristoff.robomaster_simulator.systems.buffs;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.view.actors.CustomActor;
import com.kristoff.robomaster_simulator.view.ui.controls.Image;
import com.kristoff.robomaster_simulator.view.ui.controls.UIElement;

public class BuffZone {
    Buff buff;
    CustomActor actor;
    Image buffImage;
    TextureMapObject textureMapObject;

    public BuffZone(TextureMapObject textureMapObject){
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
            case HealingForRed     -> pathHeader += "HealingRed.png";
            case ShootingForbidden -> pathHeader += "ShootingForbidden.png";
            case BulletSupplyBlue  -> pathHeader += "BulletSupplyBlue.png";
            case HealingForBlue    -> pathHeader += "HealingBlue.png";
            case MovementForbidden -> pathHeader += "MovementForbidden.png";
            case BulletSupplyRed   -> pathHeader += "BulletSupplyRed.png";
        }
        this.buffImage.setTextureRegion(pathHeader);
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
                    buffZone.getBuff() != Buff.BulletSupplyBlue &&
                buffZone.getBuff() != Buff.HealingForBlue ){
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
}
