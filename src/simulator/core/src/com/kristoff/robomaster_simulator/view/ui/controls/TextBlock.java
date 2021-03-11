package com.kristoff.robomaster_simulator.view.ui.controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.kristoff.robomaster_simulator.view.ui.controls.labels.LabelStylesHelper;
import com.kristoff.robomaster_simulator.view.ui.pages.UIPage;

/***
 * Class for all text
 */
public class TextBlock extends UIElement {
    public Label label;
    String text;
    String fontFamily;
    float fontSize;
    boolean isClearer;
    Color fontColor;
    public TextBlock(){
        fontFamily = "font/ImpactFont.fnt";
        fontColor = new Color();
        fontColor.set(0,0,0,1);
        fontSize = 1.0f;
        isClearer = true;
    }

    public String getText(){
        return this.text;
    }

    public void setText(String text){
        this.text = text;
        updateText();
    }

    public void setFontSize(float fontSize){
        this.fontSize = fontSize;
        updateText();
    }

    public void setFontColor(Color color){
        this.fontColor = color;
        updateText();
    }

    public void setFontColor(float r, float g, float b, float a){
        this.fontColor = new Color(r,g,b,a);
        updateText();
    }

    public void updateText(){
        label = new Label(
                this.text,
                LabelStylesHelper.generateLabelStyle(
                        this.fontFamily,
                        this.isClearer,
                        this.fontSize,
                        this.fontColor));
    }

    /**
     * logic handler of the actor
     *
     * @param delta
     *		the change of time from the last rendered frame to the current rendering frame,
     *	    or we call it the rendering time step / time difference.
     *	    the unite is second.
     */
    @Override
    public void act(float delta) {
        super.act(delta);
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
        label.draw(batch, parentAlpha);
    }

    @Override
    public void setRootPage(UIPage uiPage){
        super.setRootPage(uiPage);
    }
}
