package com.kristoff.robomaster_simulator.view.base.layers;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kristoff.robomaster_simulator.view.EnvRenderer;

public class Layer extends Stage {
    public EnvRenderer environment;

    public Layer(Viewport viewport, EnvRenderer envRenderer){
        super(viewport);
        this.environment = envRenderer;
    }
}
