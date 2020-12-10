package com.kristoff.robomaster_simulator.simulators;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.kristoff.robomaster_simulator.environment.Environment;
import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.RoboMasters;
import com.kristoff.robomaster_simulator.robomasters.modules.simulations.RoboMasterPoint;

import static java.lang.Math.*;

public class MatrixSimulator extends Simulator{
    public static MatrixSimulator current;

    Environment environment;
    PhysicalSimulator physicalSimulator;
    float timestate = 0f;

    public MatrixPointStatus[][] pointMatrix;
    public MatrixPointStatus[][] staticObjectPointMatrix;

    Runnable runnable;

    public MatrixSimulator(Environment environment) {
        current = this;
        this.environment = environment;
        this.physicalSimulator = environment.physicalSimulator;

        pointMatrix = new MatrixPointStatus[8490][4890];
        //pointMatrix = new MatrixPointStatus[8080][4480];
        staticObjectPointMatrix = new MatrixPointStatus[8490][4890];

        addInnerBoundary();
        addBlocks();
        pointMatrix = staticObjectPointMatrix.clone();

        runnable = new Runnable() {
            @Override
            public void run() {
                for(RoboMasterPoint point : RoboMasters.getPreviousPoints()){
                    pointMatrix[point.x][point.y] = MatrixPointStatus.Empty;
                }
                RoboMasters.stepMatrix();
                for(RoboMasterPoint point : RoboMasters.getCurrentPoints()){
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
        if(current.pointMatrix[x][y] == MatrixPointStatus.Empty || current.pointMatrix[x][y] == null){
            return false;
        }
        else {
            return true;
        }
    }

    public static RoboMasterPoint getPoint(int x, int y){
        return new RoboMasterPoint(x,y,current.pointMatrix[x][y]);
    }
}
