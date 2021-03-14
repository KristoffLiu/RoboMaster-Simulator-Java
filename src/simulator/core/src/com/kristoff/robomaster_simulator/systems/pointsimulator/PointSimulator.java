package com.kristoff.robomaster_simulator.systems.pointsimulator;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.simulators.PhysicalSimulator;
import com.kristoff.robomaster_simulator.systems.simulators.Simulator;

import java.util.List;

public class PointSimulator extends Simulator {
    PhysicalSimulator physicalSimulator;

    public PointState[][] pointMatrix;
    public PointState[][] staticObjectPointMatrix;
    public List<PointState> staticObjectPointsList;

    public PointSimulator() {
        delta = 1/60f;
        isStep = true;

        this.physicalSimulator = Systems.physicalSimulator;

        pointMatrix = new PointState[849][489];
        staticObjectPointMatrix = new PointState[849][489];
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

    public static boolean isPointNotEmpty(int x, int y){
        if(Systems.pointSimulator.pointMatrix[x][y] == PointState.Empty || Systems.pointSimulator.pointMatrix[x][y] == null){
            return false;
        }
        else {
            return true;
        }
    }

    public static boolean isPoiontInsideMap(int x, int y){
        return !(x < 20 || x > 829 || y < 20 || y > 469);
    }

    public boolean isPointEmpty(int x, int y, PointState pointState){
        return     this.pointMatrix[x][y] == PointState.Empty
                || this.pointMatrix[x][y] == null
                || this.pointMatrix[x][y] == pointState;
    }

    public boolean isPointNotEmpty(int x, int y, PointState pointState){
//        if(!isPoiontInsideMap(x, y)) return false;
        if(this.pointMatrix[x][y] == PointState.Empty
                || this.pointMatrix[x][y] == null
                || this.pointMatrix[x][y] == pointState){
            return false;
        }
        else {
            return true;
        }
    }

    public void clearPointStatus(PointState pointState){
        int m = 0;
        int n = 0;
        int count = 0;
        for(int i = 0; i < 849; i++){
            for(int j = 0; j < 489; j++){
                if(this.pointMatrix[i][j] == pointState){
                    count ++;
                }
                if(count >= 25){
                    m = i;
                    n = j;
                    break;
                }
            }
            if(count >= 25){
                break;
            }
        }
        for(int i = -50; i < 50; i++){
            for(int j = -50; j < 40; j++){
                if(this.pointMatrix[i][j] == pointState){
                    this.pointMatrix[i][j] = PointState.Empty;
                }
            }
        }
    }

    public boolean isPointNotEmpty(int x, int y, PointState pointState, PointState pointState2){
        if(!isPoiontInsideMap(x, y)) return false;
        if(this.pointMatrix[x][y] == PointState.Empty
                || this.pointMatrix[x][y] == null
                || this.pointMatrix[x][y] == pointState
                || this.pointMatrix[x][y] == pointState2){
            return false;
        }
        else {
            return true;
        }
    }

    public boolean isPointNotEmpty(int x, int y, PointState self1, PointState self2, PointState enemyPoint) {
        if(!isPoiontInsideMap(x, y)) return false;
        if(this.pointMatrix[x][y] == PointState.Empty
                || this.pointMatrix[x][y] == null
                || this.pointMatrix[x][y] == self1
                || this.pointMatrix[x][y] == self2
                || this.pointMatrix[x][y] == enemyPoint){
            return false;
        }
        else {
            return true;
        }
    }

    public boolean isPointOnEnemies(int x, int y){
        return this.pointMatrix[x][y] == PointState.Red1 || this.pointMatrix[x][y] == PointState.Red2;
    }


    public static StatePoint getRoboMasterPoint(int x, int y){
        return new StatePoint(x,y,Systems.pointSimulator.pointMatrix[x][y]);
    }

    public PointState getPoint(int x, int y){
        return Systems.pointSimulator.pointMatrix[x][y];
    }

    public PointState[][] getMatrix() {
        return pointMatrix;
    }

    public void updatePoint(int x, int y, PointState status) {
        try{
            if(!isPoiontInsideMap(x, y)) return;
            if(getMatrix()[x][y] != PointState.StaticObject){
                getMatrix()[x][y] = status;
            }
        }
        catch(ArrayIndexOutOfBoundsException e){
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
                    staticObjectPointMatrix[i][j] = PointState.StaticObject;
                }
                else if((i > x && i < x+width-1) && ( j == y || j == y+height-1)){
                    staticObjectPointMatrix[i][j] = PointState.StaticObject;
                }
                else {
                    staticObjectPointMatrix[i][j] = PointState.Empty;
                }
            }
        }
    }

    private void addBlock(int x, int y, int width, int height, float radian){
        if(radian == 0){
            for(int i=(int)x;i<x+width;i++){
                for(int j=y;j<y+height;j++){
                    staticObjectPointMatrix[i][j] = PointState.StaticObject;
                }
            }
        }
        else{
            for(int i = 0; i <= (25 * Math.sqrt(2)); i++){
                if(i <= (25 / Math.sqrt(2))){
                    for(int j = 0; j <= i ; j++){
                        staticObjectPointMatrix[i + 407][245 + j] = PointState.StaticObject;
                        staticObjectPointMatrix[i + 407][245 - j] = PointState.StaticObject;
                    }
                }
                else{
                    for(int j = 0; j <= 25 * Math.sqrt(2) - i; j++){
                        staticObjectPointMatrix[i + 407][245 + j] = PointState.StaticObject;
                        staticObjectPointMatrix[i + 407][245 - j] = PointState.StaticObject;
                    }
                }
            }
        }
    }
}
