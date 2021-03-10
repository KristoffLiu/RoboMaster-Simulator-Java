package com.kristoff.robomaster_simulator.view.ui.controls;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kristoff.robomaster_simulator.view.ui.pages.UIPage;

import java.util.ArrayList;

/***
 * Container for items in the UI
 */

public class Container extends UIElement {
    public ArrayList<UIElement> children;
    float virtualWidth;
    float virtualHeight;

    public Container(){
        super();
        children = new ArrayList<>();
    }

    public void setBackground(String imagePath){
        this.setTextureRegion(new TextureRegion(new Texture(imagePath)));
    }

    /***
     * add UI element
     * @param child the actor which will be the child
     */
    public void add(UIElement child){
        children.add(child);
        child.setUIParent(this);
    }

    @Override
    public void setRootPage(UIPage rootPage) {
        super.setRootPage(rootPage);
        for(UIElement child : children){
            child.setRootPage(rootPage);
        }
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
        for(UIElement child : children){
            child.act(delta);
        }
    }

    public void removed(){
        for(UIElement child : children){
            children.remove(child);
            child.getRootPage().removeUIElement(child);
            if(child instanceof Container){
                ((Container)child).removed();
            }
        }
    }
}
