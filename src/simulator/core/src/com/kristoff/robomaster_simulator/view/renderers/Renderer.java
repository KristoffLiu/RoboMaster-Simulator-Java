package com.kristoff.robomaster_simulator.view.renderers;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.kristoff.robomaster_simulator.envs.Environment;

public class Renderer{
    LwjglApplication application;
    public Renderer(Environment environment, RendererConfiguration configuration){
        application = new LwjglApplication(environment, configuration);
    }
}
