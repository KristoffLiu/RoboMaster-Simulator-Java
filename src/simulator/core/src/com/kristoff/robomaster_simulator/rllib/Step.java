package com.kristoff.robomaster_simulator.rllib;

import java.util.HashMap;

public class Step {
    public Observation observation;
    public double reward;
    public boolean done;
    public HashMap info;

    public Step(){

    }

    public Step(Observation observation, double reward, boolean done, HashMap info){
        this.observation = observation;
        this.reward = reward;
        this.done = done;
        this.info = info;
    }
}
