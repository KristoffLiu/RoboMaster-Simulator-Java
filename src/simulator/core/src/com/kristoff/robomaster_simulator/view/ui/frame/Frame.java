package com.kristoff.robomaster_simulator.view.ui.frame;

import com.badlogic.gdx.Gdx;
import com.kristoff.robomaster_simulator.view.ui.pages.UIPage;

import java.util.Stack;

/***
 * Frame to allow going between pages
 */
public class Frame {
    public Stack<UIPage> history;
    public UIPage currentPage;

    public Frame(){
        history = new Stack<>();
    }

    public void navigate(UIPage uiPage){
        history.push(uiPage);
        if(currentPage != null){
            currentPage.hide();
        }
        currentPage = history.peek();
        currentPage.setParentFrame(this);
        currentPage.appear();
        Gdx.input.setInputProcessor(currentPage);
    }

    public void goBack(){
        currentPage.hide();
        history.pop();
        currentPage = history.peek();
        currentPage.appear();
        Gdx.input.setInputProcessor(currentPage);
    }

    public void render() {
        currentPage.act();
        currentPage.draw();
    }
}
