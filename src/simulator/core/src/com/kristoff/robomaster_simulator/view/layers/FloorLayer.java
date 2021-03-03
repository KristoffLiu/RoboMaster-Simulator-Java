package com.kristoff.robomaster_simulator.view.layers;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.kristoff.robomaster_simulator.view.renderers.EnvRenderer;

public class FloorLayer extends VisualLayer {
    EnvRenderer envRenderer;

    private TiledMap map;
    private TmxMapLoader loader;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    public FloorLayer(EnvRenderer envRenderer) {
        super(envRenderer);

        this.envRenderer = envRenderer;

        envRenderer.view.getOrthographicCamera().position.x = envRenderer.width / 2f;
        envRenderer.view.getOrthographicCamera().position.y = envRenderer.height / 2f;
        envRenderer.view.getOrthographicCamera() .zoom = 1f;
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
        envRenderer.map.render();
    }


    private void birth_Zone(){

    }
}
