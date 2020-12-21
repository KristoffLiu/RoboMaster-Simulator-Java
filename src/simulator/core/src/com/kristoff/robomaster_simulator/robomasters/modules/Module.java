package com.kristoff.robomaster_simulator.robomasters.modules;

import com.kristoff.robomaster_simulator.robomasters.RoboMaster;

public class Module implements ModuleInterface {
    public RoboMaster roboMaster;

    public Module(RoboMaster roboMaster){
        this.roboMaster = roboMaster;
    }
}
