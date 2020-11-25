package com.robomaster_libgdx.environment.actors.robomasters;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.robomaster_libgdx.environment.actors.MovingObject;

public abstract class RoboMaster extends MovingObject {
    int PIN;
    //physical properties
    Body body;

    //box2d properties
    BodyDef roboMasterBodyDef;

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

    public void createRoboMasterBody(float x, float y, World world){
        PolygonShape roboMasterShape = new PolygonShape();
        roboMasterShape.setAsBox(0.28f,0.215f);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(x, y));

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 1f;
        fixtureDef.density = 1f;
        fixtureDef.shape = roboMasterShape;

        body.createFixture(fixtureDef);

        roboMasterShape.dispose();
    }

    public void createGunStructure(){
        PolygonShape roboMasterGunShape = new PolygonShape();
        roboMasterGunShape.setAsBox(0.10f,0.02f);

    }

//    public static Body getStaticRoboMasterBody(World world){
//        PolygonShape roboMasterShape = new PolygonShape();
//        roboMasterShape.setAsBox(0.28f,0.215f);
//
//        BodyDef bodyDef = new BodyDef();
//        bodyDef.type = BodyDef.BodyType.DynamicBody;
//        bodyDef.position.set(new Vector2(2f, 2f));
//
//        Body roboMasterBody = world.createBody(bodyDef);
//
//        FixtureDef fixtureDef = new FixtureDef();
//        fixtureDef.friction = 1f;
//        fixtureDef.density = 1f;
//        fixtureDef.shape = roboMasterShape;
//
//        roboMasterBody.createFixture(fixtureDef);
//
//        roboMasterShape.dispose();
//        return roboMasterBody;
//    }



}
