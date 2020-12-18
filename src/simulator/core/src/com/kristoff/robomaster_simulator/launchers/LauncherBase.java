package com.kristoff.robomaster_simulator.launchers;

import com.kristoff.robomaster_simulator.Launcher;
import com.kristoff.robomaster_simulator.envs.Environment;
import com.kristoff.robomaster_simulator.envs.EnvironmentConfiguration;

public abstract class LauncherBase {
    public static LauncherBase current;
    public Environment env;
    public EnvironmentConfiguration config;

    public LauncherBase(){
        env = new Environment(new EnvironmentConfiguration());
    }

    public static void main (String[] arg) {
        current = new Launcher();
        current.launch();
    }

    public void launch(){
        env.launch();
    }

    public Environment getEnv(){
        return env;
    }
}
