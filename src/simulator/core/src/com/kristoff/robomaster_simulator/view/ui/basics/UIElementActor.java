package com.kristoff.robomaster_simulator.view.ui.basics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Custom Actor class extends from interface Actor.
 * This custom actor offers actor the feature of collision detection.
 *
 * - Zhikang Liu 2020/11/03
 */
public class UIElementActor extends Actor {
    // TextureRegion of this Customer Actor
    private TextureRegion textureRegion;
    private final Rectangle bounds = new Rectangle();

    public UIElementActor() {
        super();
    }

    /**
     * Constructor - Create a custom actor
     * @param textureRegion The texture of the actor
     */
    public UIElementActor(TextureRegion textureRegion) {
        super();
        setTextureRegion(textureRegion);
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    /**
     * Retrieves the bounds of the actor
     * @return
     */
    public Rectangle getBounds(){
        bounds.set(
                this.getX(),
                this.getY(),
                this.getWidth(),
                this.getHeight()
        );
        return bounds;
    }


    /**
     * Sets the texture of the actor
     * @param textureRegion The texture of the actor
     */
    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
        // reset the width and height after resetting the texture region.

        if(this.getWidth() == 0 && this.getHeight() == 0){
            setSize(this.textureRegion.getRegionWidth(), this.textureRegion.getRegionHeight());
        }
    }

    /**
     * Sets the texture of the actor
     * @param string The path of the texture of the actor
     */
    public void setTextureRegion(String string) {
        this.textureRegion = new TextureRegion(new Texture(string));
        // reset the width and height after resetting the texture region.

        if(this.getWidth() == 0 && this.getHeight() == 0){
            setSize(this.textureRegion.getRegionWidth(), this.textureRegion.getRegionHeight());
        }
    }

    /**
     * RenderActor
     *
     * @param batch
     *
     * @param parentAlpha
     * 		The Sprite Batch, is used to render the texture of actor encapsulation.
     *
     * @param parentAlpha
     * 		the transparent of parent
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (textureRegion == null || !isVisible() || textureRegion.getTexture() == null)  {
            return;
        }

        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        batch.draw(
                textureRegion,
                getX(), getY(),
                getOriginX(), getOriginY(),
                getWidth(), getHeight(),
                getScaleX(), getScaleY(),
                getRotation()
        );
    }
}
