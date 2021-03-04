package com.kristoff.robomaster_simulator.systems.pointsimulator;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.simulators.PhysicalSimulator;
import com.kristoff.robomaster_simulator.systems.simulators.Simulator;

import java.util.List;

public class PointSimulator extends Simulator {
    PhysicalSimulator physicalSimulator;

    public PointStatus[][] pointMatrix;
    public PointStatus[][] staticObjectPointMatrix;
    public List<PointStatus> staticObjectPointsList;

    public PointSimulator() {
        delta = 1/60f;
        isStep = true;

        this.physicalSimulator = Systems.physicalSimulator;

        pointMatrix = new PointStatus[849][489];
        staticObjectPointMatrix = new PointStatus[849][489];
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

    public enum PointStatus {
        Empty,
        StaticObject,
        Blue1,
        Blue2,
        Red1,
        Red2
    }

    public static boolean isPointNotEmpty(int x, int y){
        if(Systems.pointSimulator.pointMatrix[x][y] == PointStatus.Empty || Systems.pointSimulator.pointMatrix[x][y] == null){
            return false;
        }
        else {
            return true;
        }
    }

    public boolean isPointNotEmpty(int x, int y, PointStatus pointStatus){
        if(this.pointMatrix[x][y] == PointStatus.Empty
                || this.pointMatrix[x][y] == null
                || this.pointMatrix[x][y] == pointStatus){
            return false;
        }
        else {
            return true;
        }
    }

    public boolean isPointNotEmpty(int x, int y, PointStatus pointStatus, PointStatus pointStatus2){
        if(this.pointMatrix[x][y] == PointStatus.Empty
                || this.pointMatrix[x][y] == null
                || this.pointMatrix[x][y] == pointStatus
                || this.pointMatrix[x][y] == pointStatus2){
            return false;
        }
        else {
            return true;
        }
    }

    public static StatusPoint getRoboMasterPoint(int x, int y){
        return new StatusPoint(x,y,Systems.pointSimulator.pointMatrix[x][y]);
    }

    public PointStatus getPoint(int x, int y){
        return Systems.pointSimulator.pointMatrix[x][y];
    }

    public PointStatus[][] getMatrix() {
        return pointMatrix;
    }

    public void updatePoint(int x, int y, PointStatus status) {
        if(getMatrix()[x][y] != PointStatus.StaticObject){
            getMatrix()[x][y] = status;
        }
    }

    private void addInnerBoundary(){
//        addRectangle(204,204,8080,4480);
        addBlock(0, 0, 20, 489, 0);
        addBlock(20, 0, 808, 20, 0);
        addBlock(20, 468, 808, 20, 0);
        addBlock(828, 0, 20, 489, 0);
    }

    private void addBlocks(){
        for(TextureMapObject textureMapObject : Systems.map.getBlocks()){
            float x = textureMapObject.getX() / 10;
            float y = textureMapObject.getY() / 10;
            float width = textureMapObject.getTextureRegion().getRegionWidth() / 10;
            float height = textureMapObject.getTextureRegion().getRegionHeight() / 10;
//            addRectangle( x,  y,  width, height);
            addBlock( (int)x,  (int)y,  (int)width, (int)height, textureMapObject.getRotation());
        }
    }

    private void addRectangle(float x, float y, float width, float height){
        addRectangle((int) x, (int) y, (int) width, (int) height);
    }

    private void addRectangle(int x, int y, int width, int height){
        for(int i=x;i<x+width;i++){
            for(int j=y;j<y+height;j++){
                if((i == x || i == x+width-1) && ( j >= y && j <= y+height-1)){
                    staticObjectPointMatrix[i][j] = PointStatus.StaticObject;
                }
                else if((i > x && i < x+width-1) && ( j == y || j == y+height-1)){
                    staticObjectPointMatrix[i][j] = PointStatus.StaticObject;
                }
                else {
                    staticObjectPointMatrix[i][j] = PointStatus.Empty;
                }
            }
        }
    }

    private void addBlock(int x, int y, int width, int height, float radian){
        if(radian == 0){
            for(int i=(int)x;i<x+width-1;i++){
                for(int j=y;j<y+height-1;j++){
                    staticObjectPointMatrix[i][j] = PointStatus.StaticObject;
                }
            }
        }
        else{
            for(int i = 0; i <= (25 * Math.sqrt(2)); i++){
                if(i <= (25 / Math.sqrt(2))){
                    for(int j = 0; j <= i ; j++){
                        staticObjectPointMatrix[i + 407][245 + j] = PointStatus.StaticObject;
                        staticObjectPointMatrix[i + 407][245 - j] = PointStatus.StaticObject;
                    }
                }
                else{
                    for(int j = 0; j <= 25 * Math.sqrt(2) - i; j++){
                        staticObjectPointMatrix[i + 407][245 + j] = PointStatus.StaticObject;
                        staticObjectPointMatrix[i + 407][245 - j] = PointStatus.StaticObject;
                    }
                }
            }
        }
    }
}
