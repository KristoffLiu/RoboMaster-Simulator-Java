package com.kristoff.robomaster_simulator.robomasters.modules;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.kristoff.robomaster_simulator.robomasters.RoboMaster;

public class MainBody {
    RoboMaster thisRoboMaster;
    public Body body;
    BodyDef bodyDef;

    public MainBody(RoboMaster roboMaster){
        this.thisRoboMaster = roboMaster;
    }

    //  roboMasterShape.setAsBox(0.28f,0.215f); FOR ROBOMASTER 2019
    public void init(float x, float y, World world) {
        PolygonShape roboMasterShape = new PolygonShape();

        roboMasterShape.setAsBox(
                thisRoboMaster.property.width / 2f,
                thisRoboMaster.property.height / 2f);

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(x, y));
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
    }
}
