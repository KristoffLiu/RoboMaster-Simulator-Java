from typing import Sequence
from py4j.java_gateway import JavaGateway
from py4j.java_gateway import java_import
import time, datetime

gateway = JavaGateway()          #启动py4j服务器
entrypoint = gateway.entry_point #获取服务器桥的入口

map = entrypoint.getMap() #直接获取matrixsimulator点阵模拟器实时生成的整张地图，类型为RoboMasterPoint[][]的java二维数组

java_import(gateway.jvm,'java.util.*') #导入java中的类的方法

red2 = entrypoint.getRoboMaster("Red2") #直接获取RoboMaster对象
red2.setHealth(0)