package com.kristoff.robomaster_simulator.launchers.py_entrypoints;

import com.kristoff.robomaster_simulator.envs.Environment;
import com.kristoff.robomaster_simulator.launchers.LauncherBase;
import py4j.GatewayServer;

public class EntryPoint extends LauncherBase {

    public static void main(String[] args) {
        LauncherBase.main(args);
        GatewayServer gatewayServer = new GatewayServer(new EntryPoint());
        gatewayServer.start();
        System.out.println("Gateway Server Started");
    }

    @Override
    public Environment getEnv(){
        return env;
    }
}
