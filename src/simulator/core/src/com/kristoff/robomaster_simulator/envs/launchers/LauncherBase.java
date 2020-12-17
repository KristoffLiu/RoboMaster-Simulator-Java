package com.kristoff.robomaster_simulator.envs.launchers;

import com.kristoff.robomaster_simulator.Launcher;
import com.kristoff.robomaster_simulator.envs.environment.Environment;
import com.kristoff.robomaster_simulator.envs.environment.EnvironmentConfiguration;

public abstract class LauncherBase {
    public static LauncherBase current;
    public Environment env;
    public EnvironmentConfiguration config;

    public LauncherBase(){

    }

    public static void main (String[] arg) {
        current = new Launcher();
        current.init();
    }

    public void init(){
        env = new Environment();
    }

    public Environment getEnv(){
        return env;
    }
}
