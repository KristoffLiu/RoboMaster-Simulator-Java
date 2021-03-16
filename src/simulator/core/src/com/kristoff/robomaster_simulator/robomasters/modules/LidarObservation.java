package com.kristoff.robomaster_simulator.robomasters.modules;

import com.badlogic.gdx.math.MathUtils;
import com.kristoff.robomaster_simulator.robomasters.types.Enemy;
import com.kristoff.robomaster_simulator.systems.pointsimulator.PointState;
import com.kristoff.robomaster_simulator.teams.RoboMasters;
import com.kristoff.robomaster_simulator.utils.LoopThread;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.pointsimulator.StatePoint;
import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.systems.pointsimulator.PointSimulator;
import com.kristoff.robomaster_simulator.utils.Position;

import java.util.concurrent.CopyOnWriteArrayList;

public class LidarObservation extends LoopThread {
    RoboMaster me;
    public CopyOnWriteArrayList<StatePoint> others;
    public PointState[][] other_array;

    PointState pointState;

    public LidarObservation(RoboMaster roboMaster){
        this.me = roboMaster;
        isStep = false;
        delta = 1/60f;

        switch (me.No){
            case 0 -> {
                pointState = PointState.Blue1;
            }
            case 1 -> {
                pointState = PointState.Blue2;
            }
            case 2 -> {
                pointState = PointState.Red1;
            }
            case 3 -> {
                pointState = PointState.Red2;
            }
        }
        others = new CopyOnWriteArrayList<>();
    }

    @Override
    public void step(){
        synchronized (Systems.pointSimulator){
            if(Systems.pointSimulator != null){
                others = lidarPointCloudSimulate(
                        this.me.getLidarPosition().x,
                        this.me.getLidarPosition().y);
            }
        }
    }

    public CopyOnWriteArrayList<StatePoint> lidarPointCloudSimulate(float c_x, float c_y){
        CopyOnWriteArrayList<StatePoint> pointsArray = new CopyOnWriteArrayList<>();
        int centre_x = (int) (c_x / 10);
        int centre_y = (int) (c_y / 10);
        float radius = 8000;
        float sectorAngleDegree = 1f;//每道光线的度数，也就是就是图中的θ
        float sectorAngleRadian = (float)Math.toRadians(sectorAngleDegree);//每道光线的度数，也就是就是图中的θ
        for(float rotationAngleDegree = 0;rotationAngleDegree < 360; rotationAngleDegree += sectorAngleDegree){
            float rotationRadian = (float)Math.toRadians(rotationAngleDegree);
            //以下代码用的公式，仅当角度小于45°或大于315°的时候有效
            if(rotationAngleDegree < 45 || rotationAngleDegree >= 315){
                //光线一层层扩散，也就是说等腰三角形逐渐变大，y轴数值每次增加1
                for(int y = 0;y < Math.cos(rotationRadian + sectorAngleRadian) * radius; y++){
                    int startX = (int)Math.floor(Math.tan(rotationRadian) * y); //也就是图中我画的A点，相似三角形每次放大以后，上边的左边顶点，逐层扫描的起始位置
                    int endX = (int)Math.ceil(Math.tan(rotationRadian + sectorAngleRadian) * y);//也就是图中我画的B点，相似三角形每次放大以后，上边的右边顶点，逐层扫描的结束位置
                    //读取当前这一层的每一个点的位置（它们的y轴数值一样，只需要步进x+=1），判断有没有障碍物在这个电商
                    boolean canstop = false;
                    for(int x = startX; x < endX; x++){
                        //判断数组中第x行第y列数值是不是0(也就是是不是空的)
                        if(Systems.pointSimulator.isPointNotEmpty(centre_x + x,centre_y + y, pointState)) {
                            pointsArray.add(getPointFromMatrix(centre_x + x,centre_y + y));
                            canstop = true;
                            break;
                        }
                    }
                    if(canstop) break;
                }
            }
            else if(rotationAngleDegree >= 135 && rotationAngleDegree < 225){
                //光线一层层扩散，也就是说等腰三角形逐渐变大，y轴数值每次增加1
                for(int y = 0; y > Math.cos(rotationRadian + sectorAngleRadian) * radius; y--){
                    int startX = (int)Math.ceil(Math.tan(rotationRadian) * y); //也就是图中我画的A点，相似三角形每次放大以后，上边的左边顶点，逐层扫描的起始位置
                    int endX = (int)Math.floor(Math.tan(rotationRadian + sectorAngleRadian) * y);//也就是图中我画的B点，相似三角形每次放大以后，上边的右边顶点，逐层扫描的结束位置
                    //读取当前这一层的每一个点的位置（它们的y轴数值一样，只需要步进x+=1），判断有没有障碍物在这个电商
                    boolean canstop = false;
                    for(int x = startX; x > endX; x--){
                        //判断数组中第x行第y列数值是不是0(也就是是不是空的)
                        if(Systems.pointSimulator.isPointNotEmpty(centre_x + x,centre_y + y, pointState)) {
                            pointsArray.add(getPointFromMatrix(centre_x + x,centre_y + y));
                            canstop = true;
                            break;
                        }
                    }
                    if(canstop) break;
                }
            }
            else if(rotationAngleDegree >= 45 && rotationAngleDegree < 135){
                //光线一层层扩散，也就是说等腰三角形逐渐变大，y轴数值每次增加1
                for(int x = 0;x < Math.sin(rotationRadian + sectorAngleRadian) * radius; x++){
                    int startY = (int)Math.ceil(x / Math.tan(rotationRadian)); //也就是图中我画的A点，相似三角形每次放大以后，上边的左边顶点，逐层扫描的起始位置
                    int endY = (int)Math.floor(x / Math.tan(rotationRadian + sectorAngleRadian));//也就是图中我画的B点，相似三角形每次放大以后，上边的右边顶点，逐层扫描的结束位置
                    //读取当前这一层的每一个点的位置（它们的y轴数值一样，只需要步进x+=1），判断有没有障碍物在这个电商
                    boolean canstop = false;
                    for(int y = startY; y > endY; y--){
                        //判断数组中第x行第y列数值是不是0(也就是是不是空的)
                        if(Systems.pointSimulator.isPointNotEmpty(centre_x + x,centre_y + y, pointState)) {
                            pointsArray.add(getPointFromMatrix(centre_x + x,centre_y + y));
                            canstop = true;
                            break;
                        }
                    }
                    if(canstop) break;
                }
            }
            else if(rotationAngleDegree >= 225 && rotationAngleDegree < 315){
                //光线一层层扩散，也就是说等腰三角形逐渐变大，y轴数值每次增加1
                for(int x = 0;x > Math.sin(rotationRadian + sectorAngleRadian) * radius; x--){
                    int startY = (int)Math.floor(x / Math.tan(rotationRadian)); //也就是图中我画的A点，相似三角形每次放大以后，上边的左边顶点，逐层扫描的起始位置
                    int endY = (int)Math.ceil(x / Math.tan(rotationRadian + sectorAngleRadian));//也就是图中我画的B点，相似三角形每次放大以后，上边的右边顶点，逐层扫描的结束位置
                    //读取当前这一层的每一个点的位置（它们的y轴数值一样，只需要步进x+=1），判断有没有障碍物在这个电商
                    boolean canstop = false;
                    for(int y = startY; y < endY; y++){
                        //判断数组中第x行第y列数值是不是0(也就是是不是空的)
                        if(Systems.pointSimulator.isPointNotEmpty(centre_x + x,centre_y + y, pointState)) {
                            pointsArray.add(Systems.pointSimulator.getRoboMasterPoint(centre_x + x,centre_y + y));
                            canstop = true;
                            break;
                        }
                    }
                    if(canstop) break;
                }
            }
        }
        return pointsArray;
    }

    public PointState[][] lidarPointCloudSimulateArray(float c_x, float c_y){
        PointState[][] pointsArray = new PointState[8490][4890];
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
        CopyOnWriteArrayList<StatePoint> pointsArrayList = new CopyOnWriteArrayList<>();
        PointState[][] pointsArray = new PointState[8490][4890];

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
        others =  pointsArrayList;
        other_array = pointsArray;
    }

    private StatePoint getPointFromMatrix(int x, int y){
        return PointSimulator.getRoboMasterPoint(x, y);
    }

    private boolean isPointNotEmpty(int x, int y){
        return PointSimulator.isPointNotEmpty(x, y);
    }


//    public boolean canSeeLockedEnemy(Position startingPosition){
//        return canSeeRoboMaster(startingPosition, Enemy.getLockedEnemy());
//    }
//
//    public boolean canSeeUnlockedEnemy(Position startingPosition){
//        return canSeeRoboMaster(startingPosition, Enemy.getUnlockedEnemy());
//    }
//
//    public boolean canSeeRoboMaster(Position startingPosition, RoboMaster targetRobo){
//        for(int i = 0; i < 60; i+=10){
//            for(int j = 0; j < 45; j+=10){
//                Position targetPosition = targetRobo.actor.getPoint(i, j);
//                if(canSeeThePoint(startingPosition, targetPosition, 500, getAngle(targetPosition, startingPosition))){
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    public int getAngle(Position target, Position startingPosition) {
//        return (int) Math.toDegrees(Math.atan2(target.x - startingPosition.x, target.y - startingPosition.y));
//    }
//
//    public boolean canSeeThePoint(Position startingPosition, Position targetPosition, int radius, int degree){
//        int centre_x = startingPosition.x / 10;//原点的在大地图中的x轴相对位置
//        int centre_y = startingPosition.y / 10;//原点的在大地图中的y轴相对位置
//        float sectorAngleDegree = 1f;//每道光线的度数，也就是就是图中的θ
//        float sectorAngleRadian = (float)Math.toRadians(sectorAngleDegree);//每道光线的度数，也就是就是图中的θ
//        float rotationAngleDegree = degree;
//        //绕周围照射一圈光线，照射次数与每道光线的度数有关
//        //degree就是我图中的Δ
//        float rotationRadian = (float)Math.toRadians(rotationAngleDegree);
//        //以下代码用的公式，仅当角度小于45°或大于315°的时候有效
//        if(rotationAngleDegree < 45 || rotationAngleDegree >= 315){
//            //光线一层层扩散，也就是说等腰三角形逐渐变大，y轴数值每次增加1
//            for(int y = 0;y < Math.cos(rotationRadian + sectorAngleRadian) * radius; y++){
//                int startX = (int)Math.floor(Math.tan(rotationRadian) * y); //也就是图中我画的A点，相似三角形每次放大以后，上边的左边顶点，逐层扫描的起始位置
//                int endX = (int)Math.ceil(Math.tan(rotationRadian + sectorAngleRadian) * y);//也就是图中我画的B点，相似三角形每次放大以后，上边的右边顶点，逐层扫描的结束位置
//                //读取当前这一层的每一个点的位置（它们的y轴数值一样，只需要步进x+=1），判断有没有障碍物在这个电商
//                boolean canstop = false;
//                for(int x = startX; x < endX; x++){
//                    //判断数组中第x行第y列数值是不是0(也就是是不是空的)
//                    if(Systems.pointSimulator.isPointNotEmpty(centre_x + x,centre_y + y, me.pointState)) {
//                        if(targetPosition.equals(centre_x+x,centre_y+y)){
//                            return true;
//                        }
//                        canstop = true;
//                        break;
//                    }
//                }
//                if(canstop) break;
//            }
//        }
//        else if(rotationAngleDegree >= 135 && rotationAngleDegree < 225){
//            //光线一层层扩散，也就是说等腰三角形逐渐变大，y轴数值每次增加1
//            for(int y = 0; y > Math.cos(rotationRadian + sectorAngleRadian) * radius; y--){
//                int startX = (int)Math.ceil(Math.tan(rotationRadian) * y); //也就是图中我画的A点，相似三角形每次放大以后，上边的左边顶点，逐层扫描的起始位置
//                int endX = (int)Math.floor(Math.tan(rotationRadian + sectorAngleRadian) * y);//也就是图中我画的B点，相似三角形每次放大以后，上边的右边顶点，逐层扫描的结束位置
//                //读取当前这一层的每一个点的位置（它们的y轴数值一样，只需要步进x+=1），判断有没有障碍物在这个电商
//                boolean canstop = false;
//                for(int x = startX; x > endX; x--){
//                    //判断数组中第x行第y列数值是不是0(也就是是不是空的)
//                    if(Systems.pointSimulator.isPointNotEmpty(centre_x + x,centre_y + y, me.pointState)) {
//                        if(targetPosition.equals(centre_x+x,centre_y+y)){
//                            return true;
//                        }
//                        canstop = true;
//                        break;
//                    }
//                }
//                if(canstop) break;
//            }
//        }
//        else if(rotationAngleDegree >= 45 && rotationAngleDegree < 135){
//            //光线一层层扩散，也就是说等腰三角形逐渐变大，y轴数值每次增加1
//            for(int x = 0;x < Math.sin(rotationRadian + sectorAngleRadian) * radius; x++){
//                int startY = (int)Math.ceil(x / Math.tan(rotationRadian)); //也就是图中我画的A点，相似三角形每次放大以后，上边的左边顶点，逐层扫描的起始位置
//                int endY = (int)Math.floor(x / Math.tan(rotationRadian + sectorAngleRadian));//也就是图中我画的B点，相似三角形每次放大以后，上边的右边顶点，逐层扫描的结束位置
//                //读取当前这一层的每一个点的位置（它们的y轴数值一样，只需要步进x+=1），判断有没有障碍物在这个电商
//                boolean canstop = false;
//                for(int y = startY; y > endY; y--){
//                    //判断数组中第x行第y列数值是不是0(也就是是不是空的)
//                    if(Systems.pointSimulator.isPointNotEmpty(centre_x + x,centre_y + y, me.pointState)) {
//                        if(targetPosition.equals(centre_x+x,centre_y+y)){
//                            return true;
//                        }
//                        canstop = true;
//                        break;
//                    }
//                }
//                if(canstop) break;
//            }
//        }
//        else if(rotationAngleDegree >= 225 && rotationAngleDegree < 315){
//            //光线一层层扩散，也就是说等腰三角形逐渐变大，y轴数值每次增加1
//            for(int x = 0;x > Math.sin(rotationRadian + sectorAngleRadian) * radius; x--){
//                int startY = (int)Math.floor(x / Math.tan(rotationRadian)); //也就是图中我画的A点，相似三角形每次放大以后，上边的左边顶点，逐层扫描的起始位置
//                int endY = (int)Math.ceil(x / Math.tan(rotationRadian + sectorAngleRadian));//也就是图中我画的B点，相似三角形每次放大以后，上边的右边顶点，逐层扫描的结束位置
//                //读取当前这一层的每一个点的位置（它们的y轴数值一样，只需要步进x+=1），判断有没有障碍物在这个电商
//                boolean canstop = false;
//                for(int y = startY; y < endY; y++){
//                    //判断数组中第x行第y列数值是不是0(也就是是不是空的)
//                    if(Systems.pointSimulator.isPointNotEmpty(centre_x + x,centre_y + y, me.pointState)) {
//                        if(targetPosition.equals(centre_x+x,centre_y+y)){
//                            return true;
//                        }
//                        canstop = true;
//                        break;
//                    }
//                }
//                if(canstop) break;
//            }
//        }
//        return false;
//    }
}
