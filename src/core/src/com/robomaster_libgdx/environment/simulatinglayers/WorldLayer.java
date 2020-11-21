package com.robomaster_libgdx.environment.simulatinglayers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.robomaster_libgdx.environment.Environment;
import com.robomaster_libgdx.environment.actors.CustomActor;
import com.robomaster_libgdx.environment.actors.robomasters.AlexanderMasterI;
import com.robomaster_libgdx.environment.actors.robomasters.ProtoMaster;
import com.robomaster_libgdx.environment.actors.robomasters.RoboMaster;
import com.robomaster_libgdx.environment.simulatinglayers.baselayers.RenderedLayer;

public class WorldLayer extends RenderedLayer {
    Environment environment;

    private TiledMap map;
    private TmxMapLoader loader;
    private OrthogonalTiledMapRenderer renderer;

    public WorldLayer(Environment environment) {
        super(environment);

        this.environment = environment;
        loader      = new TmxMapLoader();
        map         = loader.load("Map/CompetitionMap/map.tmx");
        renderer    = new OrthogonalTiledMapRenderer(map);

        AlexanderMasterI alexanderMasterI = new AlexanderMasterI();
        alexanderMasterI.setPosition(500,500);
        //direction   = new Vector2();
        this.addActor(alexanderMasterI);

        environment.view.getOrthographicCamera().position.x = 8490f / 2f;
        environment.view.getOrthographicCamera().position.y = 4890f / 2f;
        environment.view.getOrthographicCamera().zoom = 10f;
    }

    public void resize(int width, int height) {
        //viewport.update(width, height);
    }

    @Override
    public void act(float delta){
        super.act(delta);
    }

    @Override
    public void draw () {
        renderer.setView((OrthographicCamera) environment.view.getOrthographicCamera());
        renderer.render();
        super.draw();
        //Gdx.app.log("MyTag", String.valueOf(camera.zoom));
//        getViewport().getCamera().
//        getViewport().getCamera().update();
    }
}
