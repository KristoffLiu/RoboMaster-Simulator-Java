package com.kristoff.robomaster_simulator.teams.enemyobservations;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.kristoff.robomaster_simulator.robomasters.types.Enemy;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.systems.pointsimulator.StatePoint;
import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.utils.Position;

public class EnemyObservation{
    RoboMaster self1;
    RoboMaster self2;
    RoboMaster enemy;
    Position position = new Position();
    int radius = EnemiesObservationSimulator.getRadius();
    int weight = 0;

    public EnemyObservation(RoboMaster self1, RoboMaster self2, RoboMaster enemy, int value){
        this.self1 = self1;
        this.self2 = self2;
        this.enemy = enemy;
        this.weight = value;
    }

    public void setPosition(int x, int y){
        setPosition(new Position(x,y));
    }

    public void setPosition(Position position){
        this.position = position;
    }

    public void simulate(int[][] EnemyObservationMapPoints){
        int[][] pointsArray = EnemyObservationMapPoints;
        Array<StatePoint> pointsArrayList = new Array<>();
        this.setPosition(enemy.getLidarPosition());

        int centre_x = position.x / 10;//原点的在大地图中的x轴相对位置
        int centre_y = position.y / 10;//原点的在大地图中的y轴相对位置
        float sectorAngleDegree = 1f;//每道光线的度数，也就是就是图中的θ
        float sectorAngleRadian = (float)Math.toRadians(sectorAngleDegree);//每道光线的度数，也就是就是图中的θ
        //绕周围照射一圈光线，照射次数与每道光线的度数有关
        //degree就是我图中的Δ
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
                        addPoint(pointsArray,centre_x + x,centre_y + y);
                        if(Systems.pointSimulator.isPointNotEmpty(centre_x + x,centre_y + y, self1.pointState, self2.pointState, this.enemy.pointState)) {
                            pointsArrayList.add(Systems.pointSimulator.getRoboMasterPoint(centre_x + x,centre_y + y));
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
                        addPoint(pointsArray,centre_x + x,centre_y + y);
                        if(Systems.pointSimulator.isPointNotEmpty(centre_x + x,centre_y + y, self1.pointState, self2.pointState, this.enemy.pointState)) {
                            pointsArrayList.add(Systems.pointSimulator.getRoboMasterPoint(centre_x + x,centre_y + y));
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
                        addPoint(pointsArray,centre_x + x,centre_y + y);
                        if(Systems.pointSimulator.isPointNotEmpty(centre_x + x,centre_y + y, self1.pointState, self2.pointState, this.enemy.pointState)) {
                            pointsArrayList.add(Systems.pointSimulator.getRoboMasterPoint(centre_x + x,centre_y + y));
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
                        addPoint(pointsArray,centre_x + x,centre_y + y);
                        if(Systems.pointSimulator.isPointNotEmpty(centre_x + x,centre_y + y, self1.pointState, self2.pointState, this.enemy.pointState)) {
                            pointsArrayList.add(Systems.pointSimulator.getRoboMasterPoint(centre_x + x,centre_y + y));
                            canstop = true;
                            break;
                        }
                    }
                    if(canstop) break;
                }
            }
        }
    }

    public void simulate3(int[][] EnemyObservationMapPoints){
        int[][] pointsArray = EnemyObservationMapPoints;
        Array<StatePoint> pointsArrayList = new Array<>();
        this.setPosition(enemy.getLidarPosition());

        int centre_x = position.x / 10;//原点的在大地图中的x轴相对位置
        int centre_y = position.y / 10;//原点的在大地图中的y轴相对位置
        float sectorAngleDegree = 1f;//每道光线的度数，也就是就是图中的θ
        float sectorAngleRadian = (float)Math.toRadians(sectorAngleDegree);//每道光线的度数，也就是就是图中的θ
        //绕周围照射一圈光线，照射次数与每道光线的度数有关
        //degree就是我图中的Δ
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
                        addPoint(pointsArray,centre_x + x,centre_y + y);
                        if(Systems.pointSimulator.isPointNotEmpty(centre_x + x,centre_y + y, self1.pointState, self2.pointState, this.enemy.pointState)) {
                            pointsArrayList.add(Systems.pointSimulator.getRoboMasterPoint(centre_x + x,centre_y + y));
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
                        addPoint(pointsArray,centre_x + x,centre_y + y);
                        if(Systems.pointSimulator.isPointNotEmpty(centre_x + x,centre_y + y, self1.pointState, self2.pointState, this.enemy.pointState)) {
                            pointsArrayList.add(Systems.pointSimulator.getRoboMasterPoint(centre_x + x,centre_y + y));
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
                        addPoint(pointsArray,centre_x + x,centre_y + y);
                        if(Systems.pointSimulator.isPointNotEmpty(centre_x + x,centre_y + y, self1.pointState, self2.pointState, this.enemy.pointState)) {
                            pointsArrayList.add(Systems.pointSimulator.getRoboMasterPoint(centre_x + x,centre_y + y));
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
                        addPoint(pointsArray,centre_x + x,centre_y + y);
                        if(Systems.pointSimulator.isPointNotEmpty(centre_x + x,centre_y + y, self1.pointState, self2.pointState, this.enemy.pointState)) {
                            pointsArrayList.add(Systems.pointSimulator.getRoboMasterPoint(centre_x + x,centre_y + y));
                            canstop = true;
                            break;
                        }
                    }
                    if(canstop) break;
                }
            }
        }
    }


    public void simulate2(int[][] EnemyObservationMapPoints, Array<StatePoint> eoArrayList){
        if(((Enemy)enemy).isLocked()) radius = 550;
        else radius = 550;
        int[][] pointsArray = EnemyObservationMapPoints;
        Array<StatePoint> pointsArrayList = new Array<>();
        this.setPosition(enemy.getLidarPosition());
        int centre_x = position.x / 10;
        int centre_y = position.y / 10;
        float precisionOfDegree = 0.01f;
        int x = 0;
        int y = 0;
        for(float degree = 0;degree < 360; degree += precisionOfDegree){
            float radian = degree * MathUtils.degreesToRadians;
            if(degree == 0 || degree == 180){
                for(y = 0;y < radius; y++){
                    if(degree == 0){
                        addPoint(pointsArray,centre_x,centre_y + y);
                        if(Systems.pointSimulator.isPointNotEmpty(centre_x, centre_y + y, self1.pointState, self2.pointState, this.enemy.pointState)){
                            pointsArrayList.add(Systems.pointSimulator.getRoboMasterPoint(centre_x, centre_y + y));
                            break;
                        }
                    }
                    else {
                        addPoint(pointsArray,centre_x,centre_y - y);
                        if(Systems.pointSimulator.isPointNotEmpty(centre_x, centre_y - y, self1.pointState, self2.pointState, this.enemy.pointState)){
                            pointsArrayList.add(Systems.pointSimulator.getRoboMasterPoint(centre_x, centre_y - y));
                            break;
                        }
                    }
                }
            }
            else if(degree == 90 || degree == 270){
                for(x = 0;x < radius; x++){
                    if(degree == 90){
                        addPoint(pointsArray,centre_x + x,centre_y);
                        if(Systems.pointSimulator.isPointNotEmpty(centre_x + x, centre_y, self1.pointState, self2.pointState, this.enemy.pointState)) {
                            pointsArrayList.add(Systems.pointSimulator.getRoboMasterPoint(centre_x + x, centre_y));
                            break;
                        }
                    }
                    else {
                        addPoint(pointsArray,centre_x - x,centre_y);
                        if(Systems.pointSimulator.isPointNotEmpty(centre_x - x, centre_y, self1.pointState, self2.pointState, this.enemy.pointState)) {
                            pointsArrayList.add(Systems.pointSimulator.getRoboMasterPoint(centre_x - x, centre_y));
                            break;
                        }
                    }
                }
            }
            else if(degree > 315 || degree < 45 || (degree > 135 && degree < 225)){
                for(y = 0;y < Math.abs(Math.cos(radian) * radius); y++){
                    int offset_x = (int) (Math.round(Math.tan(radian) * y));
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
                    addPoint(pointsArray,centre_x + offset_x,centre_y + offset_y);
                    if(Systems.pointSimulator.isPointNotEmpty(centre_x + offset_x, centre_y + offset_y, self1.pointState, self2.pointState, this.enemy.pointState)) {
                        pointsArrayList.add(Systems.pointSimulator.getRoboMasterPoint(centre_x + offset_x, centre_y + offset_y));
                        break;
                    }
                }
            }
            else{
                for(x = 0;x < Math.abs(Math.sin(radian) * radius); x++){
                    int offset_x = x;
                    int offset_y = (int) (Math.round(x / Math.tan(radian)));
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
                    addPoint(pointsArray,centre_x + offset_x,centre_y + offset_y);
                    if(Systems.pointSimulator.isPointNotEmpty(centre_x + offset_x, centre_y + offset_y, self1.pointState, self2.pointState, this.enemy.pointState)) {
                        pointsArrayList.add(Systems.pointSimulator.getRoboMasterPoint(centre_x + offset_x, centre_y + offset_y));
                        break;
                    }
                }
            }
            x = 0;
            y = 0;
        }
    }

    private void addPoint(int[][] pointsArray, int x, int y){
        Position position = new Position(x,y);
        if(!position.isInsideTheMap(true)) return;
        int pointValue = pointsArray[x][y];
        if(pointValue != this.weight){
            if(pointValue != 3){
                pointsArray[x][y] += this.weight;
            }
        }
    }
}
