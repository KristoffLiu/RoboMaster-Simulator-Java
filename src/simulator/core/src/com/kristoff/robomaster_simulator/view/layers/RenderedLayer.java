package com.kristoff.robomaster_simulator.view.layers;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.kristoff.robomaster_simulator.robomasters.RoboMasters;
import com.kristoff.robomaster_simulator.view.Renderer;
import com.kristoff.robomaster_simulator.view.base.actors.CustomActor;
import com.kristoff.robomaster_simulator.robomasters.types.RoboMaster;
import com.kristoff.robomaster_simulator.view.base.layers.VisualLayer;

public class RenderedLayer extends VisualLayer {

    public RenderedLayer(Renderer renderer) {
        super(renderer);
        //addBirthZones();
        //addBlocks();
        //addBuffZones();
        //renderRoboMasters();
    }

    public void addBirthZones(){
        for(TextureMapObject textureMapObject : environment.map.getBirthZones()){
            float scale = 1f / 1000f;
            CustomActor actor = new CustomActor(textureMapObject.getTextureRegion());
            actor.setX(textureMapObject.getX() * scale);
            actor.setY(textureMapObject.getY() * scale);
            actor.setWidth(textureMapObject.getTextureRegion().getRegionWidth() * scale);
            actor.setHeight(textureMapObject.getTextureRegion().getRegionHeight() * scale);
            this.addActor(actor);
        }
    }

    public void addBlocks(){
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

    public void addBuffZones(){
        for(TextureMapObject textureMapObject : environment.map.getBuffZones()){
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
        for (RoboMaster roboMaster : RoboMasters.all) {
            this.addActor(roboMaster.actor);
        }
    }

    @Override
    public void act (float delta) {
        float scale = 1f / 1000f;
        super.act(delta);
    }

    @Override
    public void draw(){
        super.draw();
    }

}
