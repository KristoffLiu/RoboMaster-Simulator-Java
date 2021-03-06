package com.kristoff.robomaster_simulator.robomasters.robomaster.modules;

import com.badlogic.gdx.math.MathUtils;
import com.kristoff.robomaster_simulator.robomasters.teams.RoboMasters;
import com.kristoff.robomaster_simulator.utils.LoopThread;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.pointsimulator.StatusPoint;
import com.kristoff.robomaster_simulator.robomasters.robomaster.RoboMaster;
import com.kristoff.robomaster_simulator.systems.pointsimulator.PointSimulator;

import java.util.concurrent.CopyOnWriteArrayList;

public class LidarObservation extends LoopThread {
    RoboMaster thisRoboMaster;
    LidarMode mode;
    public CopyOnWriteArrayList<StatusPoint> other;
    public PointSimulator.PointStatus[][] other_array;

    PointSimulator.PointStatus pointStatus;

    public LidarObservation(RoboMaster roboMaster){
        this.thisRoboMaster = roboMaster;
        mode = RoboMasters.lidarMode;
        isStep = true;
        delta = 1/60f;

        switch (thisRoboMaster.No){
            case 0 -> {
                pointStatus = PointSimulator.PointStatus.Blue1;
            }
            case 1 -> {
                pointStatus = PointSimulator.PointStatus.Blue2;
            }
            case 2 -> {
                pointStatus = PointSimulator.PointStatus.Red1;
            }
            case 3 -> {
                pointStatus = PointSimulator.PointStatus.Red2;
            }
        }

        switch (mode){
            case list -> {
                other = new CopyOnWriteArrayList<>();
            }
            case array -> {
                other_array = new PointSimulator.PointStatus[8490][4890];
            }
            case both -> {
                other = new CopyOnWriteArrayList<>();
                other_array = new PointSimulator.PointStatus[8490][4890];
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
        synchronized (Systems.pointSimulator){
            if(Systems.pointSimulator != null){
                switch (mode){
                    case list -> {
                        other = lidarPointCloudSimulate(
                                RoboMasters.teamBlue.get(0).getLidarPosition().x,
                                RoboMasters.teamBlue.get(0).getLidarPosition().y);
                    }
                    case array -> {
                        other_array = lidarPointCloudSimulateArray(
                                RoboMasters.teamBlue.get(0).getLidarPosition().x,
                                RoboMasters.teamBlue.get(0).getLidarPosition().y);
                    }
                    case both -> {
                        lidarSimulateInBothMode(
                                RoboMasters.teamBlue.get(0).getLidarPosition().x,
                                RoboMasters.teamBlue.get(0).getLidarPosition().y);
                    }
                }
            }
        }
    }

    public CopyOnWriteArrayList<StatusPoint> lidarPointCloudSimulate(float c_x, float c_y){
        CopyOnWriteArrayList<StatusPoint> pointsArray = new CopyOnWriteArrayList<>();
        int centre_x = (int) (c_x);
        int centre_y = (int) (c_y);
        float precisionOfDegree = 0.5f;
        int x = 0;
        int y = 0;
        for(float degree = 0;degree < 360; degree += precisionOfDegree){
            float radian = degree * MathUtils.degreesToRadians;
            if(degree == 0 || degree == 180){
                for(y = 0;y <4800; y++){
                    if(degree == 0){
                        if(Systems.pointSimulator.isPointNotEmpty(centre_x, centre_y + y,pointStatus)){
                            pointsArray.add(getPointFromMatrix(centre_x, centre_y + y));
                            break;
                        }
                    }
                    else {
                        if(Systems.pointSimulator.isPointNotEmpty(centre_x, centre_y - y,pointStatus)){
                            pointsArray.add(getPointFromMatrix(centre_x, centre_y - y));
                            break;
                        }
                    }
                }
            }
            else if(degree == 90 || degree == 270){
                for(x = 0;x <8400; x++){
                    if(degree == 90){
                        if(Systems.pointSimulator.isPointNotEmpty(centre_x + x, centre_y,pointStatus)){
                            pointsArray.add(getPointFromMatrix(centre_x + x, centre_y));
                            break;
                        }
                    }
                    else {
                        if(Systems.pointSimulator.isPointNotEmpty(centre_x - x, centre_y,pointStatus)){
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
                    if(Systems.pointSimulator.isPointNotEmpty(centre_x + offset_x, centre_y + offset_y,pointStatus)){
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
                    if(Systems.pointSimulator.isPointNotEmpty(centre_x + offset_x, centre_y + offset_y,pointStatus)){
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

    public PointSimulator.PointStatus[][] lidarPointCloudSimulateArray(float c_x, float c_y){
        PointSimulator.PointStatus[][] pointsArray = new PointSimulator.PointStatus[8490][4890];
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
                        pointsArray[centre_x][centre_y + y] = Systems.pointSimulator.getPoint(centre_x, centre_y + y);
                        if(isPointNotEmpty(centre_x, centre_y + y)) break;
                    }
                    else {
                        pointsArray[centre_x][centre_y - y] = Systems.pointSimulator.getPoint(centre_x, centre_y - y);
                        if(isPointNotEmpty(centre_x, centre_y - y)) break;
                    }
                }
            }
            else if(degree == 90 || degree == 270){
                for(x = 0;x <8400; x++){
                    if(degree == 90){
                        pointsArray[centre_x + x][centre_y] = Systems.pointSimulator.getPoint(centre_x + x, centre_y);
                        if(isPointNotEmpty(centre_x + x, centre_y)) break;
                    }
                    else {
                        pointsArray[centre_x - x][centre_y] = Systems.pointSimulator.getPoint(centre_x - x, centre_y);
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
                    pointsArray[centre_x + offset_x][centre_y + offset_y] = Systems.pointSimulator.getPoint(centre_x + offset_x, centre_y + offset_y);
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
                    pointsArray[centre_x + offset_x][centre_y + offset_y] = Systems.pointSimulator.getPoint(centre_x + offset_x, centre_y + offset_y);
                    if(isPointNotEmpty(centre_x + offset_x, centre_y + offset_y)) break;
                }
            }
            x = 0;
            y = 0;
        }
        return pointsArray;
    }


    public void lidarSimulateInBothMode(float c_x, float c_y){
        CopyOnWriteArrayList<StatusPoint> pointsArrayList = new CopyOnWriteArrayList<>();
        PointSimulator.PointStatus[][] pointsArray = new PointSimulator.PointStatus[8490][4890];

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
                        pointsArray[centre_x][centre_y + y] = Systems.pointSimulator.getPoint(centre_x, centre_y + y);
                        if(isPointNotEmpty(centre_x, centre_y + y)){
                            pointsArrayList.add(getPointFromMatrix(centre_x, centre_y + y));
                            break;
                        }
                    }
                    else {
                        pointsArray[centre_x][centre_y - y] = Systems.pointSimulator.getPoint(centre_x, centre_y - y);

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
                        pointsArray[centre_x + x][centre_y] = Systems.pointSimulator.getPoint(centre_x + x, centre_y);
                        if(isPointNotEmpty(centre_x + x, centre_y)){
                            pointsArrayList.add(getPointFromMatrix(centre_x + x, centre_y));
                            break;
                        }
                    }
                    else {
                        pointsArray[centre_x - x][centre_y] = Systems.pointSimulator.getPoint(centre_x - x, centre_y);
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
                    pointsArray[centre_x + offset_x][centre_y + offset_y] = Systems.pointSimulator.getPoint(centre_x + offset_x, centre_y + offset_y);
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
                    pointsArray[centre_x + offset_x][centre_y + offset_y] = Systems.pointSimulator.getPoint(centre_x + offset_x, centre_y + offset_y);
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


    private StatusPoint getPointFromMatrix(int x, int y){
        return PointSimulator.getRoboMasterPoint(x, y);
    }

    private boolean isPointNotEmpty(int x, int y){
        return PointSimulator.isPointNotEmpty(x, y);
    }
}
