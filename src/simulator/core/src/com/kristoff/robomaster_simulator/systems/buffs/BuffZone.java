package com.kristoff.robomaster_simulator.systems.buffs;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.kristoff.robomaster_simulator.view.actors.CustomActor;

public class Buff {
    BuffType type;
    CustomActor actor;
    TextureMapObject textureMapObject;

    public Buff(TextureMapObject textureMapObject){
        this.type = BuffType.NotActivated;
        this.textureMapObject = textureMapObject;
        float scale = 1f / 1000f;
        this.actor = new CustomActor(textureMapObject.getTextureRegion());
        actor.setX(textureMapObject.getX() * scale);
        actor.setY(textureMapObject.getY() * scale);
        actor.setWidth(textureMapObject.getTextureRegion().getRegionWidth() * scale);
        actor.setHeight(textureMapObject.getTextureRegion().getRegionHeight() * scale);
    }
}
