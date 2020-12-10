package com.kristoff.robomaster_simulator.robomasters.modules.properties;

public class Property {
    //physical properties
    public float mass = 17.1f; //unit: kg
    public float width = 0.60f; //unit: m
    public float height = 0.45f; //unit: m
    public float area = width * height; //unit: metre square

    //mechanical properties;
    float acceleration;
    float velocity;

    //Competition properties;
    //RoboMasterSate roboMasterState;
    float health;
    int numOfBulletsOwned;
    int numOfBulletsLeft;
    int numOfBulletsShot;
}