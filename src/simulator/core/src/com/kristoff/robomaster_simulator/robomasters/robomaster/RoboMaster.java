package com.kristoff.robomaster_simulator.robomasters.robomaster;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.kristoff.robomaster_simulator.envs.Simulator;
import com.kristoff.robomaster_simulator.robomasters.robomaster.strategies.PathPlanning;
import com.kristoff.robomaster_simulator.robomasters.teams.Team;
import com.kristoff.robomaster_simulator.robomasters.teams.RoboMasters;
import com.kristoff.robomaster_simulator.systems.matrixsimulation.MatrixSimulator;
import com.kristoff.robomaster_simulator.robomasters.robomaster.judgement.JudgeModule;
import com.kristoff.robomaster_simulator.robomasters.robomaster.modules.*;
import com.kristoff.robomaster_simulator.robomasters.robomaster.modules.enemyobservations.EnemiesObservationSimulator;
import com.kristoff.robomaster_simulator.systems.matrixsimulation.RoboMasterPoint;
import com.kristoff.robomaster_simulator.utils.Position;

import java.util.List;

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

    public Team team;

    public Property                         property;                       //基本属性 Basic Property
    public Actor                            actor;                          //行为器
    public RMPhysicalSimulation             RMPhysicalSimulation;                       //主体的2d物理建模
    public Weapon                           weapon;                         //武器 Weapon
    public Renderer                         renderer;                       //渲染器
    public LidarObservation                 lidarObservation;               //激光雷达Lidar发生器
    public Dynamics                         dynamics;                       //动力系统
    public JudgeModule                      judgeModule;                    //裁判系统
    public EnemiesObservationSimulator      enemiesObservationSimulator;    //敌军视野模拟
    public PathPlanning pathPlanning;

    public String name;
    public int No;
    public MatrixSimulator.MatrixPointStatus pointStatus;


    public RoboMaster(String textureRegionPath, Team team, String name) {
        this.team = team;
        this.name = name;
        if(this.team == RoboMasters.teamBlue){
            No = RoboMasters.teamBlue.size();
        }
        else {
            No = 2 + RoboMasters.teamRed.size();
        }
        switch (this.No){
            case 0 -> {
                pointStatus = MatrixSimulator.MatrixPointStatus.Blue1;
            }
            case 1 -> {
                pointStatus = MatrixSimulator.MatrixPointStatus.Blue2;
            }
            case 2 -> {
                pointStatus = MatrixSimulator.MatrixPointStatus.Red1;
            }
            case 3 -> {
                pointStatus = MatrixSimulator.MatrixPointStatus.Red2;
            }
        }

        property = new Property();
        actor = new Actor(this);
        renderer = new Renderer(textureRegionPath,this);
        weapon = new Weapon(this);
        judgeModule = new JudgeModule(this);
        lidarObservation = new LidarObservation(this);


        enemiesObservationSimulator = new EnemiesObservationSimulator(this);
        pathPlanning = new PathPlanning(this.enemiesObservationSimulator.matrix, this);


        switch (Simulator.current.config.mode){
            case simulator,simulatorRLlib ->{
                RMPhysicalSimulation = new RMPhysicalSimulation(this);
                dynamics = new Dynamics(this);
            }
            case realMachine -> {
            }
        }
    }

    public void start(){
        switch (Simulator.current.config.mode){
            case realMachine -> {

            }
            case simulator, simulatorRLlib -> {
                this.RMPhysicalSimulation.start();
                this.dynamics.start();
            }
        }
        this.lidarObservation.start();
        this.renderer.start();
        this.actor.startToFormMatrix();
    }

    public Position getPosition() {
        return new Position(this.actor.x,this.actor.y);
    }
    public int getX() {
        return this.actor.x;
    }
    public int getY() {
        return this.actor.y;
    }
    public float getRotation() {
        return this.actor.rotation;
    }

    public void setPosition(int x, int y) {
        this.actor.update(x, y);
    }

    public void setPosition(int x, int y, float rotation) {
        this.actor.update(x, y, rotation);
    }

    public Position getLidarPosition() {
        return getPosition();
    }

    public float getFacingAngle() {
        return this.actor.getFacingAngle();
    }

    public float getCannonAngle() {
        return this.actor.getCannonAngle();
    }

    public int[][] getEnemiesObservationSimulationResult(){
        return this.enemiesObservationSimulator.matrix;
    }

    public List<RoboMasterPoint> getLidarObservation(){
        return this.lidarObservation.other;
    }

    public Vector2 getLinearVelocity() {
        return this.RMPhysicalSimulation.body.getLinearVelocity();
    }

    public float getAngularVelocity() {
        return this.RMPhysicalSimulation.body.getAngularVelocity();
    }

    public Position getPointAvoidingFacingEnemies(){
        return pathPlanning.getPointAvoidingFacingEnemies();
    }
    public Position getPointToTheSafeZone(){
        return pathPlanning.getPointToTheSafeZone();
    }

//    public void move2() {
//        //this.body.setLinearVelocity(VectorHelper.getForce());
//    }

//    public void drive(Vector2 force1) {
//        drive(force1,
//                force1,
//                force1,
//                force1);
//    }
//
//    public void drive(Vector2 force1,
//                      Vector2 force2,
//                      Vector2 force3,
//                      Vector2 force4) {
////        this.RMPhysicalSimulation.body.applyForce(force1, getPosition().add(this.property.width / 2f, this.property.height / 2f), false);
////        this.RMPhysicalSimulation.body.applyForce(force2, getPosition().add(-this.property.width / 2f, this.property.height / 2f), false);
////        this.RMPhysicalSimulation.body.applyForce(force3, getPosition().add(this.property.width / 2f, -this.property.height / 2f), false);
////        this.RMPhysicalSimulation.body.applyForce(force4, getPosition().add(-this.property.width / 2f, -this.property.height / 2f), false);
//    }


    //    public void simulate() {
//        simulateFriction();
//    }
//
//    public void simulateFriction() {
//        float μ = 0.2f;
//        float weight = this.property.mass * 9.81f;
//        float friction = -μ * weight / 4f / 10f;
//        if (!this.getLinearVelocity().isZero()) {
//            float θ = this.getLinearVelocity().angleRad();
//            float directionAngle = (float) ((2f * Math.PI - θ) + Math.PI / 2f);
//            if (directionAngle > 2 * Math.PI) {
//                directionAngle = (float) (directionAngle % (2 * Math.PI));
//            }
//            this.drive(VectorHelper.getVector(friction, directionAngle),
//                    VectorHelper.getVector(friction, directionAngle),
//                    VectorHelper.getVector(friction, directionAngle),
//                    VectorHelper.getVector(friction, directionAngle));
//        }
//        if (!(this.getAngularVelocity() == 0f)) {
//            this.RMPhysicalSimulation.body.setAngularVelocity(this.getAngularVelocity() + (-this.getAngularVelocity() / 5));
//        }
//    }


//    public void transformRotation(float degree) {
//        this.RMPhysicalSimulation.body.setTransform(this.RMPhysicalSimulation.body.getPosition(), degree);
//        this.RMPhysicalSimulation.cannonBody.setTransform(this.RMPhysicalSimulation.cannonBody.getPosition(), degree);
//    }
}
