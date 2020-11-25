package com.robomaster_libgdx.environment.simulatinglayers;

import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.robomaster_libgdx.environment.Environment;
import com.robomaster_libgdx.environment.simulatinglayers.baselayers.Layer;

public class BackgroundLayer extends Layer {

    public BackgroundLayer(StretchViewport stretchViewport, Environment environment) {
        super(stretchViewport,environment);
    }
}
