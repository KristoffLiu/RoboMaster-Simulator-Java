from typing import Sequence
from py4j.java_gateway import JavaGateway
from py4j.java_gateway import java_import
import time, datetime

gateway = JavaGateway()          #启动py4j服务器
entrypoint = gateway.entry_point #获取服务器桥的入口

map = entrypoint.getMap() #直接获取matrixsimulator点阵模拟器实时生成的整张地图，类型为RoboMasterPoint[][]的java二维数组

java_import(gateway.jvm,'java.util.*') #导入java中的类的方法

robomaster = entrypoint.getRoboMaster("Blue1") #直接获取RoboMaster对象
# 一共有四辆Robomaster，分别叫 Blue1, Blue2, Red1, Red2。
# robomaster.setPosition(5000,1000) #设置x, y坐标

robomaster.setPosition(700,1600,float(0.6)) #设置x, y, yaw数值。 yaw用radian数值表示, 且必须是float类型

a = robomaster.getPosition() #获得roboMaster的坐标，类型为java的Position类
b = robomaster.getX() #获得roboMaster的x坐标，类型为int整型
c = robomaster.getY() #获得roboMaster的y坐标，类型为int整型
d = robomaster.getRotation() #获得roboMaster的yaw数值，类型为float浮点类型
#e = robomaster.getEnemiesObservationSimulationResult() #获得roboMaster的敌军激光雷达观测视野合成模拟结果，类型为java的int[][]二维数组
#0 safe
#1 observe1only
#2 observe2only
#3 observeboth
f = robomaster.getLidarObservation() #获得roboMaster的激光雷达观测结果，类型为java的List<RoboMasterPoint>列表


blue2 = entrypoint.getRoboMaster("Blue2")
blue2.setPosition(6000,1600,float(1)) #设置x, y, yaw数值。 yaw用radian数值表示, 且必须是float类型
red1 = entrypoint.getRoboMaster("Red1")
red1.setPosition(5500,600,float(1)) #设置x, y, yaw数值。 yaw用radian数值表示, 且必须是float类型
red2 = entrypoint.getRoboMaster("Red2")
red2.setPosition(7600,2600,float(1)) #设置x, y, yaw数值。 yaw用radian数值表示, 且必须是float类型

# for i in range(100):
#     robomaster.setPosition(robomaster.getX() + 60, robomaster.getY() - 30, float(1.57))

#     robomaster3.setPosition(robomaster3.getX() - 40, robomaster3.getY() - 40, float(0))
#     robomaster4.setPosition(robomaster4.getX() - 40, robomaster4.getY() + 40, float(0))
#     robomaster.getNextPredictedPosition()

#robomaster.getPointAvoidingFacingEnemies()

decisionPoint = robomaster.getDecisionMade()
print(decisionPoint.getX())
print(decisionPoint.getY())

decisionPoint2 = blue2.getDecisionMade()
print(decisionPoint2.getX())
print(decisionPoint2.getY())

# for i in range(500):
#     robomaster.loseHealth(10)
#     time.sleep(0.01)

starttime = datetime.datetime.now()
#long running
for i in entrypoint.getDemoList():
    print(i)
endtime = datetime.datetime.now()
timetaken = (endtime - starttime)
print(timetaken)

starttime = datetime.datetime.now()
for i in range(500):
    print(i)
endtime = datetime.datetime.now()
print(timetaken)

