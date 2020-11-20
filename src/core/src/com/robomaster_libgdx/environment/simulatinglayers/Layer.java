package com.robomaster_libgdx.environment.simulatinglayers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.sun.org.apache.bcel.internal.generic.RETURN;

public class Layer extends Stage {

    public Layer(StretchViewport stretchViewport){
        super(stretchViewport);
    }


    public void setCamera(float x, float y){
        this.getViewport().getCamera().position.set(x,y,0);
        this.updateCamera();
    }

    public void setCamera(float x, float y, float z){
        this.getViewport().getCamera().position.set(x,y,z);
        this.updateCamera();
    }

    public Camera getCamera(){
        return this.getViewport().getCamera();
    }

    public void updateCamera(){
        this.getViewport().getCamera().update();
    }
}
