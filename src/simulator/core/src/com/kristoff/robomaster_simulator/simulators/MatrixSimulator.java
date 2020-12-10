package com.kristoff.robomaster_simulator.simulators;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.kristoff.robomaster_simulator.environment.Environment;
import com.kristoff.robomaster_simulator.robomasters.RoboMasters;
import com.kristoff.robomaster_simulator.robomasters.types.RoboMaster;

import static java.lang.Math.*;

public class MatrixSimulator extends Simulator{
    Environment environment;
    PhysicalSimulator physicalSimulator;
    float timestate = 0f;

    public boolean[][] pointMatrix;
    public Array<boolean[][]> roboMasterMatrixes;

    public MatrixSimulator(Environment environment) {
        this.environment = environment;
        this.physicalSimulator = environment.physicalSimulator;

        pointMatrix = new boolean[8490][4890];

        addInnerBoundary();
        addBlocks();

        roboMasterMatrixes = new Array<>();
        for(int i = 0;i < 4; i++){
            roboMasterMatrixes.add(new boolean[8490][4890]);
        }
    }

    public void step(){
        updateRoboMasters();
    }

    private void addInnerBoundary(){
        addRectangle(204,204,8080,4480);
    }

    private void addBlocks(){
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

    private Vector2 roboSidePoint(double angle, Vector2 center, int width, int height){
        double anglein = atan(height/width);
        double halfcross = width * width + height * height;
        double resultx = center.x + sin(angle + anglein) * halfcross;
        double resulty = center.y + cos(angle + anglein) * halfcross;
        return new Vector2(Integer.parseInt(new java.text.DecimalFormat("0").format(resultx)),Integer.parseInt(new java.text.DecimalFormat("0").format(resulty)));
    }

    private void addLineByTwoPoint(Vector2 a, Vector2 b, boolean[][] basematric){
        int middletan = Integer.parseInt(new java.text.DecimalFormat("0").format((a.y - b.y)/(a.x - b.x)));
        if(a.x<b.x){
            for(int i=Integer.parseInt(new java.text.DecimalFormat("0").format(a.x));i<b.x;i++){
                basematric[i][(Integer.parseInt(new java.text.DecimalFormat("0").format(a.y)) + middletan)] = true;
            }
        }
        else{
            for(int i=Integer.parseInt(new java.text.DecimalFormat("0").format(b.x));i<a.x;i++){
                basematric[i][(Integer.parseInt(new java.text.DecimalFormat("0").format(b.y)) + middletan)] = true;
            }
        }
    }

    private void updateRoboMasters(){
        RoboMasters.all.get(1).matrix.step();
        RoboMasters.all.get(2).matrix.step();
        RoboMasters.all.get(3).matrix.step();
    }

    private void flushArea(){
        for(int i=205;i<8284;i++){
            for(int j=205;j<4684;j++){
                pointMatrix[i][j] = false;
            }
        }
    }


    public boolean isPointContained(int x, int y){
        if(pointMatrix[x][y]
           || RoboMasters.all.get(1).matrix.getMatrix()[x][y]
           || RoboMasters.all.get(2).matrix.getMatrix()[x][y]
           || RoboMasters.all.get(3).matrix.getMatrix()[x][y]
           ){
            return true;
        }
        else {
            return false;
        }
    }

}
