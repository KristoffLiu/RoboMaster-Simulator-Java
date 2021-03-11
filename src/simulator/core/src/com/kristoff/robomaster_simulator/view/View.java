package com.kristoff.robomaster_simulator.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.*;

public class View{
    private OrthographicCamera camera;

    Viewport viewport;

    boolean isZooming                   = false;
    private final float zoomTimeLock    = 0.1f;
    private final float zoomMax         = 400f;
    private final float zoomMin         = 0.4f;
    private       float zoomOffset = 0.001f;
    private       float zoomTimeCount   = 0f;

    boolean isHorizontallyTranslating   = false;
    private final float horizontalTranslationLock       = 0.1f;
    private final float horizontalTranslationMax        = 8.49f;
    private final float horizontalTranslationMin        = -2f;
    private       float horizontalTranslationOffset     = 0.001f;
    private       float horizontalTranslationTimeCount  = 0f;

    boolean isVerticallyTranslating   = false;
    private final float verticalTranslationLock       = 0.1f;
    private final float verticalTranslationMax        = 4.89f;
    private final float verticalTranslationMin        = -2f;
    private       float verticalTranslationOffset     = 0.001f;
    private       float verticalTranslationTimeCount  = 0f;



    public View(float width, float height){
        camera = new OrthographicCamera(width,height);
        viewport = new FitViewport(width,height,camera);
    }

    public Viewport getViewport(){
        return viewport;
    }

    public OrthographicCamera getOrthographicCamera(){
        return camera;
    }

    public void update(float deltaTime){
        updateZoom(deltaTime);
        updatePositionX(deltaTime);
        updatePositionY(deltaTime);
    }

    public void updateSize(float width,float height){
//        getViewport().setScreenWidth((int)width - 300);
//        getViewport().setScreenHeight((int)height);
//
//
//        float x = camera.position.x;
//        float y = camera.position.y;
//        getOrthographicCamera().setToOrtho (
//                true, width, height);
//        camera.position.x = x;
//        camera.position.y = y;
    }

    private void updateZoom(float deltaTime){
        if(isZooming){
            zoomTimeCount += deltaTime;

            if(camera.zoom > zoomMax){
                camera.zoom = zoomMax;
            }
            else if(camera.zoom < zoomMin){
                camera.zoom = zoomMin;
            }
            else{
                camera.zoom += zoomOffset;
            }

            if(zoomTimeCount > zoomTimeLock){
                zoomTimeCount = 0f;
                isZooming = false;
            }
        }
    }

    private void updatePositionX(float deltaTime){
        if(isHorizontallyTranslating){
            horizontalTranslationTimeCount += deltaTime;

            if(camera.position.x > horizontalTranslationMax){
                camera.position.x = horizontalTranslationMax;
            }
            else if(camera.position.x < horizontalTranslationMin){
                camera.position.x = horizontalTranslationMin;
            }
            else{
                camera.position.x += horizontalTranslationOffset * this.getZoomFactor();
            }

            if(horizontalTranslationTimeCount > horizontalTranslationLock){
                horizontalTranslationTimeCount = 0f;
                isHorizontallyTranslating = false;
            }
        }
    }

    private void updatePositionY(float deltaTime){
        if(isVerticallyTranslating){
            verticalTranslationTimeCount += deltaTime;

            if(camera.position.y > verticalTranslationMax){
                camera.position.y = verticalTranslationMax;
            }
            else if(camera.position.y < verticalTranslationMin){
                camera.position.y = verticalTranslationMin;
            }
            else{
                camera.position.y += verticalTranslationOffset * this.getZoomFactor();
            }

            if(verticalTranslationTimeCount > verticalTranslationLock){
                verticalTranslationTimeCount = 0f;
                isVerticallyTranslating = false;
            }
        }
    }

    private float getZoomFactor(){
        return camera.zoom / 7.5f;
    }

    public void setHorizontalTranslation(boolean bool){
        if(bool){
            this.horizontalTranslationOffset = 0.6f;
        }
        else{
            this.horizontalTranslationOffset = -0.6f;
        }
        isHorizontallyTranslating = true;
    }

    public void setVerticalTranslation(boolean bool){
        if(bool){
            this.verticalTranslationOffset = 0.6f;
        }
        else{
            this.verticalTranslationOffset = -0.6f;
        }
        isVerticallyTranslating = true;
    }

    public void setZoom(float zoomOffset){
        if(zoomOffset > 0){
            this.zoomOffset = 0.02f;
        }
        else if(zoomOffset < 0){
            this.zoomOffset = -0.02f;
        }
        isZooming = true;
    }
}
