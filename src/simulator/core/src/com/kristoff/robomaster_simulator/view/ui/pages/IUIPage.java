package com.kristoff.robomaster_simulator.view.ui.pages;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.kristoff.robomaster_simulator.view.ui.controls.UIElement;

/***
 * Interface for a page
 */

public interface IUIPage {
    void addUIElement(UIElement uiElement);
    void removeUIElement(UIElement uiElement);
    Array<Actor> getUIElementsAll();
    void hide();
    void appear();
}
