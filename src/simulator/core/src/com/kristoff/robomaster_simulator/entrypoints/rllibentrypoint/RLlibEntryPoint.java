package com.kristoff.robomaster_simulator.entrypoints.rllibentrypoint;

import com.kristoff.robomaster_simulator.rllib.RLlibEnvironment;
import py4j.GatewayServer;

public class RLlibEntryPoint {
    RLlibEnvironment env = new RLlibEnvironment();

    public static void main(String[] args) {
        GatewayServer gatewayServer = new GatewayServer(new RLlibEntryPoint());
        gatewayServer.start();
        System.out.println("Gateway Server Started");
    }
}
