package com.robomaster_libgdx.environment.actors.robomasters;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.robomaster_libgdx.environment.actors.MovingObject;

public class RoboMaster extends MovingObject {
    int PIN;
    //physical properties
    float physical_width;
    float physical_height;

    //mechanical properties;
    float acceleration;
    float velocity;

    //Competition properties;
    //RoboMasterSate roboMasterState;
    float health;
    int numOfBulletsOwned;
    int numOfBulletsLeft;
    int numOfBulletsShot;

    public RoboMaster(TextureRegion textureRegion) {
        super(textureRegion);
    }


    //

}
