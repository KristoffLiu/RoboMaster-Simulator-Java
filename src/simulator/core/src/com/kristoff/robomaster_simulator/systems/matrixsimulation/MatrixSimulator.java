package com.kristoff.robomaster_simulator.systems.matrixsimulation;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMasters;
import com.kristoff.robomaster_simulator.systems.simulators.PhysicalSimulator;
import com.kristoff.robomaster_simulator.systems.simulators.Simulator;

public class MatrixSimulator extends Simulator {
    PhysicalSimulator physicalSimulator;

    public MatrixPointStatus[][] pointMatrix;
    public MatrixPointStatus[][] staticObjectPointMatrix;

    Runnable runnable;

    public MatrixSimulator() {
        delta = 1/60f;
        isStep = true;

        this.physicalSimulator = Systems.physicalSimulator;

        pointMatrix = new MatrixPointStatus[8490][4890];
        //pointMatrix = new MatrixPointStatus[8080][4480];
        staticObjectPointMatrix = new MatrixPointStatus[8490][4890];

        addInnerBoundary();
        addBlocks();
        pointMatrix = staticObjectPointMatrix.clone();

        runnable = new Runnable() {
            @Override
            public void run() {
                for(RoboMasterPoint point : Systems.roboMasters.getPreviousPoints()){
                    pointMatrix[point.x][point.y] = MatrixPointStatus.Empty;
                }
                Systems.roboMasters.stepMatrix();
                for(RoboMasterPoint point : Systems.roboMasters.getCurrentPoints()){
                    pointMatrix[point.x][point.y] = point.status;
                }
            }
        };
    }

    public void step(){
        runnable.run();
    }

    private void addInnerBoundary(){
        addRectangle(204,204,8080,4480);
    }

    private void addBlocks(){
        for(TextureMapObject textureMapObject : Systems.map.getBlocks()){
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

    public enum MatrixPointStatus {
        Empty,
        StaticObject,
        TeamRed,
        TeamBlue
    }

    public static boolean isPointNotEmpty(int x, int y){
        if(Systems.matrixSimulator.pointMatrix[x][y] == MatrixPointStatus.Empty || Systems.matrixSimulator.pointMatrix[x][y] == null){
            return false;
        }
        else {
            return true;
        }
    }

    public static RoboMasterPoint getPoint(int x, int y){
        return new RoboMasterPoint(x,y,Systems.matrixSimulator.pointMatrix[x][y]);
    }
}
