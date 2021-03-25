package com.kristoff.robomaster_simulator.core;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class SimulatorConfiguration {
    public SimulatorMode mode = SimulatorMode.simulator;

    LwjglApplicationConfiguration rendererConfig = new LwjglApplicationConfiguration();
    public boolean isRendering                  = true;
    public float physicalWorldStep              = 1 / 60f;
    public int renderedFrameRate                = 60;

    public int width                            = 1920;
    public int height                           = 1080;
    public float scaleFactor                    = 0.6f;
}
