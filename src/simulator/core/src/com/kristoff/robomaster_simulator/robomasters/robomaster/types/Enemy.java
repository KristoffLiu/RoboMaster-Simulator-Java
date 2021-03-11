package com.kristoff.robomaster_simulator.robomasters.robomaster.types;

import com.kristoff.robomaster_simulator.robomasters.robomaster.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.robomaster.modules.*;
import com.kristoff.robomaster_simulator.robomasters.teams.Team;

/***
 * //    private double weight = 17.1;           //Kg
 * //    private int max_forward_speed = 3;      //m/s
 * //    private int max_cross_range_speed = 2;  //m/s
 * //    private float shooting_speed = 6;       //per second
 * //    private float cannon_range = 180;       //degree
 * //    private float bullet_speed = 25;        //m/s
 * //    private int max_carrying_bullet = 300;  //m/s
 */
public class Enemy extends RoboMaster {
    boolean isLocked = false;
    boolean isInTheView = true;
    InView inView;
    int count = 0;

    public Enemy(Team team, String name){
        super("RoboMasters/AlexanderMaster.png", team, name);
        inView = new InView(this, 5);
        inView.start();
    }

    @Override
    public void setPosition(int x, int y) {
        System.out.println(this.getPosition().distanceTo(x, y));
        if(isInTheView && this.getPosition().distanceTo(x, y) > 50 && count < 5){
            count ++;
            return;
        }
        count ++;
        setInTheView();
        super.setPosition(x, y);
    }

    @Override
    public void setPosition(int x, int y, float rotation) {
        if(isInTheView && this.getPosition().distanceTo(x, y) > 50 && count < 5){
            count ++;
            return;
        }
        count = 0;
        setInTheView();
        super.setPosition(x, y, rotation);
    }

    public void setInTheView(){
        isInTheView = true;
        inView.resetTimer();
    }

    public void setNotInTheView(){
        isInTheView = false;
    }

    public boolean isInTheView(){
        return this.isInTheView;
    }

    public void lock(){
        isLocked = true;
    }

    public void unlock(){
        isLocked = false;
    }

    public boolean isLocked(){
        return this.isLocked;
    }
}
