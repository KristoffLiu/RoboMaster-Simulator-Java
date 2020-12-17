package com.kristoff.robomaster_simulator.environment;

public class EnvironmentConfiguration {

    public float physicalWorldStep = 1 / 60f;

    public boolean isRendering = true;
    public int renderedFrameRate = 120;
    public EnvironmentMode mode = EnvironmentMode.debug;

    public int width = 1920;
    public int height = 1080;
    public float scaleFactor = 0.6f;

    public enum EnvironmentMode{
        debug,
        individual,
        rllib,
        ros_realmachinedebugging
    }
}
