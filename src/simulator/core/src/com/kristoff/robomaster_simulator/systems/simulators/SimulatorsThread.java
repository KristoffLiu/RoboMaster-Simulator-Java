package com.kristoff.robomaster_simulator.systems.simulators;

public class SimulatorsThread extends Thread {
//    float delta = 1/60f;
//    float timeState_1 = 0f;
//
//    public SimulatorsThread(){
//        RoboMasters.init();
//    }
//
//    @Override
//    public void run(){
//        try {
//            while (true){
//
//                PhysicalSimulator.current.step(delta);
//                if(timeState_1 >= 1/20f){
//                    timeState_1 = 0f;
//                    MatrixSimulator.current.step();
//                    RoboMasters.stepObservation();
//                }
//                Thread.sleep((long) (1/60f * 1000));
//                timeState_1 += delta;
//            }
//        }
//        catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
}
