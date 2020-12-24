package com.kristoff.robomaster_simulator.utils;

public class TriggeredThread extends Thread {
    public boolean isTriggered = false;
    public TriggeredThread(){

    }

    @Override
    public void run(){
        if (isTriggered){
            synchronized (this){
                step();
                this.isTriggered = false;
            }
        }
    }

    public void step(){

    }
}
