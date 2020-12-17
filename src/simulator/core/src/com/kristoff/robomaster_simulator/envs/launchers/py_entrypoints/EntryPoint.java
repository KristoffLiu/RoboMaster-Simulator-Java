package com.kristoff.robomaster_simulator.envs.launchers.py_entrypoints;

import com.kristoff.robomaster_simulator.envs.environment.Environment;
import com.kristoff.robomaster_simulator.envs.launchers.LauncherBase;
import py4j.GatewayServer;

public class EntryPoint extends LauncherBase {

    public static void main(String[] args) {
        GatewayServer gatewayServer = new GatewayServer(new EntryPoint());
        gatewayServer.start();
        System.out.println("Gateway Server Started");
    }

    @Override
    public void init(){
        super.init();
    }

    @Override
    public Environment getEnv(){
        return env;
    }
}
