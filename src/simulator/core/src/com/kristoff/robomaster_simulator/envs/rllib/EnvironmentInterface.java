package com.kristoff.robomaster_simulator.envs.rllib;

public interface EnvironmentInterface {
    void seed();
    void reset();
    Step step();
    void render();
    void close();
}
