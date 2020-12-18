package com.kristoff.robomaster_simulator.systems.robomasters.modules;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMasters;
import com.kristoff.robomaster_simulator.utils.BackendThread;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.matrixsimulation.RoboMasterPoint;
import com.kristoff.robomaster_simulator.systems.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.systems.matrixsimulation.MatrixSimulator;

public class LidarObservation extends BackendThread {
    RoboMaster thisRoboMaster;
    LidarMode mode;
    public Array<RoboMasterPoint> other;
    public MatrixSimulator.MatrixPointStatus[][] other_array;

    MatrixSimulator.MatrixPointStatus pointStatus;

    public LidarObservation(RoboMaster roboMaster){
        this.thisRoboMaster = roboMaster;
        mode = RoboMasters.lidarMode;
        isStep = true;
        delta = 1/60f;

        switch (thisRoboMaster.No){
            case 0 -> {
                pointStatus = MatrixSimulator.MatrixPointStatus.Blue1;
            }
            case 1 -> {
                pointStatus = MatrixSimulator.MatrixPointStatus.Blue2;
            }
            case 2 -> {
                pointStatus = MatrixSimulator.MatrixPointStatus.Red1;
            }
            case 3 -> {
                pointStatus = MatrixSimulator.MatrixPointStatus.Red2;
            }
        }

        switch (mode){
            case list -> {
                other = new Array<>();
            }
            case array -> {
                other_array = new MatrixSimulator.MatrixPointStatus[8490][4890];
            }
            case both -> {
                other = new Array<>();
                other_array = new MatrixSimulator.MatrixPointStatus[8490][4890];
            }
        }
    }

    public enum LidarMode{
        list,
        array,
        both
    }

    @Override
    public void step(){
        if(Systems.matrixSimulator != null){
            switch (mode){
                case list -> {
                    other = lidarPointCloudSimulate(
                            Systems.roboMasters.teamBlue.get(0).getLidarPosition().x,
                            Systems.roboMasters.teamBlue.get(0).getLidarPosition().y);
                }
                case array -> {
                    other_array = lidarPointCloudSimulateArray(
                            Systems.roboMasters.teamBlue.get(0).getLidarPosition().x,
                            Systems.roboMasters.teamBlue.get(0).getLidarPosition().y);
                }
                case both -> {
                    lidarSimulateInBothMode(
                            Systems.roboMasters.teamBlue.get(0).getLidarPosition().x,
                            Systems.roboMasters.teamBlue.get(0).getLidarPosition().y);
                }
            }
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
                        if(Systems.matrixSimulator.isPointNotEmpty(centre_x, centre_y + y,pointStatus)){
                            pointsArray.add(getPointFromMatrix(centre_x, centre_y + y));
                            break;
                        }
                    }
                    else {
                        if(Systems.matrixSimulator.isPointNotEmpty(centre_x, centre_y - y,pointStatus)){
                            pointsArray.add(getPointFromMatrix(centre_x, centre_y - y));
                            break;
                        }
                    }
                }
            }
            else if(degree == 90 || degree == 270){
                for(x = 0;x <8400; x++){
                    if(degree == 90){
                        if(Systems.matrixSimulator.isPointNotEmpty(centre_x + x, centre_y,pointStatus)){
                            pointsArray.add(getPointFromMatrix(centre_x + x, centre_y));
                            break;
                        }
                    }
                    else {
                        if(Systems.matrixSimulator.isPointNotEmpty(centre_x - x, centre_y,pointStatus)){
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
                    if(Systems.matrixSimulator.isPointNotEmpty(centre_x + offset_x, centre_y + offset_y,pointStatus)){
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
                    if(Systems.matrixSimulator.isPointNotEmpty(centre_x + offset_x, centre_y + offset_y,pointStatus)){
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

    public MatrixSimulator.MatrixPointStatus[][] lidarPointCloudSimulateArray(float c_x, float c_y){
        MatrixSimulator.MatrixPointStatus[][] pointsArray = new MatrixSimulator.MatrixPointStatus[8490][4890];
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
                        pointsArray[centre_x][centre_y + y] = Systems.matrixSimulator.getPoint(centre_x, centre_y + y);
                        if(isPointNotEmpty(centre_x, centre_y + y)) break;
                    }
                    else {
                        pointsArray[centre_x][centre_y - y] = Systems.matrixSimulator.getPoint(centre_x, centre_y - y);
                        if(isPointNotEmpty(centre_x, centre_y - y)) break;
                    }
                }
            }
            else if(degree == 90 || degree == 270){
                for(x = 0;x <8400; x++){
                    if(degree == 90){
                        pointsArray[centre_x + x][centre_y] = Systems.matrixSimulator.getPoint(centre_x + x, centre_y);
                        if(isPointNotEmpty(centre_x + x, centre_y)) break;
                    }
                    else {
                        pointsArray[centre_x - x][centre_y] = Systems.matrixSimulator.getPoint(centre_x - x, centre_y);
                        if(isPointNotEmpty(centre_x - x, centre_y)) break;
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
                    pointsArray[centre_x + offset_x][centre_y + offset_y] = Systems.matrixSimulator.getPoint(centre_x + offset_x, centre_y + offset_y);
                    if(isPointNotEmpty(centre_x + offset_x, centre_y + offset_y)) break;
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
                    pointsArray[centre_x + offset_x][centre_y + offset_y] = Systems.matrixSimulator.getPoint(centre_x + offset_x, centre_y + offset_y);
                    if(isPointNotEmpty(centre_x + offset_x, centre_y + offset_y)) break;
                }
            }
            x = 0;
            y = 0;
        }
        return pointsArray;
    }


    public void lidarSimulateInBothMode(float c_x, float c_y){
        Array<RoboMasterPoint> pointsArrayList = new Array<>();
        MatrixSimulator.MatrixPointStatus[][] pointsArray = new MatrixSimulator.MatrixPointStatus[8490][4890];

        int centre_x = (int) (c_x * 1000);
        int centre_y = (int) (c_y * 1000);
        float precisionOfDegree = 0.5f;
        int x = 0;
        int y = 0;
        for(float degree = 0;degree < 360; degree += precisionOfDegree){
            float radian = degree * MathUtils.degreesToRadians;
            if(degree == 0 || degree == 180){
                for(y = 0;y <4800; y+=10){
                    if(degree == 0){
                        pointsArray[centre_x][centre_y + y] = Systems.matrixSimulator.getPoint(centre_x, centre_y + y);
                        if(isPointNotEmpty(centre_x, centre_y + y)){
                            pointsArrayList.add(getPointFromMatrix(centre_x, centre_y + y));
                            break;
                        }
                    }
                    else {
                        pointsArray[centre_x][centre_y - y] = Systems.matrixSimulator.getPoint(centre_x, centre_y - y);

                        if(isPointNotEmpty(centre_x, centre_y - y)){
                            pointsArrayList.add(getPointFromMatrix(centre_x, centre_y - y));
                            break;
                        }
                    }
                }
            }
            else if(degree == 90 || degree == 270){
                for(x = 0;x <8400; x+=10){
                    if(degree == 90){
                        pointsArray[centre_x + x][centre_y] = Systems.matrixSimulator.getPoint(centre_x + x, centre_y);
                        if(isPointNotEmpty(centre_x + x, centre_y)){
                            pointsArrayList.add(getPointFromMatrix(centre_x + x, centre_y));
                            break;
                        }
                    }
                    else {
                        pointsArray[centre_x - x][centre_y] = Systems.matrixSimulator.getPoint(centre_x - x, centre_y);
                        if(isPointNotEmpty(centre_x - x, centre_y)){
                            pointsArrayList.add(getPointFromMatrix(centre_x - x, centre_y));
                            break;
                        }
                    }
                }
            }
            else if(degree > 315 || degree < 45 || (degree > 135 && degree < 225)){
                for(y = 0;y <4800; y+=10){
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
                    pointsArray[centre_x + offset_x][centre_y + offset_y] = Systems.matrixSimulator.getPoint(centre_x + offset_x, centre_y + offset_y);
                    if(isPointNotEmpty(centre_x + offset_x, centre_y + offset_y)){
                        pointsArrayList.add(getPointFromMatrix(centre_x + offset_x, centre_y + offset_y));
                        break;
                    }
                }
            }
            else{
                for(x = 0;x <8400; x+=10){
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
                    pointsArray[centre_x + offset_x][centre_y + offset_y] = Systems.matrixSimulator.getPoint(centre_x + offset_x, centre_y + offset_y);
                    if(isPointNotEmpty(centre_x + offset_x, centre_y + offset_y)){
                        pointsArrayList.add(getPointFromMatrix(centre_x + offset_x, centre_y + offset_y));
                        break;
                    }
                }
            }
            x = 0;
            y = 0;
        }
        other =  pointsArrayList;
        other_array = pointsArray;
    }


    private RoboMasterPoint getPointFromMatrix(int x, int y){
        return MatrixSimulator.getRoboMasterPoint(x, y);
    }

    private boolean isPointNotEmpty(int x, int y){
        return MatrixSimulator.isPointNotEmpty(x, y);
    }
}
