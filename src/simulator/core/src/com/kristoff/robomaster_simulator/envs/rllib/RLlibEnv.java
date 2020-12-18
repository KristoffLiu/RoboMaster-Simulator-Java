package com.kristoff.robomaster_simulator.envs.rllib;

import com.kristoff.robomaster_simulator.envs.Environment;
import com.kristoff.robomaster_simulator.envs.EnvironmentConfiguration;

public class RLlibEnv extends Environment implements RLlibEnvInterface {
    public RLlibEnv(EnvironmentConfiguration config){
        super(config);
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
