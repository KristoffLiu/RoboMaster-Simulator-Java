package com.robomaster_libgdx.environment.simulatinglayers;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.robomaster_libgdx.environment.Environment;
import com.robomaster_libgdx.environment.actors.CustomActor;
import com.robomaster_libgdx.environment.simulatinglayers.baselayers.VisualLayer;

public class RenderedLayer extends VisualLayer {

    public RenderedLayer(Environment environment) {
        super(environment);
        renderBirthZone();
        renderBlocks();
        renderBuffZone();
    }

    public void renderBirthZone(){
        for(TextureMapObject textureMapObject : environment.map.getBirthZone()){
            float scale = 1f / 1000f;
            CustomActor actor = new CustomActor(textureMapObject.getTextureRegion());
            actor.setX(textureMapObject.getX() * scale);
            actor.setY(textureMapObject.getY() * scale);
            actor.setWidth(textureMapObject.getTextureRegion().getRegionWidth() * scale);
            actor.setHeight(textureMapObject.getTextureRegion().getRegionHeight() * scale);
            this.addActor(actor);
        }
    }

    public void renderBlocks(){
        for(TextureMapObject textureMapObject : environment.map.getBlocks()){
            float scale = 1f / 1000f;
            CustomActor actor = new CustomActor(textureMapObject.getTextureRegion());
            actor.setX(textureMapObject.getX() * scale);
            actor.setY(textureMapObject.getY() * scale);
            actor.setWidth(textureMapObject.getTextureRegion().getRegionWidth() * scale);
            actor.setHeight(textureMapObject.getTextureRegion().getRegionHeight() * scale);
            this.addActor(actor);
        }
    }

    public void renderBuffZone(){
        for(TextureMapObject textureMapObject : environment.map.getBuffZone()){
            float scale = 1f / 1000f;
            CustomActor actor = new CustomActor(textureMapObject.getTextureRegion());
            actor.setX(textureMapObject.getX() * scale);
            actor.setY(textureMapObject.getY() * scale);
            actor.setWidth(textureMapObject.getTextureRegion().getRegionWidth() * scale);
            actor.setHeight(textureMapObject.getTextureRegion().getRegionHeight() * scale);
            this.addActor(actor);
        }
    }
}
