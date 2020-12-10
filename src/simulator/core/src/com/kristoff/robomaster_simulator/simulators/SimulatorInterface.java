package com.kristoff.robomaster_simulator.simulators;

public interface SimulatorInterface {
    void step();
    void step(float delta);
    void stepAsync();
    void stepAsync(float delta);
}
