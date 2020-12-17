package com.kristoff.robomaster_simulator.core.apis.entrypoints.rllibentrypoint;

import com.kristoff.robomaster_simulator.core.apis.rllib.RLlibEnvironment;
import py4j.GatewayServer;

public class RLlibEntryPoint {
    RLlibEnvironment env = new RLlibEnvironment();

    public static void main(String[] args) {
        GatewayServer gatewayServer = new GatewayServer(new RLlibEntryPoint());
        gatewayServer.start();
        System.out.println("Gateway Server Started");
    }

    public RLlibEnvironment getEnv(){
        return env;
    }
}
