package com.kristoff.robomaster_simulator.launcher;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kristoff.robomaster_simulator.core.Simulator;
import com.kristoff.robomaster_simulator.view.Frame;

public class Launcher {
    public static void main (String[] arg) {
        Simulator simulator = new Simulator();
        simulator.launch();
    }
}
