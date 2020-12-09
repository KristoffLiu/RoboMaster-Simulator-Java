package com.kristoff.robomaster_simulator.launcher.entrypoints.rllibentrypoint;

public interface RLlibEnvironmentInterface {
    void seed();
    void reset();
    void step();
    void render();
    void close();
}
