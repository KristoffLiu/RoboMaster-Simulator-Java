package com.kristoff.robomaster_simulator.envs.launchers.env.rllib;

import com.kristoff.robomaster_simulator.envs.environment.Environment;

public class RLlibEnv extends Environment implements RLlibEnvInterface {
    RLlibEnvSimulatorConfiguration config;

    public RLlibEnv(){
        super(EnvironmentMode.rllib);
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
