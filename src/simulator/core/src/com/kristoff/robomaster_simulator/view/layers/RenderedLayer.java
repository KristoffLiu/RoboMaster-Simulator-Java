package com.kristoff.robomaster_simulator.view.layers;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.kristoff.robomaster_simulator.view.Renderer;
import com.kristoff.robomaster_simulator.view.base.actors.CustomActor;
import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.view.base.layers.VisualLayer;

public class RenderedLayer extends VisualLayer {

    public RenderedLayer(Renderer renderer) {
        super(renderer);
        renderBirthZone();
        renderBlocks();
        renderBuffZone();
        //renderRoboMasters();
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

    public void renderRoboMasters() {
        for (CustomActor actor : environment.allRoboMasters) {
            this.addActor(actor);
        }
    }

    @Override
    public void act (float delta) {
        float scale = 1f / 1000f;
        super.act(delta);
        for (RoboMaster roboMaster : environment.allRoboMasters) {
            roboMaster.act();
        }
    }

    @Override
    public void draw(){
        super.draw();
    }

}
