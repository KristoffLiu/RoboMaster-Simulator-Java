package com.robomaster_libgdx.environment;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.robomaster_libgdx.environment.libs.MatrixSenseSystem;
import com.robomaster_libgdx.environment.robomasters.AlexanderMasterI;
import com.robomaster_libgdx.environment.robomasters.RoboMaster;
import com.robomaster_libgdx.environment.maps.StandardCompetitionMap2020;
import com.robomaster_libgdx.environment.simulatinglayers.FloorLayer;
import com.robomaster_libgdx.Simulator;
import com.robomaster_libgdx.environment.simulatinglayers.GlobalMatrixLayer;
import com.robomaster_libgdx.environment.simulatinglayers.PhysicsLayer;
import com.robomaster_libgdx.environment.simulatinglayers.RenderedLayer;


public class Environment implements Screen {
    Simulator simulator;

    public final float width = 8.49f;
    public final float height = 4.89f;

    public View view;
    public StandardCompetitionMap2020 map;

    FloorLayer floorLayer;
    GlobalMatrixLayer globalMatrixLayer;
    RenderedLayer renderedLayer;
    PhysicsLayer physicsLayer;

    public ShapeRenderer shapeRenderer = new ShapeRenderer();
    public ShapeRenderer pointCloudRenderer = new ShapeRenderer();


    public Array<RoboMaster> allRoboMasters = new Array<>();
    public Array<RoboMaster> teamBlue = new Array<>();
    public Array<RoboMaster> teamRed = new Array<>();

    public MatrixSenseSystem matrixSenseSystem;

    public Environment(final Simulator simulator){
        this.simulator = simulator;

        shapeRenderer = new ShapeRenderer();
        pointCloudRenderer = new ShapeRenderer();

        for(int i = 0; i <= 1; i++){
            teamBlue.add(new AlexanderMasterI());
        }
        for(int i = 0; i <= 1; i++){
            teamRed.add(new AlexanderMasterI());
        }
        allRoboMasters.addAll(teamBlue);
        allRoboMasters.addAll(teamRed);

        view = new View(width, height);
        map = new StandardCompetitionMap2020(this);
        matrixSenseSystem = new MatrixSenseSystem(this);

        floorLayer = new FloorLayer(this);
        floorLayer.addListener(new GlobalInputEventHandler(view));
        renderedLayer = new RenderedLayer(this);
        physicsLayer = new PhysicsLayer(this);
        globalMatrixLayer = new GlobalMatrixLayer(this);


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
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        uiStage.act();
//        uiStage.draw();
        view.update(delta);

        shapeRenderer.setProjectionMatrix(view.getOrthographicCamera().combined);
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        floorLayer.act();
        floorLayer.draw();
        shapeRenderer.end();

        physicsLayer.step();
        physicsLayer.render();
        renderedLayer.act();
        renderedLayer.draw();

        pointCloudRenderer.setProjectionMatrix(view.getOrthographicCamera().combined);
        pointCloudRenderer.setAutoShapeType(true);
        pointCloudRenderer.begin(ShapeRenderer.ShapeType.Filled);
        globalMatrixLayer.act();
        globalMatrixLayer.draw();
        pointCloudRenderer.end();

        matrixSenseSystem.update(delta);
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
