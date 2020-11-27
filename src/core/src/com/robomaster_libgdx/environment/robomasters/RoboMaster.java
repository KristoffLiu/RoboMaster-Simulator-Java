package com.robomaster_libgdx.environment.robomasters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.GearJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;
import com.robomaster_libgdx.environment.libs.actors.MovingObject;

public abstract class RoboMaster extends MovingObject {
    int PIN;
    //physical properties
    float mass;
    World physicalWorld;

    //box2d properties
    Body body;
    BodyDef bodyDef;

    Body cannon;
    BodyDef cannonDef;

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
        setMass(17.1f);
    }

    private void setMass(float mass){
        this.mass = mass;
    }

//    private double weight = 17.1;           //Kg
//    private int max_forward_speed = 3;      //m/s
//    private int max_cross_range_speed = 2;  //m/s
//    private float shooting_speed = 6;       //per second
//    private float cannon_range = 180;       //degree
//    private float bullet_speed = 25;        //m/s
//    private int max_carrying_bullet = 300;  //m/s

    public void createRoboMasterBody(float x, float y, World world){
        physicalWorld = world;
        PolygonShape roboMasterShape = new PolygonShape();
        //roboMasterShape.setAsBox(0.28f,0.215f);
        roboMasterShape.setAsBox(0.30f,0.225f);

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(x, y));
        //bodyDef.linearVelocity.set(new Vector2(20f,10f));

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 1f;
        fixtureDef.density = 1000f;
        fixtureDef.shape = roboMasterShape;
        fixtureDef.restitution = 1.0f;
        

        body.createFixture(fixtureDef);

        roboMasterShape.dispose();
        createGunStructure(x, y, world);

        body.applyForce(new Vector2(5000000f,200f), new Vector2(x,y),false);
    }

    public void createGunStructure(float x, float y, World world){
        PolygonShape roboMasterShape = new PolygonShape();
        roboMasterShape.setAsBox(0.1175f,0.04f);

        cannonDef = new BodyDef();
        cannonDef.type = BodyDef.BodyType.DynamicBody;
        cannonDef.position.set(new Vector2(x - 0.1f, y));

        cannon = world.createBody(cannonDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 1f;
        fixtureDef.density = 1f;
        fixtureDef.shape = roboMasterShape;

        cannon.createFixture(fixtureDef);

        roboMasterShape.dispose();
        connectComponentsWithJoint(x, y, world);
    }

    public void connectComponentsWithJoint(float x, float y, World world){
        RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.initialize(body,cannon,new Vector2(x, y));
        revoluteJointDef.maxMotorTorque = 5f;
        revoluteJointDef.motorSpeed = 5f;
        revoluteJointDef.enableMotor = true;
        revoluteJointDef.lowerAngle = 0;
        revoluteJointDef.upperAngle = (float) (Math.PI / 2);
        world.createJoint(revoluteJointDef);
    }

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

    public void shoot(){
        CircleShape bulletShape = new CircleShape();
        bulletShape.setRadius(0.02f);
        //bulletShape.setRadius(0.0085f);

        FixtureDef fd = new FixtureDef();
        fd.shape = bulletShape;
        fd.density = 20.0f;
        fd.restitution = 1.0f;

        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.DynamicBody;
        bd.bullet = true;// 精确检测
        bd.position.set(this.body.getPosition().x, this.body.getPosition().y);

        Body m_bullet = physicalWorld.createBody(bd);
        m_bullet.createFixture(fd);

        //m_bullet.setLinearVelocity(new Vector2(400, 0));
        m_bullet.setLinearVelocity(new Vector2(10f, 10f));
    }

    public float getCannonAngle(){
        return 0f;
    }



}
