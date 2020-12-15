package com.kristoff.robomaster_simulator.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.kristoff.robomaster_simulator.environment.Environment;
import com.kristoff.robomaster_simulator.environment.Systems;
import com.kristoff.robomaster_simulator.view.io.GlobalInputEventHandler;
import com.kristoff.robomaster_simulator.systems.maps.Map;
import com.kristoff.robomaster_simulator.systems.simulators.MatrixSimulator;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMasters;
import com.kristoff.robomaster_simulator.view.layers.*;
import com.kristoff.robomaster_simulator.view.base.Assets;


public class Renderer implements Screen {
    Environment environment;
    Assets assets = new Assets();

    public final float width = 8.49f;
    public final float height = 4.89f;

    public View view;
    public RoboMasters roboMasters;
    public Map map;

    public MatrixSimulator matrixSimulator;

    public FloorLayer floorLayer;
    public PhysicsDebugLayer physicsDebugLayer;

    public RenderedLayer renderedLayer;
    public LidarPointCloudLayer lidarPointCloudLayer;

    public FrameRate frameRate;

    public Renderer(final Environment environment){
        this.environment = environment;
        this.matrixSimulator = Systems.matrixSimulator;


        view = new View(width, height);
        map = Systems.map;


        floorLayer = new FloorLayer(this);
        floorLayer.addListener(new GlobalInputEventHandler(this));
        renderedLayer = new RenderedLayer(this);
        physicsDebugLayer = new PhysicsDebugLayer(environment);

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

        renderedLayer.act();
        renderedLayer.draw();

        lidarPointCloudLayer.act(delta);
        lidarPointCloudLayer.draw();

        physicsDebugLayer.render(delta);

        frameRate.update();
        frameRate.render();

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
