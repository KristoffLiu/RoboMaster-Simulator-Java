package com.robomaster_libgdx.simulator.layers.baselayers;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.robomaster_libgdx.simulator.Renderer;

public class Layer extends Stage {
    public Renderer environment;

    public Layer(Viewport viewport, Renderer renderer){
        super(viewport);
        this.environment = renderer;
    }
}
