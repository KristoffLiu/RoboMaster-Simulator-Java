package com.kristoff.robomaster_simulator.core.simulator.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.kristoff.robomaster_simulator.core.simulator.Renderer;

public class StandardCompetitionMap2020 extends Map {
    Renderer renderer;
    private final float scale = 1f/1000f;

    private TiledMap map;
    private TmxMapLoader loader;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    public StandardCompetitionMap2020(Renderer renderer){
        this.renderer = renderer;
        loader      = new TmxMapLoader();
        map         = loader.load("Map/CompetitionMap/map.tmx");
        this.orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(map,scale);
    }

    public Array<TextureMapObject> getBlocks(){
        Array<TextureMapObject> textureMapObjects = new Array<>();
        MapLayer blocksLayer = this.map.getLayers().get("Block_Zone");
        for(MapObject mapObject : blocksLayer.getObjects()){
            textureMapObjects.add((TextureMapObject) mapObject);
        }
        return textureMapObjects;
    }

    public Array<TextureMapObject> getBirthZone(){
        Array<TextureMapObject> textureMapObjects = new Array<>();
        MapLayer blocksLayer = this.map.getLayers().get("Birth_Zone");
        for(MapObject mapObject : blocksLayer.getObjects()){
            textureMapObjects.add((TextureMapObject) mapObject);
        }
        return textureMapObjects;
    }

    public Array<TextureMapObject> getBuffZone(){
        Array<TextureMapObject> textureMapObjects = new Array<>();
        MapLayer blocksLayer = this.map.getLayers().get("Buff_Zone");
        for(MapObject mapObject : blocksLayer.getObjects()){
            textureMapObjects.add((TextureMapObject) mapObject);
        }
        return textureMapObjects;
    }

    public void render(){
        orthogonalTiledMapRenderer.setView((OrthographicCamera) renderer.view.getOrthographicCamera());
        orthogonalTiledMapRenderer.render();
    }
}
