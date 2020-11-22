package com.robomaster_libgdx.environment.simulatinglayers.baselayers;

import com.badlogic.gdx.graphics.Camera;
import com.robomaster_libgdx.environment.Environment;

public class RenderedLayer extends Layer {

    public RenderedLayer(Environment environment) {
        super(environment.view.getViewport());
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