package com.kristoff.robomaster_simulator.robomasters.modules;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.systems.Systems;

public class RMPhysicalSimulation {
    RoboMaster thisRoboMaster;
    public Body body;
    BodyDef bodyDef;
    public Body cannonBody;
    BodyDef cannonBodyDef;

    RevoluteJoint cannonRevoluteJoint;
    RevoluteJointDef cannonRevoluteJointDef;

    public RMPhysicalSimulation(RoboMaster roboMaster){
        this.thisRoboMaster = roboMaster;
    }

    //  roboMasterShape.setAsBox(0.28f,0.215f); FOR ROBOMASTER 2019
    public void start() {
        float x = this.thisRoboMaster.actor.x / 1000f;
        float y = this.thisRoboMaster.actor.y / 1000f;
        World world = Systems.physicalSimulator.physicalWorld;
        PolygonShape roboMasterShape = new PolygonShape();

        roboMasterShape.setAsBox(
                thisRoboMaster.property.width / 2f,
                thisRoboMaster.property.height / 2f);

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(x, y));
        bodyDef.angle = thisRoboMaster.actor.rotation;
        bodyDef.angularDamping = 0.9f;
        bodyDef.linearDamping = 0.9f;

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 1f;
        fixtureDef.density = thisRoboMaster.property.mass / thisRoboMaster.property.area;
        fixtureDef.shape = roboMasterShape;
        fixtureDef.restitution = 0.001f;

        body.createFixture(fixtureDef);

        roboMasterShape.dispose();

        PolygonShape cannonShape = new PolygonShape();
        cannonShape.setAsBox(0.1175f, 0.04f);

        cannonBodyDef = new BodyDef();
        cannonBodyDef.type = BodyDef.BodyType.DynamicBody;
        cannonBodyDef.position.set(new Vector2(x - 0.1f, y));
        cannonBodyDef.angle = this.thisRoboMaster.actor.rotation;

        cannonBody = world.createBody(cannonBodyDef);

        FixtureDef cannonFixtureDef = new FixtureDef();
        cannonFixtureDef.friction = 1f;
        cannonFixtureDef.density = 1f;
        cannonFixtureDef.shape = cannonShape;

        cannonBody.createFixture(cannonFixtureDef);

        cannonShape.dispose();
        initcannonRevoluteJoint(x, y, world);
    }

    private void initcannonRevoluteJoint(float x, float y, World world) {
        cannonRevoluteJointDef = new RevoluteJointDef();
        cannonRevoluteJointDef.initialize(thisRoboMaster.RMPhysicalSimulation.body, this.cannonBody, new Vector2(x, y));
        cannonRevoluteJointDef.maxMotorTorque = 50f;
        cannonRevoluteJointDef.enableMotor = true;
        cannonRevoluteJointDef.lowerAngle = (float) (-Math.PI / 2);
        cannonRevoluteJointDef.upperAngle = (float) (Math.PI / 2);
        cannonRevoluteJointDef.enableLimit = true;
        cannonRevoluteJoint = (RevoluteJoint) world.createJoint(cannonRevoluteJointDef);
    }

    public void cannonRotateCW() {
        cannonRevoluteJoint.setMotorSpeed(-4f);
        //cannon.applyForce(Force.getForce(10f,cannon.getAngle()),cannon.getPosition(),false);
    }

    public void cannonRotateCCW() {
        cannonRevoluteJoint.setMotorSpeed(4f);
        //cannon.applyForce(Force.getForce(-10f,cannon.getAngle()),cannon.getPosition(),false);
    }
}
