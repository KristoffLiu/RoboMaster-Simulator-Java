package com.kristoff.robomaster_simulator.judgementsystem;

import com.kristoff.robomaster_simulator.systems.robomasters.RoboMaster;

public class AddBullet extends Buff{
    public AddBullet(RoboMaster Robomaster) {
        super(Robomaster);
        Robomaster.property.numOfBulletsLeft += 100;
        Robomaster.property.numOfBulletsOwned += 100;

    }
}
