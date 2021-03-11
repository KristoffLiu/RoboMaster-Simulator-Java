package com.kristoff.robomaster_simulator.view.layers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.kristoff.robomaster_simulator.robomasters.robomaster.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.robomaster.modules.RendererInputListener;
import com.kristoff.robomaster_simulator.robomasters.teams.RoboMasters;
import com.kristoff.robomaster_simulator.view.actors.CustomActor;
import com.kristoff.robomaster_simulator.view.layers.VisualLayer;
import com.kristoff.robomaster_simulator.view.renderers.EnvRenderer;
import com.kristoff.robomaster_simulator.view.ui.controls.labels.LabelStylesHelper;

import java.util.ArrayList;

public class RoboMasterLayer extends VisualLayer {
    ArrayList<Label> roboMastersIDList = new ArrayList<>();
    SpriteBatch spriteBatch;

    public RoboMasterLayer(EnvRenderer envRenderer) {
        super(envRenderer);
        spriteBatch = new SpriteBatch();
        renderRoboMasters();
    }

    public void renderRoboMasters() {
        for (RoboMaster roboMaster : RoboMasters.all) {
            roboMaster.renderer.addListener(new RendererInputListener(roboMaster));
            this.addActor(roboMaster.renderer);
        }
    }

    public void addID(){

    }

    @Override
    public void act (float delta) {
        float scale = 1f / 1000f;
        super.act(delta);
    }

    @Override
    public void draw(){
        super.draw();
//        spriteBatch.begin();
//        for(int i = 0; i < RoboMasters.all.size(); i ++){
//            RoboMaster roboMaster = RoboMasters.all.get(i);
//            Label label = roboMastersIDList.get(i);
//            label.d
//        }
//        spriteBatch.end();
    }

}
