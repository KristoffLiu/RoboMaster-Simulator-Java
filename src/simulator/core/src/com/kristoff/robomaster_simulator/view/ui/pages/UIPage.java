package com.kristoff.robomaster_simulator.view.ui.pages;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kristoff.robomaster_simulator.core.Simulator;
import com.kristoff.robomaster_simulator.view.ui.controls.UIElement;
import com.kristoff.robomaster_simulator.view.ui.frame.Frame;

/***
 * the stage showing the user interfaces only.
 */
public class UIPage extends Stage implements IUIPage {
    private Frame parentFrame = null;
    Array<Actor> uiElements = new Array<>();

    public UIPage() {
        super(new StretchViewport(Simulator.VIEW_WIDTH, Simulator.VIEW_HEIGHT));
    }

    public UIPage(Viewport viewport) {
        super(viewport);
    }

    /***
     * add UI element
     * @param uiElement the actor which will be the child
     */
    @Override
    public void addUIElement(UIElement uiElement) {
        super.addActor(uiElement);
        this.uiElements.add(uiElement);
        uiElement.setRootPage(this);
    }

    /***
     * remove the ui element
     * @param uiElement the ui element which will be the child
     */
    @Override
    public void removeUIElement(UIElement uiElement) {
        super.getActors().removeValue(uiElement, true);
    }

    /***
     * remove the ui element
     * @return the array of actor representing the child elements of this ui stage.
     */
    @Override
    public Array<Actor> getUIElementsAll() {
        return uiElements;
    }

    /***
     * hide
     */
    @Override
    public void hide(){
        for(Actor actor : this.getActors()){
            ((UIElement)actor).hide();
        }
    }

    /***
     * appear
     */
    @Override
    public void appear(){
        for(Actor actor : this.getActors()){
            ((UIElement)actor).appear();
        }
    }

    public Frame getParentFrame(){
        return this.parentFrame;
    }

    public void setParentFrame(Frame frame){
        this.parentFrame = frame;
    }

    @Override
    public void act(float delta){
        super.act(delta);
        for(Actor actor : this.getActors()){
            actor.act(delta);
        }
    }
}
