package com.kristoff.robomaster_simulator.systems.robomasters.modules;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.kristoff.robomaster_simulator.environment.BackendThread;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.utils.VectorHelper;

public class Dynamics extends BackendThread {
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
        thisBody = roboMaster.mainBody.body;
        setVelocity();
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

    @Override
    public void step(){

    }
}
