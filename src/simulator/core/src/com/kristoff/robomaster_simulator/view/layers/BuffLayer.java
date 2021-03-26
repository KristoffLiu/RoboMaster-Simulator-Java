package com.kristoff.robomaster_simulator.view.layers;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.buffs.BuffZone;
import com.kristoff.robomaster_simulator.view.actors.CustomActor;
import com.kristoff.robomaster_simulator.view.renderers.EnvRenderer;
import org.lwjgl.Sys;

public class BuffLayer extends VisualLayer {

    public BuffLayer(EnvRenderer envRenderer) {
        super(envRenderer);
        addBuffZones();
    }

    public void addBuffZones(){
        for(BuffZone buffZone : Systems.refree.getBuffZones()){
            this.addActor(buffZone.getBuffZoneActor());
        }
    }

    @Override
    public void act (float delta) {
        float scale = 1f / 1000f;
        super.act(delta);
    }

    @Override
    public void draw(){
        super.draw();
    }
}
