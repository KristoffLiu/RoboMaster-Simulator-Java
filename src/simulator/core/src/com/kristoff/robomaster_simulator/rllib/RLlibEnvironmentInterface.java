package com.kristoff.robomaster_simulator.rllib;

public interface RLlibEnvironmentInterface {
    void seed();
    void reset();
    Step step();
    void render();
    void close();
}
