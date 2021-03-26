package com.kristoff.robomaster_simulator.view.layers;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.kristoff.robomaster_simulator.view.renderers.EnvRenderer;
import com.kristoff.robomaster_simulator.view.actors.CustomActor;

public class RenderedLayer extends VisualLayer {

    public RenderedLayer(EnvRenderer envRenderer) {
        super(envRenderer);
        addBirthZones();
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
