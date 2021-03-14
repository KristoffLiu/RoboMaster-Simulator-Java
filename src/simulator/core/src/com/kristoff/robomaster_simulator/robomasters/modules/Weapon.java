package com.kristoff.robomaster_simulator.robomasters.modules;

import com.badlogic.gdx.physics.box2d.*;
import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.systems.simulators.PhysicalSimulator;
import com.kristoff.robomaster_simulator.utils.VectorHelper;

public class Weapon {
    RoboMaster thisRoboMaster;

    public Weapon(RoboMaster roboMaster){
        this.thisRoboMaster = roboMaster;
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
        bd.position.set(this.thisRoboMaster.RMPhysicalSimulation.cannonBody.getPosition().x,this.thisRoboMaster.RMPhysicalSimulation.cannonBody.getPosition().y);

        com.badlogic.gdx.physics.box2d.Body m_bullet = PhysicalSimulator.current.physicalWorld.createBody(bd);
        m_bullet.createFixture(fd);

        //m_bullet.setLinearVelocity(new Vector2(400, 0));
        m_bullet.setLinearVelocity(VectorHelper.getVector(25f,this.thisRoboMaster.getCannonAngle()));
    }
}
