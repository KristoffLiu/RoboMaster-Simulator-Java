from typing import Sequence
from py4j.java_gateway import JavaGateway
from py4j.java_gateway import java_import

gateway = JavaGateway() #启动py4j服务器
entrypoint = gateway.entry_point #获取服务器桥的入口

map = entrypoint.getMap() #直接获取matrixsimulator点阵模拟器实时生成的整张地图，类型为RoboMasterPoint[][]的java二维数组

java_import(gateway.jvm,'java.util.*') #导入java中的类的方法

robomaster = entrypoint.getRoboMaster("Blue1") #直接获取RoboMaster对象
#一共有四辆Robomaster，分别叫 Blue1, Blue2, Red1, Red2。
# robomaster.setPosition(5000,1000) #设置x, y坐标
robomaster.setPosition(5000,1000,float(1.57)) #设置x, y, yaw数值。 yaw用radian数值表示, 且必须是float类型
a = robomaster.getPosition() #获得roboMaster的坐标，类型为java的Position类
b = robomaster.getX() #获得roboMaster的x坐标，类型为int整型
c = robomaster.getY() #获得roboMaster的y坐标，类型为int整型
d = robomaster.getRotation() #获得roboMaster的yaw数值，类型为float浮点类型
e = robomaster.getEnemiesObservationSimulationResult() #获得roboMaster的敌军激光雷达观测视野合成模拟结果，类型为java的int[][]二维数组
#0 safe
#1 observe1only
#2 observe2only
#3 observeboth
f = robomaster.getLidarObservation() #获得roboMaster的激光雷达观测结果，类型为java的List<RoboMasterPoint>列表



# print(dir(a.getClass))
# print(b)
# print(c)
print(type(e))
# print(e[0][10])
# robomasterpoint = f[10]
# print(robomasterpoint.getX())
# print(map[0][1])