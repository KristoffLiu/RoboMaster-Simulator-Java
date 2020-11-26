package com.robomaster_libgdx.environment.robomasters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.robomaster_libgdx.environment.libs.actors.MovingObject;

public abstract class RoboMaster extends MovingObject {
    int PIN;
    //physical properties
    Body body;

    //box2d properties
    BodyDef bodyDef;

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
        shapeRenderer = new ShapeRenderer();
    }

    public void createRoboMasterBody(float x, float y, World world){
        PolygonShape roboMasterShape = new PolygonShape();
        //roboMasterShape.setAsBox(0.28f,0.215f);
        roboMasterShape.setAsBox(0.30f,0.225f);

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(x, y));
        bodyDef.linearVelocity.set(new Vector2(20f,10f));

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 1f;
        fixtureDef.density = 1f;
        fixtureDef.shape = roboMasterShape;
        fixtureDef.restitution = 1.0f;

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

    ShapeRenderer shapeRenderer;

    public void act(){
        float scale = 1f / 1000f;
        this.setWidth(0.6f);
        this.setHeight(0.45f);
        if(this.bodyDef != null){
            this.setX(this.body.getPosition().x - this.getWidth() / 2f);
            this.setY(this.body.getPosition().y - this.getHeight() / 2f);
            this.setOriginX(this.getWidth() / 2f);
            this.setOriginY(this.getHeight() / 2f);
            this.setRotation(MathUtils.radiansToDegrees * this.body.getAngle());
        }
    }

    public Vector2 getLidarPosition(){
        return body.getPosition();
    }



}
