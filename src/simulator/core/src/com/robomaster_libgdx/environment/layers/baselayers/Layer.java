package com.robomaster_libgdx.environment.layers.baselayers;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.robomaster_libgdx.environment.Environment;

public class Layer extends Stage {
    public Environment environment;

    public Layer(Viewport viewport, Environment environment){
        super(viewport);
        this.environment = environment;
    }
}
