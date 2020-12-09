package com.kristoff.robomaster_simulator.view.base.layers;

import com.badlogic.gdx.graphics.Camera;
import com.kristoff.robomaster_simulator.view.Renderer;

public class VisualLayer extends Layer {

    public VisualLayer(Renderer renderer) {
        super(renderer.view.getViewport(), renderer);
    }

    @Override
    public void act (float delta) {
        super.act(delta);
    }

    @Override
    public void draw () {
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
