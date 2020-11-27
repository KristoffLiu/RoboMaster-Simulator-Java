package com.robomaster_libgdx.environment.robomasters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.robomaster_libgdx.environment.robomasters.RoboMaster;

public class AlexanderMasterII extends RoboMaster {
    public AlexanderMasterII(){
        super(new TextureRegion(
                        new Texture("RoboMasters/AlexanderMaster.png")));
    }
    private double weight = 17.1;           //Kg
    private int max_forward_speed = 3;      //m/s
    private int max_cross_range_speed = 2;  //m/s
    private float shooting_speed = 6;       //per second
    private float cannon_range = 180;       //degree
    private float bullet_speed = 25;        //m/s
    private int max_carrying_bullet = 300;  //m/s

}
