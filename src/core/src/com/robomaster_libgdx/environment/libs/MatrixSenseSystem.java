package com.robomaster_libgdx.environment.libs;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.robomaster_libgdx.environment.Environment;
import org.graalvm.compiler.loop.MathUtil;

public class MatrixSenseSystem {
    Environment environment;
    ShapeRenderer shapeRenderer;
    public boolean[][] pointMatrix;
    float scale = 1f / 1000f;
    public MatrixSenseSystem(Environment environment) {
        this.environment = environment;
        shapeRenderer = new ShapeRenderer();
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
        for(int i=x;i<x+width-1;i++){
            for(int j=y;j<y+height-1;j++){
                if((i == x || i == i+width-1) && ( j >= y && j <= y+height-1)){
                    pointMatrix[i][j] = true;
                }
                else if((i > x && i < i+width-1) && ( j == y || j == y+height-1)){
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

    public void update(){
        flushArea();
    }

    public boolean[][] lidarPointCloudSimulate(float c_x, float c_y){
        boolean[][] matrix = new boolean[8490][4890];
        int centre_x = (int) c_x;
        int centre_y = (int) c_y;
        float precisionOfDegree = 0.5f;
        for(float degree = 0;degree < 360; degree += precisionOfDegree){
            float radian = degree * MathUtils.degreesToRadians;
            if(degree == 0){
                for(int y = 0;y <9000; y++){
                    matrix[centre_x][centre_y + y] = isPointContained(centre_x, centre_y + y);
                    if(isPointContained(centre_x, centre_y + y)){
                        break;
                    }
                }
            }
            else if
            else if(degree == 90 || degree == 270){
                for(int x = 0;x <9000; x++){
                    int y = (int) Math.tan(radian) * x;
                    matrix[x][y] = isPointContained(x, y);
                    if(isPointContained(x, y)){
                        break;
                    }
                }
            }
            else if(degree > 315 || degree < 45 || (degree > 135 && degree < 225)){
                for(int y = 0;y <9000; y++){
                    int x = (int) Math.tan(radian) * y;
                    matrix[x][y] = isPointContained(x, y);
                    if(isPointContained(x, y)){
                        break;
                    }
                }
            }
            else{
                for(int x = 0;x <9000; x++){
                    int y = (int) Math.tan(radian) * x;
                    matrix[x][y] = isPointContained(x, y);
                    if(isPointContained(x, y)){
                        break;
                    }
                }
            }
        }
    }

    public boolean isPointContained(int x, int y){
        return pointMatrix[x][y];
    }
}
