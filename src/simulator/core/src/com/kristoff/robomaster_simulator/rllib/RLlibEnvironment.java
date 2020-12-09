package com.kristoff.robomaster_simulator.rllib;

import com.kristoff.robomaster_simulator.core.Simulator;

public class RLlibEnvironment implements RLlibEnvironmentInterface{
    Simulator simulator;
    RLlibEnvironmentConfiguration config;

    public RLlibEnvironment(){
        config = new RLlibEnvironmentConfiguration();
        simulator = new Simulator();
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
        if(config.isRendering){

        }
    }

    @Override
    public void close() {

    }

    public void isRendering(boolean bool){
        config.isRendering = bool;
    }
}
