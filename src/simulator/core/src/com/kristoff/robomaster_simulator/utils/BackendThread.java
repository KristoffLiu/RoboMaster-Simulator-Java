package com.kristoff.robomaster_simulator.environment;

public class BackendThread extends Thread {
    protected float delta = 1/60f;
    protected boolean isStep = false;

    public BackendThread(){

    }

    @Override
    public void run(){
        try {
            while (isStep){
                synchronized (this){
                    step();
                    Thread.sleep((long) (delta * 1000));
                }
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void step(){

    }
}
