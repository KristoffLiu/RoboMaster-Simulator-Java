package com.kristoff.robomaster_simulator.simulators;

public class Simulator implements SimulatorInterface{
    Runnable step;

    public Simulator(){
        step = new Runnable() {
            @Override
            public void run() {
                step();
            }
        };
    }

    public void step(){

    }

    public void step(float delta){

    }

    public void stepAsync(){
        step.run();
    }

    public void stepAsync(float delta){
        step.run();
    }
}
