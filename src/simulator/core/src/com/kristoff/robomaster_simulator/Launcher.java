package com.kristoff.robomaster_simulator;

import com.kristoff.robomaster_simulator.environment.Environment;

public class Launcher {
    public static void main (String[] arg) {
        Environment environment = new Environment();
        environment.launch();
    }
}
