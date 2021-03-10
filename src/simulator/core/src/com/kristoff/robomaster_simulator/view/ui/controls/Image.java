package com.kristoff.robomaster_simulator.view.ui.controls;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/***
 * class of a Image, which is inherited from a UI Element.
 */
public class Image extends UIElement {

    public Image() {
        super();
    }

    public Image(TextureRegion textureRegion) {
        super();
        setTextureRegion(textureRegion);
    }
}
