package com.kristoff.robomaster_simulator.robomasters.modules;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.kristoff.robomaster_simulator.utils.LoopThread;
import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.utils.VectorHelper;

public class Dynamics extends LoopThread {
    RoboMaster thisRoboMaster;
    Body thisBody;

    boolean isManuallyControl = false;
    boolean isMoving = false;

    Vector2 currentVelocity = new Vector2();
    float linearDamping = 0.9f;
    float maxLinearSpeed = 3.0f;

    float currentAngularVelocity = 2f;
    float angularDamping = 1.0f;
    float maxAngularSpeed = 3.0f;


    public Dynamics(RoboMaster roboMaster){
        isStep = true;
        delta = 1/5f;
        thisRoboMaster = roboMaster;
    }

    @Override
    public void start(){
        thisBody = thisRoboMaster.RMPhysicalSimulation.body;
        setVelocity();
        super.start();
    }

    public void moveForward() {
        float worldAngle = thisRoboMaster.getFacingAngle();
        currentVelocity = VectorHelper.getVector(maxLinearSpeed, worldAngle);
        isMoving = true;
        setVelocity();
    }

    public void moveLeft() {
        float worldAngle = thisRoboMaster.getFacingAngle();
        currentVelocity = VectorHelper.getVector(maxLinearSpeed, worldAngle - (float) (Math.PI / 2f));
        isMoving = true;
        setVelocity();
    }

    public void moveRight() {
        float worldAngle = thisRoboMaster.getFacingAngle();
        currentVelocity = VectorHelper.getVector(maxLinearSpeed, worldAngle + (float) (Math.PI / 2f));
        isMoving = true;
        setVelocity();
    }

    public void moveBehind() {
        float worldAngle = thisRoboMaster.getFacingAngle();
        currentVelocity = VectorHelper.getVector(- maxLinearSpeed, worldAngle);
        isMoving = true;
        setVelocity();
    }

    public void rotateCW() {
        currentAngularVelocity = -3f;
        setAngularVelocity();
    }

    public void rotateCCW() {
        currentAngularVelocity = 3f;
        setAngularVelocity();
    }

    public void stopMoving() {
        isMoving = false;
        thisBody.setAngularDamping(angularDamping);
        thisBody.setLinearDamping(linearDamping);
        thisBody.setLinearVelocity(new Vector2());
    }

    public void setVelocity(){
        thisBody.setAngularDamping(angularDamping);
        thisBody.setLinearDamping(0f);
        thisBody.setLinearVelocity(currentVelocity);
    }

    public void stopRotating(){
        thisBody.setAngularDamping(angularDamping);
        thisBody.setAngularVelocity(0);
    }

    public void setAngularVelocity(){
        thisBody.setAngularDamping(0f);
        thisBody.setAngularVelocity(currentAngularVelocity);
    }

    public void slowDown() {
        if (!this.thisRoboMaster.getLinearVelocity().isZero()) {
            float θ = this.thisRoboMaster.getLinearVelocity().angleRad();
            float directionAngle = (float) ((2f * Math.PI - θ) + Math.PI / 2f);
            if (directionAngle > 2 * Math.PI) {
                directionAngle = (float) (directionAngle % (2 * Math.PI));
            }
            this.thisRoboMaster.RMPhysicalSimulation.body.setLinearVelocity(new Vector2());
        }
    }

    @Override
    public void step(){

    }
}
