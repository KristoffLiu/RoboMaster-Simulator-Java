package com.kristoff.robomaster_simulator.core;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kristoff.robomaster_simulator.physicalsimulation.PhysicalSimulation;
import com.kristoff.robomaster_simulator.view.Frame;
import com.kristoff.robomaster_simulator.view.Renderer;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;

public class Simulator {
    Renderer renderer;
    PhysicalSimulation physicalSimulation;

    public Simulator(){

    }

    public void start(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    physicalSimulation.step();
                    Thread.sleep(1000f);
                }
            }
        });
    }

    public void launch(){
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = "RoboMaster Simulator - Java Platform";

        float scaleFactor = 0.6f;
        config.width = (int) (1920 * scaleFactor);
        config.height = (int) (1080 * scaleFactor);
        config.useGL30 = true;
        config.foregroundFPS = 60;

        new LwjglApplication(new Frame(), config);


    }
}
