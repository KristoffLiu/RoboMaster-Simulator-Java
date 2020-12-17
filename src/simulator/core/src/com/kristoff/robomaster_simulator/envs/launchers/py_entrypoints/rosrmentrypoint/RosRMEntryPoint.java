package com.kristoff.robomaster_simulator.envs.launchers.py_entrypoints.rosrmentrypoint;

import com.kristoff.robomaster_simulator.envs.environment.Environment;
import com.kristoff.robomaster_simulator.envs.launchers.py_entrypoints.EntryPoint;
import com.kristoff.robomaster_simulator.envs.launchers.env.rllib.RLlibEnv;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.matrixsimulation.MatrixSimulator;

public class RosRMEntryPoint extends EntryPoint {
    public RosRMEntryPoint(){

    }

    public MatrixSimulator.MatrixPointStatus[][] getMap(){
        return Systems.matrixSimulator.getMatrix();
    }

    @Override
    public void init(){
        env = new RLlibEnv();
    }

    @Override
    public Environment getEnv(){
        return env;
    }
}
