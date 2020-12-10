package com.kristoff.robomaster_simulator.robomasters.modules.weapons;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.kristoff.robomaster_simulator.robomasters.RoboMaster;

public class Cannon {
    RoboMaster thisRoboMaster;
    public Body body;
    BodyDef bodyDef;

    RevoluteJoint cannonJoint;
    RevoluteJointDef cannonJointDef;

    public Cannon(RoboMaster roboMaster){
        this.thisRoboMaster = roboMaster;
    }

    public void init(float x, float y, World world) {
        PolygonShape roboMasterShape = new PolygonShape();
        roboMasterShape.setAsBox(0.1175f, 0.04f);

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(x - 0.1f, y));

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 1f;
        fixtureDef.density = 1f;
        fixtureDef.shape = roboMasterShape;

        body.createFixture(fixtureDef);

        roboMasterShape.dispose();
        initCannonJoint(x, y, world);
    }

    private void initCannonJoint(float x, float y, World world) {
        cannonJointDef = new RevoluteJointDef();
        cannonJointDef.initialize(thisRoboMaster.mainBody.body, this.body, new Vector2(x, y));
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
}
