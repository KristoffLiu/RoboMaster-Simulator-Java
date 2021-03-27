package com.kristoff.robomaster_simulator.robomasters.modules;

public class Property {
    //physical properties
    public static float mass = 17.1f; //unit: kg
    public static float width = 0.60f; //unit: m
    public static float height = 0.50f; //unit: m
    public static int widthUnit = 60; //unit: m
    public static int heightUnit = 50; //unit: m
    public static float area = width * height; //unit: metre square

    //mechanical properties;
    float acceleration      ;
    public float velocity   ;

    //Competition properties;
    //RoboMasterSate roboMasterState;
    public int health = 2000;
    public int numOfBulletsOwned;
    public int numOfBulletsLeft;
    int numOfBulletsShot;
    public float cannonHeat;
    public float bulletSpeed;


    public boolean movable = true;
    public boolean shootable = true;
    public boolean isAlive = true;
}
