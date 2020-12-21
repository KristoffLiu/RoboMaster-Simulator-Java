from py4j.java_gateway import JavaGateway
from py4j.java_gateway import java_import

gateway = JavaGateway() #启动py4j服务器
entrypoint = gateway.entry_point #获取服务器桥的入口

map = entrypoint.getMap() #直接获取matrixsimulator点阵模拟器实时生成的整张地图，类型为RoboMasterPoint[][]的java二维数组

java_import(gateway.jvm,'java.util.*') #导入java中的类的方法

robomaster = entrypoint.getRoboMaster("Blue1") #直接获取RoboMaster对象
#一共有四辆Robomaster，分别叫 Blue1, Blue2, Red1, Red2。
robomaster.setPosition(1000,1000) #设置x, y坐标
robomaster.setPosition(1000,1000,float(0.0)) #设置x, y, yaw数值。 yaw用radian数值表示, 且必须是float类型
robomaster.getPosition() #获得roboMaster的坐标，类型为java的Position类
robomaster.getX() #获得roboMaster的x坐标，类型为int整型
robomaster.getY() #获得roboMaster的y坐标，类型为int整型
robomaster.getRotation() #获得roboMaster的yaw数值，类型为float浮点类型
robomaster.getEnemiesObservationSimulationResult() #获得roboMaster的敌军激光雷达观测视野合成模拟结果，类型为java的int[][]二维数组
robomaster.getLidarObservation() #获得roboMaster的激光雷达观测结果，类型为java的List<RoboMasterPoint>列表