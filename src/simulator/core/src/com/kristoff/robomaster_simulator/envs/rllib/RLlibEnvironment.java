package com.kristoff.robomaster_simulator.envs.rllib;

import com.kristoff.robomaster_simulator.envs.Simulator;
import com.kristoff.robomaster_simulator.envs.SimulatorConfiguration;

public class RLlibEnvironment implements EnvironmentInterface {
    Simulator simulator;
    public RLlibEnvironment(SimulatorConfiguration config){

    }

    @Override
    public void seed() {

    }

    @Override
    public void reset(){

    }

    @Override
    public Step step(){
        return new Step();
    }

    @Override
    public void render(){
//        if(config.isRendering){
//
//        }
    }

    @Override
    public void close() {

    }

    public void isRendering(boolean bool){
        //config.isRendering = bool;
    }
}
