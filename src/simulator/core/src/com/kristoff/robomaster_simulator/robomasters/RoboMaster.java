package com.kristoff.robomaster_simulator.robomasters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.*;
import com.kristoff.robomaster_simulator.utils.VectorHelper;
import com.kristoff.robomaster_simulator.view.base.actors.MovingObject;

public abstract class RoboMaster extends MovingObject {
    int PIN;
    World physicalWorld;

    //physical properties
    public final float mass = 17.1f; //unit: kg
    public final float width = 0.60f; //unit: m
    public final float height = 0.45f; //unit: m
    public final float area = width * height; //unit: metre square

    //box2d properties
    Body body;
    BodyDef bodyDef;

    Body cannon;
    BodyDef cannonDef;

    RevoluteJoint cannonJoint;
    RevoluteJointDef cannonJointDef;

    ElectronicSpeedController ESC;

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

    //    private double weight = 17.1;           //Kg
    //    private int max_forward_speed = 3;      //m/s
    //    private int max_cross_range_speed = 2;  //m/s
    //    private float shooting_speed = 6;       //per second
    //    private float cannon_range = 180;       //degree
    //    private float bullet_speed = 25;        //m/s
    //    private int max_carrying_bullet = 300;  //m/s

    //  roboMasterShape.setAsBox(0.28f,0.215f); FOR ROBOMASTER 2019
    public void createRoboMasterBody(float x, float y, World world) {
        physicalWorld = world;
        PolygonShape roboMasterShape = new PolygonShape();

        roboMasterShape.setAsBox(width / 2f, height / 2f);

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(x, y));


        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 1f;
        fixtureDef.density = mass / area;
        fixtureDef.shape = roboMasterShape;
        fixtureDef.restitution = 1.0f;

        body.createFixture(fixtureDef);

        roboMasterShape.dispose();
        createGunStructure(x, y, world);
    }

    public void createGunStructure(float x, float y, World world) {
        PolygonShape roboMasterShape = new PolygonShape();
        roboMasterShape.setAsBox(0.1175f, 0.04f);

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


    public void connectComponentsWithJoint(float x, float y, World world) {
        cannonJointDef = new RevoluteJointDef();
        cannonJointDef.initialize(body, cannon, new Vector2(x, y));
        cannonJointDef.maxMotorTorque = 50f;
        cannonJointDef.enableMotor = true;
        cannonJointDef.lowerAngle = (float) (-Math.PI / 2);
        cannonJointDef.upperAngle = (float) (Math.PI / 2);
        cannonJointDef.enableLimit = true;
        cannonJoint = (RevoluteJoint) world.createJoint(cannonJointDef);
    }

    public void cannonRotateCW() {
        cannonJoint.setMotorSpeed(-4f);
        //cannon.applyForce(Force.getForce(10f,cannon.getAngle()),cannon.getPosition(),false);
    }

    public void cannonRotateCCW() {
        cannonJoint.setMotorSpeed(4f);
        //cannon.applyForce(Force.getForce(-10f,cannon.getAngle()),cannon.getPosition(),false);
    }

    ShapeRenderer shapeRenderer;

    public void act() {
        float scale = 1f / 1000f;
        this.setWidth(0.6f);
        this.setHeight(0.45f);
        if (this.bodyDef != null) {
            this.setX(this.body.getPosition().x - this.getWidth() / 2f);
            this.setY(this.body.getPosition().y - this.getHeight() / 2f);
            this.setOriginX(this.getWidth() / 2f);
            this.setOriginY(this.getHeight() / 2f);
            this.setRotation(MathUtils.radiansToDegrees * this.body.getAngle());
        }
    }

    public Vector2 getLidarPosition() {
        return body.getPosition();
    }

    public void shoot() {
        CircleShape bulletShape = new CircleShape();
        bulletShape.setRadius(0.02f);
        //bulletShape.setRadius(0.0085f);

        FixtureDef fd = new FixtureDef();
        fd.shape = bulletShape;
        fd.density = 0.000000001f;
        fd.restitution = 0.1f;
        fd.friction = 0.8f;

        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.DynamicBody;
        bd.bullet = true;// 精确检测
        bd.position.set(this.cannon.getPosition().x,this.cannon.getPosition().y);

        Body m_bullet = physicalWorld.createBody(bd);
        m_bullet.createFixture(fd);

        //m_bullet.setLinearVelocity(new Vector2(400, 0));
        m_bullet.setLinearVelocity(VectorHelper.getVector(25f,this.getCannonAngle()));
    }

    public float getFacingAngle() {
        return (float) (-body.getAngle() - Math.PI / 2f);
    }

    public Vector2 getPosition() {
        return this.body.getPosition();
    }

    public float getCannonAngle() {
        return (float) (-cannon.getAngle() - Math.PI / 2f);
    }

    public void moveForward() {
        float force = 250f;
        float worldAngle = getFacingAngle();
        body.applyForce(VectorHelper.getVector(force, worldAngle), getPosition(), false);
    }

    public void moveLeft() {
        float force = 250f;
        float worldAngle = getFacingAngle();
        body.applyForce(VectorHelper.getVector(force, worldAngle - (float) (Math.PI / 2f)), getPosition(), false);
    }

    public void moveRight() {
        float force = 250f;
        float worldAngle = getFacingAngle();
        body.applyForce(VectorHelper.getVector(force, worldAngle + (float) (Math.PI / 2f)), getPosition(), false);
    }

    public void moveBehind() {
        float force = -250f;
        float worldAngle = getFacingAngle();
        body.applyForce(VectorHelper.getVector(force, worldAngle), getPosition(), false);
    }

    public void move() {
        float force = -50f;
        float worldAngle = getFacingAngle();
        body.applyForce(VectorHelper.getVector(force, worldAngle), getPosition(), false);
    }

    public void rotate() {
        float force = -50f;
        float worldAngle = getFacingAngle();
        body.applyForce(VectorHelper.getVector(force, worldAngle), getPosition(), false);
    }

    public void drive(Vector2 force1) {
        drive(force1,
                force1,
                force1,
                force1);
    }

    public void drive(Vector2 force1,
                      Vector2 force2,
                      Vector2 force3,
                      Vector2 force4) {
        body.applyForce(force1, getPosition().add(width / 2f, height / 2f), false);
        body.applyForce(force2, getPosition().add(-width / 2f, height / 2f), false);
        body.applyForce(force3, getPosition().add(width / 2f, -height / 2f), false);
        body.applyForce(force4, getPosition().add(-width / 2f, -height / 2f), false);
    }

    public void transformRotation(float degree) {
        body.setTransform(body.getPosition(), degree);
        cannon.setTransform(cannon.getPosition(), degree);
    }

    public Vector2 getLinearVelocity() {
        return this.body.getLinearVelocity();
    }

    public float getAngularVelocity() {
        return this.body.getAngularVelocity();
    }

    public void simulate() {
        simulateFriction();
    }

    public void simulateFriction() {
        float μ = 0.2f;
        float weight = mass * 9.81f;
        float friction = -μ * weight / 4f / 10f;
        if (!this.getLinearVelocity().isZero()) {
            float θ = this.getLinearVelocity().angleRad();
            float directionAngle = (float) ((2f * Math.PI - θ) + Math.PI / 2f);
            if (directionAngle > 2 * Math.PI) {
                directionAngle = (float) (directionAngle % (2 * Math.PI));
            }
            Gdx.app.log("", String.valueOf(directionAngle));
            this.drive(VectorHelper.getVector(friction, directionAngle),
                    VectorHelper.getVector(friction, directionAngle),
                    VectorHelper.getVector(friction, directionAngle),
                    VectorHelper.getVector(friction, directionAngle));
        }
        if (!(this.getAngularVelocity() == 0f)) {
            this.body.setAngularVelocity(this.getAngularVelocity() + (-this.getAngularVelocity() / 5));
        }
    }

    public void move2() {
        //this.body.setLinearVelocity(VectorHelper.getForce());
    }

    public void slowDown() {
        if (!this.getLinearVelocity().isZero()) {
            float θ = this.getLinearVelocity().angleRad();
            float directionAngle = (float) ((2f * Math.PI - θ) + Math.PI / 2f);
            if (directionAngle > 2 * Math.PI) {
                directionAngle = (float) (directionAngle % (2 * Math.PI));
            }
            Gdx.app.log("", String.valueOf(directionAngle));
            this.body.setLinearVelocity(new Vector2());
        }
    }
}
