package com.kristoff.robomaster_simulator.systems.robomasters.modules.renderer;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.view.base.actors.MovingObject;

public class RoboMasterActor extends MovingObject {
    RoboMaster thisRoboMaster;

    public RoboMasterActor(TextureRegion textureRegion, RoboMaster roboMaster) {
        super(textureRegion);
        this.thisRoboMaster = roboMaster;
    }

    @Override
    public void act(float delta) {
        float scale = 1f / 1000f;
        this.setWidth(0.6f);
        this.setHeight(0.45f);
        if (this.thisRoboMaster.mainBody.body != null) {
            this.setX(this.thisRoboMaster.mainBody.body.getPosition().x - this.getWidth() / 2f);
            this.setY(this.thisRoboMaster.mainBody.body.getPosition().y - this.getHeight() / 2f);
            this.setOriginX(this.getWidth() / 2f);
            this.setOriginY(this.getHeight() / 2f);
            this.setRotation(MathUtils.radiansToDegrees * this.thisRoboMaster.mainBody.body.getAngle());
        }
    }
}
