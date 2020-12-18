package com.kristoff.robomaster_simulator.launchers.py_entrypoints.rllibentrypoint;

import com.kristoff.robomaster_simulator.envs.Environment;
import com.kristoff.robomaster_simulator.envs.EnvironmentConfiguration;
import com.kristoff.robomaster_simulator.envs.EnvironmentMode;
import com.kristoff.robomaster_simulator.envs.rosrm.RosRMEnv;
import com.kristoff.robomaster_simulator.launchers.py_entrypoints.EntryPoint;
import com.kristoff.robomaster_simulator.envs.rllib.RLlibEnv;
import py4j.GatewayServer;

public class RLlibEntryPoint extends EntryPoint {
    public RLlibEntryPoint(){
        config = new EnvironmentConfiguration();
        config.mode = EnvironmentMode.rosrm;
        env = new RLlibEnv(config);
    }
}
