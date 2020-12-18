package com.kristoff.robomaster_simulator.launchers.py_entrypoints.rosrmentrypoint;

import com.kristoff.robomaster_simulator.envs.Environment;
import com.kristoff.robomaster_simulator.envs.EnvironmentConfiguration;
import com.kristoff.robomaster_simulator.envs.EnvironmentMode;
import com.kristoff.robomaster_simulator.envs.rosrm.RosRMEnv;
import com.kristoff.robomaster_simulator.launchers.py_entrypoints.EntryPoint;
import com.kristoff.robomaster_simulator.envs.rllib.RLlibEnv;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.matrixsimulation.MatrixSimulator;
import py4j.GatewayServer;

public class RosRMEntryPoint extends EntryPoint {

    public RosRMEntryPoint(){
        config = new EnvironmentConfiguration();
        config.mode = EnvironmentMode.rosrm;
        env = new RosRMEnv(config);
        env.launch();
    }

    public static void main(String[] args) {
        GatewayServer gatewayServer = new GatewayServer(new RosRMEntryPoint());
        gatewayServer.start();
        System.out.println("Gateway Server Started");
    }

    public MatrixSimulator.MatrixPointStatus[][] getMap(){
        return Systems.matrixSimulator.getMatrix();
    }

}
