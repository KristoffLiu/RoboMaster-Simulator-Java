package com.kristoff.robomaster_simulator.view.base.layers;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kristoff.robomaster_simulator.view.Renderer;

public class Layer extends Stage {
    public Renderer environment;

    public Layer(Viewport viewport, Renderer renderer){
        super(viewport);
        this.environment = renderer;
    }
}
