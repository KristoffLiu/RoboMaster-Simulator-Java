package com.kristoff.robomaster_simulator.systems.matrixsimulation;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.robomasters.RoboMasters;
import com.kristoff.robomaster_simulator.systems.simulators.PhysicalSimulator;
import com.kristoff.robomaster_simulator.systems.simulators.Simulator;

public class MatrixSimulator extends Simulator {
    PhysicalSimulator physicalSimulator;

    public MatrixPointStatus[][] pointMatrix;
    public MatrixPointStatus[][] staticObjectPointMatrix;

    public MatrixSimulator() {
        delta = 1/60f;
        isStep = true;

        this.physicalSimulator = Systems.physicalSimulator;

        pointMatrix = new MatrixPointStatus[8490][4890];
        //pointMatrix = new MatrixPointStatus[8080][4480];
        staticObjectPointMatrix = new MatrixPointStatus[8490][4890];


    }

    @Override
    public void start(){
        addInnerBoundary();
        addBlocks();
        pointMatrix = staticObjectPointMatrix.clone();
        super.start();
    }

    public void step(){

    }

    public enum MatrixPointStatus {
        Empty,
        StaticObject,
        Blue1,
        Blue2,
        Red1,
        Red2
    }



    public static boolean isPointNotEmpty(int x, int y){
        if(Systems.matrixSimulator.pointMatrix[x][y] == MatrixPointStatus.Empty || Systems.matrixSimulator.pointMatrix[x][y] == null){
            return false;
        }
        else {
            return true;
        }
    }

    public boolean isPointNotEmpty(int x, int y, MatrixPointStatus pointStatus){
        if(this.pointMatrix[x][y] == MatrixPointStatus.Empty
                || this.pointMatrix[x][y] == null
                || this.pointMatrix[x][y] == pointStatus){
            return false;
        }
        else {
            return true;
        }
    }

    public static RoboMasterPoint getRoboMasterPoint(int x, int y){
        return new RoboMasterPoint(x,y,Systems.matrixSimulator.pointMatrix[x][y]);
    }

    public MatrixPointStatus getPoint(int x, int y){
        return Systems.matrixSimulator.pointMatrix[x][y];
    }

    public MatrixPointStatus[][] getMatrix() {
        return pointMatrix;
    }

    private void addInnerBoundary(){
//        addRectangle(204,204,8080,4480);
        addBlock(0, 0, 204, 4890);
        addBlock(205, 0, 8080, 204);
        addBlock(205, 4685, 8080, 205);
        addBlock(8284, 0, 204, 4890);
    }

    private void addBlocks(){
        for(TextureMapObject textureMapObject : Systems.map.getBlocks()){
            float x = textureMapObject.getX();
            float y = textureMapObject.getY();
            float width = textureMapObject.getTextureRegion().getRegionWidth();
            float height = textureMapObject.getTextureRegion().getRegionHeight();;
//            addRectangle( x,  y,  width, height);
            addBlock( x,  y,  width, height);
        }
    }

    private void addRectangle(float x, float y, float width, float height){
        addRectangle((int) x, (int) y, (int) width, (int) height);
    }

    private void addRectangle(int x, int y, int width, int height){
        for(int i=x;i<x+width;i++){
            for(int j=y;j<y+height;j++){
                if((i == x || i == x+width-1) && ( j >= y && j <= y+height-1)){
                    staticObjectPointMatrix[i][j] = MatrixPointStatus.StaticObject;
                }
                else if((i > x && i < x+width-1) && ( j == y || j == y+height-1)){
                    staticObjectPointMatrix[i][j] = MatrixPointStatus.StaticObject;
                }
                else {
                    staticObjectPointMatrix[i][j] = MatrixPointStatus.Empty;
                }
            }
        }
    }

    private void addBlock(float x, float y, float width, float height){
        addBlock((int) x, (int) y, (int) width, (int) height);
    }

    private void addBlock(int x, int y, int width, int height){
        for(int i=x;i<x+width-1;i++){
            for(int j=y;j<y+height-1;j++){
                staticObjectPointMatrix[i][j] = MatrixPointStatus.StaticObject;
                }
            }
    }
}
