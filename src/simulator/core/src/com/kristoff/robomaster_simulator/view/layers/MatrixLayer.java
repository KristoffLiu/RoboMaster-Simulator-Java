package com.kristoff.robomaster_simulator.render.layers;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.kristoff.robomaster_simulator.core.Renderer;
import com.kristoff.robomaster_simulator.render.base.layers.VisualLayer;

public class MatrixLayer extends VisualLayer {
    float timestate = 0f;

    public boolean[][] pointMatrix;
    public MatrixLayer(Renderer renderer) {
        super(renderer);

        pointMatrix = new boolean[8490][4890];


        addInnerBoundary();
        addBlocks();
    }

    private void addInnerBoundary(){
        addRectangle(204,204,8080,4480);
    }

    public void addBlocks(){
        for(TextureMapObject textureMapObject : environment.map.getBlocks()){
            float x = textureMapObject.getX();
            float y = textureMapObject.getY();
            float width = textureMapObject.getTextureRegion().getRegionWidth();
            float height = textureMapObject.getTextureRegion().getRegionHeight();;
            addRectangle( x,  y,  width, height);
        }
    }

    private void addRectangle(float x, float y, float width, float height){
        addRectangle((int) x, (int) y, (int) width, (int) height);
    }

    private void addRectangle(int x, int y, int width, int height){
        for(int i=x;i<x+width;i++){
            for(int j=y;j<y+height;j++){
                if((i == x || i == x+width-1) && ( j >= y && j <= y+height-1)){
                    pointMatrix[i][j] = true;
                }
                else if((i > x && i < x+width-1) && ( j == y || j == y+height-1)){
                    pointMatrix[i][j] = true;
                }
                else {
                    pointMatrix[i][j] = false;
                }
            }
        }
    }

    private void flushArea(){
        for(int i=205;i<8284;i++){
            for(int j=205;j<4684;j++){
                pointMatrix[i][j] = false;
            }
        }
    }

    public boolean isPointContained(int x, int y){
        return pointMatrix[x][y];
    }

}
