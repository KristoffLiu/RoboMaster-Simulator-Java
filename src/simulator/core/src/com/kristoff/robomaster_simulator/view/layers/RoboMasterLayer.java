package com.kristoff.robomaster_simulator.view.layers;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.kristoff.robomaster_simulator.robomasters.robomaster.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.robomaster.modules.RendererInputListener;
import com.kristoff.robomaster_simulator.robomasters.teams.RoboMasters;
import com.kristoff.robomaster_simulator.view.actors.CustomActor;
import com.kristoff.robomaster_simulator.view.layers.VisualLayer;
import com.kristoff.robomaster_simulator.view.renderers.EnvRenderer;

public class RoboMasterLayer extends VisualLayer {

    public RoboMasterLayer(EnvRenderer envRenderer) {
        super(envRenderer);
        renderRoboMasters();
    }

    public void renderRoboMasters() {
        for (RoboMaster roboMaster : RoboMasters.all) {
            roboMaster.renderer.addListener(new RendererInputListener(roboMaster));
            this.addActor(roboMaster.renderer);
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
