package com.kristoff.robomaster_simulator.systems.maps;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.kristoff.robomaster_simulator.core.Simulator;
import com.kristoff.robomaster_simulator.view.renderers.EnvRenderer;

public class Map {
    EnvRenderer envRenderer;
    private final float scale = 1f/1000f;

    private String mapName;
    private TiledMap map;
    private TmxMapLoader loader;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    Array<TextureMapObject> blocks;
    Array<TextureMapObject> birthZones;
    Array<TextureMapObject> buffZones;

    //"Map/CompetitionMap/map.tmx"
    public Map(String mapName){
        this.mapName = mapName;
    }

    public void start(){
        loader      = new TmxMapLoader();
        map         = loader.load(MapSets.getMapPath(mapName));
        this.orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(map,scale);

        addBlocks();
        addBirthZones();
        addBuffZones();
    }

    public void addBlocks(){
        blocks = new Array<>();
        MapLayer blocksLayer = this.map.getLayers().get("Block_Zone");
        for(MapObject mapObject : blocksLayer.getObjects()){
            blocks.add((TextureMapObject) mapObject);
        }
    }

    public void addBirthZones(){
        birthZones = new Array<>();
        MapLayer blocksLayer = this.map.getLayers().get("Birth_Zone");
        for(MapObject mapObject : blocksLayer.getObjects()){
            birthZones.add((TextureMapObject) mapObject);
        }
    }

    public void addBuffZones(){
        buffZones = new Array<>();
        MapLayer blocksLayer = this.map.getLayers().get("Buff_Zone");
        for(MapObject mapObject : blocksLayer.getObjects()){
            buffZones.add((TextureMapObject) mapObject);
        }
    }

    public Array<TextureMapObject> getBlocks(){
        return blocks;
    }

    public Array<TextureMapObject> getBirthZones(){
        return birthZones;
    }

    public Array<TextureMapObject> getBuffZones(){
        return buffZones;
    }

    public void render(){
        try{
            if(Simulator.current.envRenderer != null){
                orthogonalTiledMapRenderer.setView(Simulator.current.envRenderer.view.getOrthographicCamera());
                orthogonalTiledMapRenderer.render();
            }
        }
        finally {

        }
    }
}
