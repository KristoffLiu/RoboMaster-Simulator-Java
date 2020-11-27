package com.robomaster_libgdx.environment.simulatinglayers;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.robomaster_libgdx.environment.Environment;
import com.robomaster_libgdx.environment.simulatinglayers.baselayers.VisualLayer;

public class MatrixLayer extends VisualLayer {
    float timestate = 0f;

    public boolean[][] pointMatrix;
    public Array<Vector2> pointMatrixArray;
    public MatrixLayer(Environment environment) {
        super(environment);

        pointMatrix = new boolean[8490][4890];
        pointMatrixArray = new Array<>();
        addInnerBoundary2();
        addBlocks2();
        //addInnerBoundary();
        //addBlocks();
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





    private void addInnerBoundary2(){
        addRectangle2(204,204,8080,4480);
    }

    public void addBlocks2(){
        for(TextureMapObject textureMapObject : environment.map.getBlocks()){
            float x = textureMapObject.getX();
            float y = textureMapObject.getY();
            float width = textureMapObject.getTextureRegion().getRegionWidth();
            float height = textureMapObject.getTextureRegion().getRegionHeight();;
            addRectangle2( x,  y,  width, height);
        }
    }

    private void addRectangle2(float x, float y, float width, float height){
        addRectangle((int) x, (int) y, (int) width, (int) height);
    }

    private void addRectangle2(int x, int y, int width, int height){
        for(int i=x;i<x+width;i++){
            for(int j=y;j<y+height;j++){
                if((i == x || i == x+width-1) && ( j >= y && j <= y+height-1)){
                    pointMatrixArray.add(new Vector2(i,j));
                }
                else if((i > x && i < x+width-1) && ( j == y || j == y+height-1)){
                    pointMatrixArray.add(new Vector2(i,j));
                }
                else {
                    pointMatrixArray.add(new Vector2(i,j));
                }
            }
        }
    }


}
