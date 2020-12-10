package com.kristoff.robomaster_simulator.simulations;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;
import com.kristoff.robomaster_simulator.core.Simulator;
import com.kristoff.robomaster_simulator.view.Renderer;
import com.kristoff.robomaster_simulator.view.base.layers.VisualLayer;

import static java.lang.Math.*;

public class MatrixSimulation {
    Simulator simulator;
    PhysicalSimulation physicalSimulation;
    float timestate = 0f;

    public boolean[][] pointMatrix;
    public boolean[][] roboMasterMatrix;
    public MatrixSimulation(Simulator simulator) {
        this.simulator = simulator;
        this.physicalSimulation = simulator.physicalSimulation;

        pointMatrix = new boolean[8490][4890];

        addInnerBoundary();
        addBlocks();

        roboMasterMatrix = new boolean[8490][4890];
        

    }

    private void addInnerBoundary(){
        addRectangle(204,204,8080,4480);
    }

    public void addBlocks(){
        for(TextureMapObject textureMapObject : simulator.map.getBlocks()){
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
        }else{
            for(int i=Integer.parseInt(new java.text.DecimalFormat("0").format(b.x));i<a.x;i++){
                basematric[i][(Integer.parseInt(new java.text.DecimalFormat("0").format(b.y)) + middletan)] = true;
            }
        }
    }

    private void addRoboMaster(double angle, Vector2 center, int width, int height, boolean[][] basematric){
        double crossAngle = atan(height/width);
        Vector2 a = roboSidePoint(angle, center, width, height);
        double AngleB = angle + 3.1415 - 2 * crossAngle;
        Vector2 b = roboSidePoint(angle + AngleB, center, width, height);
        double AngleC = crossAngle + 3.1415;
        Vector2 c = roboSidePoint(AngleC, center, width, height);
        double AngleD = AngleB + 3.1415;
        Vector2 d = roboSidePoint(AngleD, center, width, height);


        addLineByTwoPoint(a, b, basematric);
        addLineByTwoPoint(b, c, basematric);
        addLineByTwoPoint(c, d, basematric);
        addLineByTwoPoint(d, a, basematric);
    }

    private void a(){

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
