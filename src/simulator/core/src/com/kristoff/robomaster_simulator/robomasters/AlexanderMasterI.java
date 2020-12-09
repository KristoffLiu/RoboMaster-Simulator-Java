package com.kristoff.robomaster_simulator.core.robomasters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AlexanderMasterI extends RoboMaster{
    public AlexanderMasterI(){
        super(new TextureRegion(
                        new Texture("RoboMasters/Alexander's Master I.png")));
    }
    private double weight = 17.1;           //Kg
    private int max_forward_speed = 3;      //m/s
    private int max_cross_range_speed = 2;  //m/s
    private float shooting_speed = 10;      //per second
    private float cannon_range = 180;       //per second
    private float bullet_speed = 25;        //m/s
    private int max_carrying_bullet = 200;  //m/s


}
