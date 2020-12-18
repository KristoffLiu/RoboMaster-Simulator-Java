package com.kristoff.robomaster_simulator.systems.robomasters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.kristoff.robomaster_simulator.systems.robomasters.judgement.JudgeModule;
import com.kristoff.robomaster_simulator.systems.robomasters.modules.Dynamics;
import com.kristoff.robomaster_simulator.systems.robomasters.modules.Property;
import com.kristoff.robomaster_simulator.systems.robomasters.modules.RMActor;
import com.kristoff.robomaster_simulator.systems.robomasters.modules.MainBody;
import com.kristoff.robomaster_simulator.systems.robomasters.modules.Observation;
import com.kristoff.robomaster_simulator.systems.matrixsimulation.RoboMasterPointMatrix;
import com.kristoff.robomaster_simulator.systems.robomasters.modules.Weapon;
import com.kristoff.robomaster_simulator.systems.simulators.PhysicalSimulator;
import com.kristoff.robomaster_simulator.utils.VectorHelper;

/***
 * //    private double weight = 17.1;           //Kg
 * //    private int max_forward_speed = 3;      //m/s
 * //    private int max_cross_range_speed = 2;  //m/s
 * //    private float shooting_speed = 6;       //per second
 * //    private float cannon_range = 180;       //degree
 * //    private float bullet_speed = 25;        //m/s
 * //    private int max_carrying_bullet = 300;  //m/s
 */
public abstract class RoboMaster {
    int PIN;
    World physicalWorld;

    //box2d properties

    public RoboMasterList team;

    public Property property;               //基本属性 Basic Property
    public MainBody mainBody;               //主体的2d物理建模
    public Weapon weapon;                   //武器 Weapon
    public RMActor renderer;     //渲染器
    public RoboMasterPointMatrix matrix;    //点阵发生器
    public Observation observation;         //激光雷达Lidar发生器
    public Dynamics dynamics;               //动力系统
    public JudgeModule judgeModule;         //裁判系统


    public RoboMaster(TextureRegion textureRegion, RoboMasterList roboMasterList) {
        this.team = roboMasterList;
        this.team.add(this);

        property = new Property();
        mainBody = new MainBody(this);
        renderer = new RMActor(textureRegion,this);
        weapon = new Weapon(this);
        matrix = new RoboMasterPointMatrix(this);
        observation = new Observation(this);
        judgeModule = new JudgeModule(this);


        shapeRenderer = new ShapeRenderer();
    }

    ShapeRenderer shapeRenderer;

    public Vector2 getLidarPosition() {
        return this.mainBody.body.getPosition();
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
        bd.position.set(this.weapon.body.getPosition().x,this.weapon.body.getPosition().y);

        com.badlogic.gdx.physics.box2d.Body m_bullet = PhysicalSimulator.current.physicalWorld.createBody(bd);
        m_bullet.createFixture(fd);

        //m_bullet.setLinearVelocity(new Vector2(400, 0));
        m_bullet.setLinearVelocity(VectorHelper.getVector(25f,this.getCannonAngle()));
    }

    public float getFacingAngle() {
        return (float) (- this.mainBody.body.getAngle() - Math.PI / 2f);
    }

    public Vector2 getPosition() {
        return this.mainBody.body.getPosition();
    }

    public float getCannonAngle() {
        return (float) (- this.weapon.body.getAngle() - Math.PI / 2f);
    }

    public void moveForward() {
        float force = 3f;
        float worldAngle = getFacingAngle();
        //this.mainBody.body.applyForce(VectorHelper.getVector(force, worldAngle), getPosition(), false);
        //this.mainBody.body.applyLinearImpulse(VectorHelper.getVector(force, worldAngle), getPosition(), false);
        this.mainBody.body.setLinearDamping(0f);
        this.mainBody.body.setLinearVelocity(VectorHelper.getVector(force, worldAngle));
    }

    public void moveLeft() {
        float force = 250f;
        float worldAngle = getFacingAngle();
        this.mainBody.body.applyForce(VectorHelper.getVector(force, worldAngle - (float) (Math.PI / 2f)), getPosition(), false);
    }

    public void moveRight() {
        float force = 250f;
        float worldAngle = getFacingAngle();
        this.mainBody.body.applyForce(VectorHelper.getVector(force, worldAngle + (float) (Math.PI / 2f)), getPosition(), false);
    }

    public void moveBehind() {
        float force = -250f;
        float worldAngle = getFacingAngle();
        this.mainBody.body.applyForce(VectorHelper.getVector(force, worldAngle), getPosition(), false);
    }

    public void move() {
        float force = -50f;
        float worldAngle = getFacingAngle();
        this.mainBody.body.applyForce(VectorHelper.getVector(force, worldAngle), getPosition(), false);
    }

    public void rotate() {
        float force = -50f;
        float worldAngle = getFacingAngle();
        this.mainBody.body.applyForce(VectorHelper.getVector(force, worldAngle), getPosition(), false);
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
        this.mainBody.body.applyForce(force1, getPosition().add(this.property.width / 2f, this.property.height / 2f), false);
        this.mainBody.body.applyForce(force2, getPosition().add(-this.property.width / 2f, this.property.height / 2f), false);
        this.mainBody.body.applyForce(force3, getPosition().add(this.property.width / 2f, -this.property.height / 2f), false);
        this.mainBody.body.applyForce(force4, getPosition().add(-this.property.width / 2f, -this.property.height / 2f), false);
    }

    public void transformRotation(float degree) {
        this.mainBody.body.setTransform(this.mainBody.body.getPosition(), degree);
        this.weapon.body.setTransform(this.weapon.body.getPosition(), degree);
    }

    public Vector2 getLinearVelocity() {
        return this.mainBody.body.getLinearVelocity();
    }

    public float getAngularVelocity() {
        return this.mainBody.body.getAngularVelocity();
    }

    public void simulate() {
        simulateFriction();
    }

    public void simulateFriction() {
        float μ = 0.2f;
        float weight = this.property.mass * 9.81f;
        float friction = -μ * weight / 4f / 10f;
        if (!this.getLinearVelocity().isZero()) {
            float θ = this.getLinearVelocity().angleRad();
            float directionAngle = (float) ((2f * Math.PI - θ) + Math.PI / 2f);
            if (directionAngle > 2 * Math.PI) {
                directionAngle = (float) (directionAngle % (2 * Math.PI));
            }
            this.drive(VectorHelper.getVector(friction, directionAngle),
                    VectorHelper.getVector(friction, directionAngle),
                    VectorHelper.getVector(friction, directionAngle),
                    VectorHelper.getVector(friction, directionAngle));
        }
        if (!(this.getAngularVelocity() == 0f)) {
            this.mainBody.body.setAngularVelocity(this.getAngularVelocity() + (-this.getAngularVelocity() / 5));
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
            this.mainBody.body.setLinearVelocity(new Vector2());
        }
    }

    public void deploy(float x, float y, World physicalWorld){
        mainBody.init(x,y,physicalWorld);
        weapon.init(x,y,physicalWorld);
    }
}
