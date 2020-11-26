package com.robomaster_libgdx.environment.simulatinglayers;

import com.robomaster_libgdx.environment.Environment;
import com.robomaster_libgdx.environment.libs.MatrixSenseSystem;
import com.robomaster_libgdx.environment.robomasters.RoboMaster;
import com.robomaster_libgdx.environment.simulatinglayers.baselayers.VisualLayer;

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
                if(matrixSenseSystem.demoMatrix[i][j]) {
                    environment.pointCloudRenderer.setAutoShapeType(true);
                    environment.pointCloudRenderer.setColor(1.0f,0.5f,1.0f,1.0f);
                    environment.pointCloudRenderer.circle(
                            i / 1000f,
                            j / 1000f,
                            0.05f,10);
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
