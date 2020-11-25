package com.robomaster_libgdx.environment.simulatinglayers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.robomaster_libgdx.environment.Environment;
import com.robomaster_libgdx.environment.actors.robomasters.AlexanderMasterI;
import com.robomaster_libgdx.environment.simulatinglayers.baselayers.VisualLayer;
import com.badlogic.gdx.maps.MapObjects;

public class FloorLayer extends VisualLayer {
    Environment environment;

    private TiledMap map;
    private TmxMapLoader loader;
    private OrthogonalTiledMapRenderer renderer;

    public FloorLayer(Environment environment) {
        super(environment);

        this.environment = environment;

        environment.view.getOrthographicCamera().position.x = environment.width / 2f;
        environment.view.getOrthographicCamera().position.y = environment.height / 2f;
        environment.view.getOrthographicCamera().zoom = 2.0f;
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
        super.draw();
        environment.map.render();
    }


    private void birth_Zone(){

    }
}
