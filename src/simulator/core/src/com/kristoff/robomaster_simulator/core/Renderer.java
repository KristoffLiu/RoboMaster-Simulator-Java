package com.kristoff.robomaster_simulator.core.simulator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Array;
import com.kristoff.robomaster_simulator.render.layers.*;
import com.kristoff.robomaster_simulator.render.base.Assets;
import com.kristoff.robomaster_simulator.robomasters.AlexanderMasterII;
import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.core.simulator.maps.StandardCompetitionMap2020;


public class Renderer implements Screen {
    Simulator simulator;
    Assets assets = new Assets();

    public final float width = 8.49f;
    public final float height = 4.89f;

    public View view;
    public StandardCompetitionMap2020 map;

    public FloorLayer floorLayer;
    public PhysicsLayer physicsLayer;
    public MatrixLayer matrixLayer;
    public RenderedLayer renderedLayer;
    public LidarPointCloudLayer lidarPointCloudLayer;

    public FrameRate frameRate;

    public Array<RoboMaster> allRoboMasters = new Array<>();
    public Array<RoboMaster> teamBlue = new Array<>();
    public Array<RoboMaster> teamRed = new Array<>();

    public Renderer(final Simulator simulator){
        this.simulator = simulator;

        for(int i = 0; i <= 1; i++){
            RoboMaster roboMaster = new AlexanderMasterII();
            teamBlue.add(roboMaster);
        }
        for(int i = 0; i <= 1; i++){
            teamRed.add(new AlexanderMasterII());
        }
        allRoboMasters.addAll(teamBlue);
        allRoboMasters.addAll(teamRed);

        view = new View(width, height);
        map = new StandardCompetitionMap2020(this);

        floorLayer = new FloorLayer(this);
        floorLayer.addListener(new GlobalInputEventHandler(this));
        renderedLayer = new RenderedLayer(this);
        physicsLayer = new PhysicsLayer(this);
        matrixLayer = new MatrixLayer(this);
        lidarPointCloudLayer = new LidarPointCloudLayer(this);
        frameRate = new FrameRate();

        for(RoboMaster roboMaster : teamBlue){
            roboMaster.transformRotation((float) (Math.PI));
        }

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
        while (accum >= Configuration.physicalWorldStep) {
            accum -= Configuration.physicalWorldStep;
            physicsLayer.step();
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
