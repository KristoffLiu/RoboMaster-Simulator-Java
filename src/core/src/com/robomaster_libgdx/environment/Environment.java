package com.robomaster_libgdx.environment;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Array;
import com.robomaster_libgdx.environment.libs.Assets;
import com.robomaster_libgdx.environment.robomasters.AlexanderMasterII;
import com.robomaster_libgdx.environment.robomasters.RoboMaster;
import com.robomaster_libgdx.environment.maps.StandardCompetitionMap2020;
import com.robomaster_libgdx.environment.simulatinglayers.*;
import com.robomaster_libgdx.Simulator;


public class Environment implements Screen {
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

    public Environment(final Simulator simulator){
        this.simulator = simulator;

        for(int i = 0; i <= 1; i++){
            teamBlue.add(new AlexanderMasterII());
        }
        for(int i = 0; i <= 1; i++){
            teamRed.add(new AlexanderMasterII());
        }
        allRoboMasters.addAll(teamBlue);
        allRoboMasters.addAll(teamRed);

        view = new View(width, height);
        map = new StandardCompetitionMap2020(this);

        floorLayer = new FloorLayer(this);
        floorLayer.addListener(new GlobalInputEventHandler(view));
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
        Gdx.gl.glClearColor(0.4f, 0.4f, 0.4f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        view.update(delta);
        floorLayer.act();
        floorLayer.draw();

        physicsLayer.step();

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
