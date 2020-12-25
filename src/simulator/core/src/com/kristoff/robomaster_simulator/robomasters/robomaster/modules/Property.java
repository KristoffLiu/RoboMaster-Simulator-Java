package com.kristoff.robomaster_simulator.robomasters.robomaster.modules;

public class Property {
    //physical properties
    public float mass = 17.1f; //unit: kg
    public float width = 0.60f; //unit: m
    public float height = 0.45f; //unit: m
    public float area = width * height; //unit: metre square

    //mechanical properties;
    float acceleration      ;
    public float velocity   ;

    //Competition properties;
    //RoboMasterSate roboMasterState;
    public float health;
    public int numOfBulletsOwned;
    public int numOfBulletsLeft;
    int numOfBulletsShot;
    public float cannonHeat;
    public float bulletSpeed;


    public boolean movable = true;
    public boolean shootable = true;
    public boolean isAlive = false;
}
