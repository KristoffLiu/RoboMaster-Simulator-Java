package com.kristoff.robomaster_simulator.robomasters.modules;

import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.utils.LoopThread;

public abstract class MultiThreadModule extends LoopThread implements ModuleInterface{
    public RoboMaster roboMaster;

    public MultiThreadModule(RoboMaster roboMaster){
        this.roboMaster = roboMaster;
    }
}
