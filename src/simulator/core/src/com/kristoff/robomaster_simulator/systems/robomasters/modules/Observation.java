package com.kristoff.robomaster_simulator.systems.robomasters.modules;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.kristoff.robomaster_simulator.environment.BackendThread;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.matrixsimulation.RoboMasterPoint;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMasters;
import com.kristoff.robomaster_simulator.systems.matrixsimulation.MatrixSimulator;

public class Observation extends BackendThread {
    RoboMaster thisRoboMaster;
    public Array<RoboMasterPoint> other;

    public Observation(RoboMaster roboMaster){
        isStep = true;
        delta = 1/60f;
        other = new Array<>();
    }

    @Override
    public void step(){
        if(Systems.matrixSimulator != null){
            other = lidarPointCloudSimulate(
                    Systems.roboMasters.teamBlue.get(0).getLidarPosition().x,
                    Systems.roboMasters.teamBlue.get(0).getLidarPosition().y);
        }
    }

    public Array<RoboMasterPoint> lidarPointCloudSimulate(float c_x, float c_y){
        Array<RoboMasterPoint> pointsArray = new Array<>();
        int centre_x = (int) (c_x * 1000);
        int centre_y = (int) (c_y * 1000);
        float precisionOfDegree = 0.5f;
        int x = 0;
        int y = 0;
        for(float degree = 0;degree < 360; degree += precisionOfDegree){
            float radian = degree * MathUtils.degreesToRadians;
            if(degree == 0 || degree == 180){
                for(y = 0;y <4800; y++){
                    if(degree == 0){
                        if(isPointNotEmpty(centre_x, centre_y + y)){
                            pointsArray.add(getPointFromMatrix(centre_x, centre_y + y));
                            break;
                        }
                    }
                    else {
                        if(isPointNotEmpty(centre_x, centre_y - y)){
                            pointsArray.add(getPointFromMatrix(centre_x, centre_y - y));
                            break;
                        }
                    }
                }
            }
            else if(degree == 90 || degree == 270){
                for(x = 0;x <8400; x++){
                    if(degree == 90){
                        if(isPointNotEmpty(centre_x + x, centre_y)){
                            pointsArray.add(getPointFromMatrix(centre_x + x, centre_y));
                            break;
                        }
                    }
                    else {
                        if(isPointNotEmpty(centre_x - x, centre_y)){
                            pointsArray.add(getPointFromMatrix(centre_x - x, centre_y));
                            break;
                        }
                    }
                }
            }
            else if(degree > 315 || degree < 45 || (degree > 135 && degree < 225)){
                for(y = 0;y <4800; y++){
                    int offset_x = (int) (Math.tan(radian) * y);
                    int offset_y = y;
                    if(degree > 135 && degree < 180){
                        offset_x = - offset_x;
                        offset_y = - offset_y;
                    }
                    else if(degree > 180 && degree < 225){
                        offset_x = - offset_x;
                        offset_y = - offset_y;
                    }
                    else{
                        offset_x = - offset_x;
                    }
                    if(isPointNotEmpty(centre_x + offset_x, centre_y + offset_y)){
                        pointsArray.add(getPointFromMatrix(centre_x + offset_x, centre_y + offset_y));
                        break;
                    }
                }
            }
            else{
                for(x = 0;x <8400; x++){
                    int offset_x = x;
                    int offset_y = (int) (x / Math.tan(radian));
                    if(degree <= 135){
                        offset_y = - offset_y;
                    }
                    else if(degree >= 225){
                        offset_x = - offset_x;
                        offset_y = - offset_y;
                    }
                    else if(degree <= 315){
                        offset_x = - offset_x;
                    }
                    if(isPointNotEmpty(centre_x + offset_x, centre_y + offset_y)){
                        pointsArray.add(getPointFromMatrix(centre_x + offset_x, centre_y + offset_y));
                        break;
                    }
                }
            }
            x = 0;
            y = 0;
        }
        return pointsArray;
    }



    private RoboMasterPoint getPointFromMatrix(int x, int y){
        return MatrixSimulator.getPoint(x, y);
    }

    private boolean isPointNotEmpty(int x, int y){
        return MatrixSimulator.isPointNotEmpty(x, y);
    }
}