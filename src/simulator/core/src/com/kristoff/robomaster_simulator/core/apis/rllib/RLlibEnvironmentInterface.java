package com.kristoff.robomaster_simulator.core.apis.rllib;

public interface RLlibEnvironmentInterface {
    void seed();
    void reset();
    Step step();
    void render();
    void close();
}
