package com.kristoff.robomaster_simulator.systems.buffs;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kristoff.robomaster_simulator.view.actors.CustomActor;

public class BuffZone {
    Buff type;
    CustomActor actor;
    TextureMapObject textureMapObject;

    public BuffZone(TextureMapObject textureMapObject){
        this.type = Buff.NotActivated;
        this.textureMapObject = textureMapObject;
        float scale = 1f / 1000f;
        this.actor = new CustomActor(textureMapObject.getTextureRegion());
        actor.setX(textureMapObject.getX() * scale);
        actor.setY(textureMapObject.getY() * scale);
        actor.setWidth(textureMapObject.getTextureRegion().getRegionWidth() * scale);
        actor.setHeight(textureMapObject.getTextureRegion().getRegionHeight() * scale);
    }

    public CustomActor getBuffZoneActor() {
        return this.actor;
    }
}
