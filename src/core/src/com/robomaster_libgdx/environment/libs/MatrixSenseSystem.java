package com.robomaster_libgdx.environment.libs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.robomaster_libgdx.environment.Environment;
import org.graalvm.compiler.loop.MathUtil;

import java.util.Arrays;

public class MatrixSenseSystem {
    Environment environment;
    ShapeRenderer shapeRenderer;
    public boolean[][] pointMatrix;
    public boolean[][] demoMatrix;


    float scale = 1f / 1000f;
    public MatrixSenseSystem(Environment environment) {
        this.environment = environment;
        shapeRenderer = new ShapeRenderer();
        pointMatrix = new boolean[8490][4890];
        demoMatrix = new boolean[8490][4890];

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

    float timestate = 0;
    public void update(float delta){
        //flushArea();
        if(timestate > 1/20f){
            demoMatrix = lidarPointCloudSimulate(
                    environment.teamBlue.get(0).getLidarPosition().x,
                    environment.teamBlue.get(0).getLidarPosition().y);
            timestate = 0f;
        }
        else{
            timestate += delta;
        }
    }

    public boolean[][] lidarPointCloudSimulate(float c_x, float c_y){
        boolean[][] matrix = new boolean[8490][4890];
        int centre_x = (int) (c_x * 1000);
        int centre_y = (int) (c_y * 1000);
        float precisionOfDegree = 0.5f;
        int x = 0;
        int y = 0;
        for(float degree = 0;degree < 360; degree += precisionOfDegree){
            float radian = degree * MathUtils.degreesToRadians;
            if(degree == 0 || degree == 180){
                for(y = 0;y <4800; y++){
                    if(degree == 0){
                        matrix[centre_x][centre_y + y] = isPointContained(centre_x, centre_y + y);
                        if(isPointContained(centre_x, centre_y + y)){
                            break;
                        }
                    }
                    else {
                        matrix[centre_x][centre_y - y] = isPointContained(centre_x, centre_y - y);
                        if(isPointContained(centre_x, centre_y - y)){
                            break;
                        }
                    }
                }
            }
            else if(degree == 90 || degree == 270){
                for(x = 0;x <8400; x++){
                    if(degree == 90){
                        matrix[centre_x + x][centre_y] = isPointContained(centre_x + x, centre_y);
                        if(isPointContained(centre_x + x, centre_y)){
                            break;
                        }
                    }
                    else {
                        matrix[centre_x - x][centre_y] = isPointContained(centre_x - x, centre_y);
                        if(isPointContained(centre_x - x, centre_y)){
                            break;
                        }
                    }
                }
            }
            else if(degree > 315 || degree < 45 || (degree > 135 && degree < 225)){
                for(y = 0;y <4800; y++){
                    int offset_x = (int) (Math.tan(radian) * y);
                    int offset_y = y;
                    if(degree > 135 && degree < 180){
                        offset_x = - offset_x;
                        offset_y = - offset_y;
                    }
                    else if(degree > 180 && degree < 225){
                        offset_x = - offset_x;
                        offset_y = - offset_y;
                    }
                    else{
                        offset_x = - offset_x;
                    }
                    matrix[centre_x + offset_x][centre_y + offset_y] = isPointContained(centre_x + offset_x, centre_y + offset_y);
                    if(isPointContained(centre_x + offset_x, centre_y + offset_y)){
                        break;
                    }
                }
            }
            else{
                for(x = 0;x <8400; x++){
                    int offset_x = x;
                    int offset_y = (int) (x / Math.tan(radian));
                    if(degree <= 135){
                        offset_y = - offset_y;
                    }
                    else if(degree >= 225){
                        offset_x = - offset_x;
                        offset_y = - offset_y;
                    }
                    else if(degree <= 315){
                        offset_x = - offset_x;
                    }
                    matrix[centre_x + offset_x][centre_y + offset_y] = isPointContained(centre_x + offset_x, centre_y + offset_y);
                    if(isPointContained(centre_x + offset_x, centre_y + offset_y)){
                        break;
                    }
                }
            }
            x = 0;
            y = 0;
        }
        return matrix;
    }

    public boolean isPointContained(int x, int y){
        return pointMatrix[x][y];
    }

    public boolean[][] getDemoMatrix(){
        return demoMatrix;
    }
}
