package com.kristoff.robomaster_simulator.robomasters.robomaster.modules;

import com.kristoff.robomaster_simulator.robomasters.robomaster.RoboMaster;

public class Module implements ModuleInterface {
    public RoboMaster roboMaster;

    public Module(RoboMaster roboMaster){
        this.roboMaster = roboMaster;
    }
}
