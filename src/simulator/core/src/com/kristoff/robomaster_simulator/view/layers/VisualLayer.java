package com.kristoff.robomaster_simulator.view.layers;

import com.badlogic.gdx.graphics.Camera;
import com.kristoff.robomaster_simulator.view.renderers.EnvRenderer;

public class VisualLayer extends Layer {

    Runnable runnable;
    public VisualLayer(EnvRenderer envRenderer) {
        super(envRenderer.view.getViewport(), envRenderer);
        runnable = new Runnable() {
            @Override
            public void run() {
                drawAsync();
            }
        };
    }

    @Override
    public void act (float delta) {
        super.act(delta);
    }

    @Override
    public void draw () {
        runnable.run();
    }

    public void drawAsync(){
        super.draw();
    }

    public void setCamera(float x, float y, float z){
        this.getViewport().getCamera().position.set(x,y,z);
        this.updateCamera();
    }

    public Camera getCamera(){
        return this.getViewport().getCamera();
    }


    public void updateCamera(){
        //this.setCamera(camera.position.x, camera.position.y, camera.position.z);
        //this.getViewport().getCamera().update();
    }
}
