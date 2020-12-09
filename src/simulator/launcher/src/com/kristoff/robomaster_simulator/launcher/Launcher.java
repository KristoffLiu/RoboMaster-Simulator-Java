package com.kristoff.robomaster_simulator.launcher;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Simulator {
    public void launch(){
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = "RoboMaster Simulator - Java Platform";

        float scaleFactor = 0.6f;
        config.width = (int) (1920 * scaleFactor);
        config.height = (int) (1080 * scaleFactor);
        config.useGL30 = true;
        config.foregroundFPS = 60;

        new LwjglApplication(new com.kristoff.robomaster_simulator.core.simulator.Simulator(), config);
    }
}
