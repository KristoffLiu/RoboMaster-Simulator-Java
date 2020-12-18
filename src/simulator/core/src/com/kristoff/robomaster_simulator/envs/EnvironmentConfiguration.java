package com.kristoff.robomaster_simulator.envs;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class EnvironmentConfiguration {
    public EnvironmentMode mode = EnvironmentMode.normal;

    LwjglApplicationConfiguration rendererConfig = new LwjglApplicationConfiguration();
    public boolean isRendering                  = true;
    public float physicalWorldStep              = 1 / 60f;
    public int renderedFrameRate                = 120;

    public int width                            = 1920;
    public int height                           = 1080;
    public float scaleFactor                    = 0.6f;
}
