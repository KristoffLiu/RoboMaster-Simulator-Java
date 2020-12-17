package com.kristoff.robomaster_simulator.apis.rllib;

import com.kristoff.robomaster_simulator.environment.Environment;

public class RLlibEnvironment implements RLlibEnvironmentInterface{
    Environment environment;
    RLlibEnvironmentSimulatorConfiguration config;

    public RLlibEnvironment(){
        config = new RLlibEnvironmentSimulatorConfiguration();
        environment = new Environment();
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
