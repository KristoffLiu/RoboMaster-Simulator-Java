package com.robomaster_libgdx.environment.simulatinglayers;

import com.robomaster_libgdx.environment.Environment;
import com.robomaster_libgdx.environment.libs.MatrixSenseSystem;
import com.robomaster_libgdx.environment.robomasters.RoboMaster;
import com.robomaster_libgdx.environment.simulatinglayers.baselayers.VisualLayer;

import java.awt.*;
import java.util.Arrays;

public class GlobalMatrixLayer extends VisualLayer {
    MatrixSenseSystem matrixSenseSystem;
    float timestate = 0f;
    public GlobalMatrixLayer(Environment environment) {
        super(environment);
        matrixSenseSystem = environment.matrixSenseSystem;
    }

    @Override
    public void act (float delta) {
        float scale = 1f / 1000f;
        super.act(delta);
        a();
    }

    public void a(){
        for(int i=0;i<8490;i++){
            for(int j=0;j<4890;j++){
                if(matrixSenseSystem.getDemoMatrix()[i][j]) {
                    environment.pointCloudRenderer.setAutoShapeType(true);
                    Point a = new Point(i,j);
                    Point b = new Point(
                            (int)(environment.teamBlue.get(0).getLidarPosition().x * 1000),
                            (int)(environment.teamBlue.get(0).getLidarPosition().y * 1000));
                    float distance = (float) a.distance(b);
                    if(distance <= 500f){
                        environment.pointCloudRenderer.setColor(1.0f,0f,0f,1.0f);
                    }
                    else if(distance <= 1000f){
                        environment.pointCloudRenderer.setColor(0.8f,0.2f,0f,1.0f);
                    }
                    else if(distance <= 2000f){
                        environment.pointCloudRenderer.setColor(0.6f,0.4f,0f,1.0f);
                    }
                    else if(distance <= 3000f){
                        environment.pointCloudRenderer.setColor(0.4f,0.6f,0f,1.0f);
                    }
                    else if(distance <= 4000f){
                        environment.pointCloudRenderer.setColor(0.2f,0.8f,0f,1.0f);
                    }
                    else{
                        environment.pointCloudRenderer.setColor(0.0f,1.0f,0f,1.0f);
                    }
                    environment.pointCloudRenderer.circle(
                            i / 1000f,
                            j / 1000f,
                            0.05f,5);
                }
            }
        }
    }

    public void showRoboMasterLidarPointCloud(){
//        for(int i=0;i<8490;i++){
//            for(int j=0;j<4890;j++){
//                if(matrixSenseSystem.lidarPointCloudSimulate(environment.allRoboMasters.get(0).getX(), environment.teamBlue.get(0).getY())[i][j]) {
//                    environment.pointCloudRenderer.setAutoShapeType(true);
//                    environment.pointCloudRenderer.setColor(1.0f,0.5f,1.0f,1.0f);
//                    environment.pointCloudRenderer.circle(
//                            i / 1000f,
//                            j / 1000f,
//                            0.5f,10);
//                }
//            }
//        }
    }

    @Override
    public void draw(){
        super.draw();
    }
}
