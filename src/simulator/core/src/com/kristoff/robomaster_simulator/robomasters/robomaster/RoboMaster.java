package com.kristoff.robomaster_simulator.robomasters.robomaster;

import com.badlogic.gdx.math.Vector2;
import com.kristoff.robomaster_simulator.core.Simulator;
import com.kristoff.robomaster_simulator.robomasters.teams.Team;
import com.kristoff.robomaster_simulator.robomasters.teams.RoboMasters;
import com.kristoff.robomaster_simulator.systems.pointsimulator.PointSimulator;
import com.kristoff.robomaster_simulator.robomasters.robomaster.judgement.JudgeModule;
import com.kristoff.robomaster_simulator.robomasters.robomaster.modules.*;
import com.kristoff.robomaster_simulator.systems.pointsimulator.StatusPoint;
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
    public int PIN;                                             //PIN
    public Team team;                                           //归属队伍 team

    public Property                  property;                  //基本属性 Basic Property
    public Actor                     actor;                     //行为器 Acting System
    public RMPhysicalSimulation      RMPhysicalSimulation;      //主体的2d物理建模 Physical Modelling
    public Weapon                    weapon;                    //武器 Weapon
    public Renderer                  renderer;                  //渲染器 Renderer
    public LidarObservation          lidarObservation;          //激光雷达Lidar发生器 Lidar Sensor Simulator
    public Dynamics                  dynamics;                  //动力系统 Dynamic System
    public JudgeModule               judgeModule;               //裁判系统 Global Judge System
    public TacticMaker               tacticMaker;               //决策器 Decision System

    public String name;
    public int No;
    public PointSimulator.PointStatus pointStatus;

    /***
     * Constructor 构造器
     * @param textureRegionPath File path of the texture
     * @param team              Team
     * @param name              Name
     */
    public RoboMaster(String textureRegionPath, Team team, String name) {
        this.team = team;
        this.name = name;
        No = this.team == RoboMasters.teamBlue? RoboMasters.teamBlue.size() : (2 + RoboMasters.teamRed.size());

        switch (this.No){
            case 0 -> pointStatus = PointSimulator.PointStatus.Blue1;
            case 1 -> pointStatus = PointSimulator.PointStatus.Blue2;
            case 2 -> pointStatus = PointSimulator.PointStatus.Red1;
            case 3 -> pointStatus = PointSimulator.PointStatus.Red2;
        }

        property = new Property();
        actor = new Actor(this);
        renderer = new Renderer(textureRegionPath,this);
        weapon = new Weapon(this);
        judgeModule = new JudgeModule(this);
        lidarObservation = new LidarObservation(this);

        if(this.team == RoboMasters.teamBlue){
            tacticMaker = new TacticMaker(this);
        }

        //pathPlanning = new PathPlanning(this.enemiesObservationSimulator.matrix, this);

        switch (Simulator.current.config.mode){
            case simulator,simulatorRLlib ->{
                RMPhysicalSimulation = new RMPhysicalSimulation(this);
                dynamics = new Dynamics(this);
            }
            case realMachine -> {}
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
        if(this.name.contains("Blue1")){

        }
        this.lidarObservation.start();
        this.renderer.start();
        this.actor.startToFormMatrix();
    }

    //API
    public float getRotation() {
        return this.actor.rotation;
    }
    public void setPosition(int x, int y) {
        this.actor.update(x, y);
    }
    public void setPosition(int x, int y, float rotation) {
        this.actor.update(x, y, rotation);
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


    public Position getLidarPosition() {
        return getPosition();
    }

    public float getFacingAngle() {
        return this.actor.getFacingAngle();
    }

    public float getCannonAngle() {
        return this.actor.getCannonAngle();
    }

    public List<StatusPoint> getLidarObservation(){
        return this.lidarObservation.others;
    }

    public Vector2 getLinearVelocity() {
        return this.RMPhysicalSimulation.body.getLinearVelocity();
    }

    public float getAngularVelocity() {
        return this.RMPhysicalSimulation.body.getAngularVelocity();
    }

    public boolean isAlive(){
        return true;
    }

    public Position getDecisionMade(){
        return this.tacticMaker.getDecisionMade();
    }
}
