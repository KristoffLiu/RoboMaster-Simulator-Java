package com.kristoff.robomaster_simulator.simulators;

import com.kristoff.robomaster_simulator.environment.Environment;
import com.kristoff.robomaster_simulator.robomasters.RoboMasters;

public class SimulatorsThread extends Thread {

    public SimulatorsThread(){

    }

    @Override
    public void run(){
        try {
            while (true){
                MatrixSimulator.current.step();
                PhysicalSimulator.current.step(1/60f);

                RoboMasters.stepObservation();
                Thread.sleep(1000/60);
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
