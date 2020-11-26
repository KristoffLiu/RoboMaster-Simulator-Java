package com.robomaster_libgdx.environment.simulatinglayers;

import com.robomaster_libgdx.environment.Environment;
import com.robomaster_libgdx.environment.libs.MatrixSenseSystem;
import com.robomaster_libgdx.environment.robomasters.RoboMaster;
import com.robomaster_libgdx.environment.simulatinglayers.baselayers.VisualLayer;

import java.util.Arrays;

public class GlobalMatrixLayer extends VisualLayer {
    MatrixSenseSystem matrixSenseSystem;
    public GlobalMatrixLayer(Environment environment) {
        super(environment);
        matrixSenseSystem = environment.matrixSenseSystem;
    }

    @Override
    public void act (float delta) {
        float scale = 1f / 1000f;
        super.act(delta);
        for(int i=0;i<8490;i++){
            for(int j=0;j<4890;j++){
                if(matrixSenseSystem.pointMatrix[i][j]) {
                    environment.pointCloudRenderer.setAutoShapeType(true);
                    environment.pointCloudRenderer.setColor(1.0f,0.5f,1.0f,1.0f);
                    environment.pointCloudRenderer.circle(
                            i / 1000f,
                            j / 1000f,
                            0.02f,10);
                }
            }
        }
    }

    @Override
    public void draw(){
        super.draw();
    }

}
