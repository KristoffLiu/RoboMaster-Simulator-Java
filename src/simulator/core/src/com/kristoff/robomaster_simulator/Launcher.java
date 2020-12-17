package com.kristoff.robomaster_simulator;

import com.kristoff.robomaster_simulator.envs.launchers.LauncherBase;

public class Launcher extends LauncherBase {
    public static void main (String[] arg) {
        current = new Launcher();
        current.init();
    }
}
