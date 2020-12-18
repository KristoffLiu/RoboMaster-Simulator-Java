package com.kristoff.robomaster_simulator.envs.rosrm;

import com.kristoff.robomaster_simulator.envs.Environment;
import com.kristoff.robomaster_simulator.envs.EnvironmentConfiguration;

/**
 * RosRMLauncher类 是从 Launcher类 中派生出来的子类，
 * 它继承了env和config这两个属性，可以对RosLauncher进行
 */
public class RosRMEnv extends Environment {
    public RosRMEnv(EnvironmentConfiguration config){
        super(config);
    }
}
