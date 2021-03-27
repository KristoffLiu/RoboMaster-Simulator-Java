package com.kristoff.robomaster_simulator.view.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.kristoff.robomaster_simulator.core.SimulatorConfiguration;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.view.View;
import com.kristoff.robomaster_simulator.view.io.GlobalInputEventHandler;
import com.kristoff.robomaster_simulator.systems.maps.Map;
import com.kristoff.robomaster_simulator.teams.RoboMasters;
import com.kristoff.robomaster_simulator.view.layers.*;
import com.kristoff.robomaster_simulator.view.Assets;


public class EnvRenderer implements Screen {
    SimulatorConfiguration config;
    Assets assets = new Assets();

    public final float width = 8.49f;
    public final float height = 4.89f;

    public View view;
    public RoboMasters roboMasters;
    public Map map;

    public FloorLayer floorLayer;
    public PhysicsDebugLayer physicsDebugLayer;

    public RenderedLayer renderedLayer;
    public RoboMasterLayer roboMasterLayer;
    public BlocksLayer blocksLayer;
    public LidarPointCloudLayer lidarPointCloudLayer;
    public BuffLayer buffLayer;
    public HUD HUD;

    public FrameRate frameRate;

    public EnvRenderer(SimulatorConfiguration simulatorConfiguration){
        this.config = simulatorConfiguration;

        view = new View(width, height);
        map = Systems.map;

        floorLayer = new FloorLayer(this);
        renderedLayer = new RenderedLayer(this);
        roboMasterLayer = new RoboMasterLayer(this);
        roboMasterLayer.addListener(new GlobalInputEventHandler(this));

        lidarPointCloudLayer = new LidarPointCloudLayer(this);

        blocksLayer = new BlocksLayer(this);
        buffLayer = new BuffLayer(this);
        HUD = new HUD(this);
        frameRate = new FrameRate();

        switch (config.mode){
            case simulator,simulatorRLlib ->{
                physicsDebugLayer = new PhysicsDebugLayer(this);
            }
            case realMachine -> {

            }
        }

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(roboMasterLayer);
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



        roboMasterLayer.act();
        roboMasterLayer.draw();

        blocksLayer.act();
        blocksLayer.draw();

        buffLayer.act();
        buffLayer.draw();

        HUD.act(delta);
        HUD.draw();

        switch (config.mode){
            case simulator,simulatorRLlib ->{
                physicsDebugLayer.render(delta);
            }
            case realMachine -> {

            }
        }

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
