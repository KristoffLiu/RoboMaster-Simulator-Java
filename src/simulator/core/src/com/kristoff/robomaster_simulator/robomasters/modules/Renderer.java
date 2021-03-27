package com.kristoff.robomaster_simulator.robomasters.modules;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.view.actors.MovingObject;

public class Renderer extends MovingObject implements ModuleInterface {
    RoboMaster roboMaster;
    String textureRegionPath;

    public Renderer(String textureRegionPath, RoboMaster roboMaster) {
        super();
        this.textureRegionPath = textureRegionPath;
        this.roboMaster = roboMaster;
    }

    public void start(){
        this.setTextureRegion(new TextureRegion(new Texture(textureRegionPath)));
        this.setWidth(0.6f);
        this.setHeight(0.5f);
    }

    @Override
    public void act(float delta) {
        float scale = 1f / 1000f;
        this.setX(this.roboMaster.actor.x * scale - this.getWidth() / 2f);
        this.setY(this.roboMaster.actor.y * scale - this.getHeight() / 2f);
        this.setOriginX(this.getWidth() / 2f);
        this.setOriginY(this.getHeight() / 2f);
        this.setRotation(MathUtils.radiansToDegrees * this.roboMaster.actor.rotation);
    }
}
