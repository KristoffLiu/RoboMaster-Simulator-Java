package com.kristoff.robomaster_simulator.view.layers;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.kristoff.robomaster_simulator.view.Renderer;
import com.kristoff.robomaster_simulator.view.base.layers.VisualLayer;

public class FloorLayer extends VisualLayer {
    Renderer renderer;

    private TiledMap map;
    private TmxMapLoader loader;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    public FloorLayer(Renderer renderer) {
        super(renderer);

        this.renderer = renderer;

        renderer.view.getOrthographicCamera().position.x = renderer.width / 2f;
        renderer.view.getOrthographicCamera().position.y = renderer.height / 2f;
        renderer.view.getOrthographicCamera().zoom = 1f;
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
        renderer.map.render();
    }


    private void birth_Zone(){

    }
}
