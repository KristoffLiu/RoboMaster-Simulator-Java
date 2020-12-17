package com.kristoff.robomaster_simulator.envs.launchers.env.rllib;

public interface RLlibEnvInterface {
    void seed();
    void reset();
    Step step();
    void render();
    void close();
}
