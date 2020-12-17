package com.kristoff.robomaster_simulator.apis.rllib;

import com.badlogic.gdx.utils.Null;

import java.util.HashMap;

public class Step {
    public Observation observation;
    public double reward;
    public boolean done;
    public HashMap info;

    public Step(){

    }

    public Step(Observation observation, double reward, @Null boolean done, @Null HashMap info){
        this.observation = observation;
        this.reward = reward;
        this.done = done;
        this.info = info;
    }
}
