package com.kristoff.robomaster_simulator.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.kristoff.robomaster_simulator.core.Simulator;
import com.kristoff.robomaster_simulator.io.GlobalInputEventHandler;
import com.kristoff.robomaster_simulator.view.layers.*;
import com.kristoff.robomaster_simulator.view.base.Assets;


public class Renderer implements Screen {
    Simulator simulator;
    Assets assets = new Assets();

    public final float width = 8.49f;
    public final float height = 4.89f;

    public View view;


    public FloorLayer floorLayer;
    public PhysicsLayer physicsLayer;
    public MatrixLayer matrixLayer;
    public RenderedLayer renderedLayer;
    public LidarPointCloudLayer lidarPointCloudLayer;

    public FrameRate frameRate;



    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            physicsLayer.step();
        }
    };

    public Renderer(final Simulator simulator){
        this.simulator = simulator;

        view = new View(width, height);
        map = new StandardCompetitionMap2020(this);

        floorLayer = new FloorLayer(this);
        floorLayer.addListener(new GlobalInputEventHandler(this));
        renderedLayer = new RenderedLayer(this);
        physicsLayer = new PhysicsLayer(this);
        matrixLayer = new MatrixLayer(this);
        lidarPointCloudLayer = new LidarPointCloudLayer(this);
        frameRate = new FrameRate();

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(floorLayer);
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
        //Gdx.graphics.getDeltaTime();
        Gdx.gl.glClearColor(0.4f, 0.4f, 0.4f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        view.update(delta);
        floorLayer.act();
        floorLayer.draw();

        physicalSimulate(delta);

        matrixLayer.act();
        matrixLayer.draw();

        lidarPointCloudLayer.act(delta);
        lidarPointCloudLayer.draw();

        renderedLayer.act();
        renderedLayer.draw();
        physicsLayer.render(delta);
        frameRate.update();
        frameRate.render();
    }

    float accum = 0f;

    private void physicalSimulate(float delta){
        accum += Gdx.graphics.getDeltaTime();
        while (accum >= 1/60f) {
            accum -= 1/60f;
            runnable.run();
        }
    }

    /**
     * @param width the width you want to resize with.
     * @param height the height you want to resize with.
     * @link ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {
        view.updateSize(width,height);
        frameRate.resize(width,height);
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
