package com.kristoff.robomaster_simulator.launchers.py_entrypoints.rllibentrypoint;

import com.kristoff.robomaster_simulator.envs.Simulator;
import com.kristoff.robomaster_simulator.envs.SimulatorConfiguration;
import com.kristoff.robomaster_simulator.envs.SimulatorMode;
import com.kristoff.robomaster_simulator.launchers.py_entrypoints.rosrmentrypoint.RosRMEntryPoint;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.pointsimulator.PointSimulator;
import py4j.GatewayServer;

public class RLlibEntryPoint{
    public static RLlibEntryPoint current;
    public Simulator simulator;
    public SimulatorConfiguration config;

    public RLlibEntryPoint(){
        config = new SimulatorConfiguration();
        config.mode = SimulatorMode.simulatorRLlib;
        simulator = new Simulator(config);
    }

    public static void main(String[] args) {
        GatewayServer gatewayServer = new GatewayServer(new RosRMEntryPoint());
        gatewayServer.start();
        System.out.println("Gateway Server Started");
    }

    public void launch(){
        simulator.launch();
    }

    public Simulator getSimulator(){
        return simulator;
    }

    public PointSimulator.PointStatus[][] getMap(){
        return Systems.pointSimulator.getMatrix();
    }
}
