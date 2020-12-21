package com.kristoff.robomaster_simulator.launchers;

import com.kristoff.robomaster_simulator.Launcher;
import com.kristoff.robomaster_simulator.envs.Simulator;
import com.kristoff.robomaster_simulator.envs.SimulatorConfiguration;
import com.kristoff.robomaster_simulator.envs.SimulatorMode;

public abstract class LauncherBase {
    public static LauncherBase current;
    public Simulator simulator;
    public SimulatorConfiguration config;

    public LauncherBase(){
        this.config = new SimulatorConfiguration();
        this.config.mode = SimulatorMode.simulator;
        this.simulator = new Simulator(config);
        this.simulator.launch();
        this.simulator.init();
    }

    public static void main (String[] arg) {
        current = new Launcher();
    }

    public Simulator getSimulator(){
        return simulator;
    }
}
