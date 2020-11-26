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
                    float red;
                    float green;
                    float blue;
                    if(distance <= 7000){
                        red = - 1f/7000f * distance + 1.0f;
                    }
                    else {
                        red = 0f;
                    }
                    if (distance <= 6000){
                        green = 1f/6000f * distance;
                    }
                    else {
                        green = 1.0f;
                    }
                    if (distance <= 5000 && distance >= 2000){
                        blue = -1f/3000f * distance + 5f/3f;
                    }
                    else {
                        blue = 0f;
                    }
                    environment.pointCloudRenderer.setColor(red,green,blue,1.0f);
                    environment.pointCloudRenderer.circle(
                            i / 1000f,
                            j / 1000f,
                            0.04f,10);
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
