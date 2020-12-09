package com.kristoff.robomaster_simulator.launcher.entrypoints;

import py4j.GatewayServer;

public class RLlibEntryPoint {

    public static void main(String[] args) {
        GatewayServer gatewayServer = new GatewayServer(new RLlibEntryPoint());
        gatewayServer.start();
        System.out.println("Gateway Server Started");
    }
}
