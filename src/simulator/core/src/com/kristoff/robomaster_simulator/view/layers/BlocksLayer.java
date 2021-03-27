package com.kristoff.robomaster_simulator.view.layers;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.view.actors.CustomActor;
import com.kristoff.robomaster_simulator.view.renderers.EnvRenderer;

public class BlocksLayer extends VisualLayer {
    ShapeRenderer shapeRenderer5;
    public BlocksLayer(EnvRenderer envRenderer) {
        super(envRenderer);
        addBlocks();
        shapeRenderer5 = new ShapeRenderer();
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
