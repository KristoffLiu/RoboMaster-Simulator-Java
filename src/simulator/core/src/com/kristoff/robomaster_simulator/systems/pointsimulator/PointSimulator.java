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

        pointMatrix = new PointStatus[8490][4890];
        staticObjectPointMatrix = new PointStatus[8490][4890];
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

    public boolean isPointNotEmptyLowResolution(int lowX, int lowY, PointStatus pointStatus){
        int x = lowX * 10;
        int y = lowY * 10;
        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                if(this.pointMatrix[x][y] == PointStatus.Empty
                        || this.pointMatrix[x][y] == null
                        || this.pointMatrix[x][y] == pointStatus){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isPointNotEmptyLowResolution(int lowX, int lowY, PointStatus pointStatus, PointStatus pointStatus2){
        int x = lowX * 10;
        int y = lowY * 10;
        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                if(this.pointMatrix[x][y] == PointStatus.Empty
                        || this.pointMatrix[x][y] == null
                        || this.pointMatrix[x][y] == pointStatus
                        || this.pointMatrix[x][y] == pointStatus2){
                    return false;
                }
            }
        }
        return true;
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

    private void addBlock(float x, float y, float width, float height){
        addBlock((int) x, (int) y, (int) width, (int) height);
    }

    private void addBlock(int x, int y, int width, int height){
        for(int i=x;i<x+width-1;i++){
            for(int j=y;j<y+height-1;j++){
                staticObjectPointMatrix[i][j] = PointStatus.StaticObject;
            }
        }
    }
}
