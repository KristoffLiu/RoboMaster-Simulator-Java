package com.kristoff.robomaster_simulator.view.layers;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.kristoff.robomaster_simulator.robomasters.teams.RoboMasters;
import com.kristoff.robomaster_simulator.view.renderers.EnvRenderer;
import com.kristoff.robomaster_simulator.view.actors.CustomActor;
import com.kristoff.robomaster_simulator.robomasters.robomaster.RoboMaster;

public class RenderedLayer extends VisualLayer {

    public RenderedLayer(EnvRenderer envRenderer) {
        super(envRenderer);
        addBirthZones();
        addBlocks();
        addBuffZones();
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
            float textureX = textureMapObject.getX();
            float textureY = textureMapObject.getY();
            float textureWidth = textureMapObject.getTextureRegion().getRegionWidth();
            float textureHeight = textureMapObject.getTextureRegion().getRegionHeight();
            float textureRotation = textureMapObject.getRotation();
            float textureRotationInRadian = (float)Math.toRadians(textureRotation);

            CustomActor actor = new CustomActor(textureMapObject.getTextureRegion());
            actor.setX((textureX + textureWidth * (float)Math.sin(textureRotationInRadian)) * scale);
            actor.setY((textureY - textureHeight * (float)Math.sin(textureRotationInRadian)) * scale);
            actor.setRotation(textureRotation);
            actor.setWidth(textureWidth * scale);
            actor.setHeight(textureHeight * scale);
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
