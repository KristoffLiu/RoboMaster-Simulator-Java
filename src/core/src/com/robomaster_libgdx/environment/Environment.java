package com.robomaster_libgdx.environment;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.robomaster_libgdx.environment.simulatinglayers.WorldLayer;
import com.robomaster_libgdx.Simulator;

public class Environment implements Screen {
    Simulator simulator;

    WorldLayer worldLayer;
    public View view;

    public Environment(final Simulator simulator){
        this.simulator = simulator;
        view = new View(simulator.VIEW_WIDTH, simulator.VIEW_HEIGHT);
        worldLayer = new WorldLayer(this);

        worldLayer.addListener(new GlobalInputEventHandler(view));
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(worldLayer);
        Gdx.input.setInputProcessor(multiplexer);
    }

    /**
     * Called when this screen becomes the current screen for a Game.
     */
    @Override
    public void show() {

    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        uiStage.act();
//        uiStage.draw();
        view.update(delta);
        worldLayer.act();
        worldLayer.draw();
    }

    /**
     * @param width the width you want to resize with.
     * @param height the height you want to resize with.
     * @link ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {
        view.updateSize(width,height);
    }

    /**
     * @link ApplicationListener#pause()
     */
    @Override
    public void pause() {

    }

    /**
     * @link ApplicationListener#resume()
     */
    @Override
    public void resume() {

    }

    /**
     * Called when this screen is no longer the current screen for a Game.
     */
    @Override
    public void hide() {

    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {

    }

}
