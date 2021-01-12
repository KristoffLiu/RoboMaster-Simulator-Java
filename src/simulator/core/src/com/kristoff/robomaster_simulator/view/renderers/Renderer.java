package com.kristoff.robomaster_simulator.view.renderers;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.kristoff.robomaster_simulator.core.Simulator;

public class Renderer{
    LwjglApplication application;
    public Renderer(Simulator simulator, RendererConfiguration configuration){
        application = new LwjglApplication(simulator, configuration);
    }
}
