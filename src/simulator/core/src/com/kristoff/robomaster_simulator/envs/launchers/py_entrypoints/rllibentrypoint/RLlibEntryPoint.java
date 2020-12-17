package com.kristoff.robomaster_simulator.envs.launchers.py_entrypoints.rllibentrypoint;

import com.kristoff.robomaster_simulator.envs.launchers.py_entrypoints.EntryPoint;
import com.kristoff.robomaster_simulator.envs.launchers.env.rllib.RLlibEnv;

public class RLlibEntryPoint extends EntryPoint {
    public RLlibEntryPoint(){
        env = new RLlibEnv();
    }

    public RLlibEnv getEnv(){
        return (RLlibEnv) env;
    }
}
