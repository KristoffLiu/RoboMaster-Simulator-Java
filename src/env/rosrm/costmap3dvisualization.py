# TODO:
# 1. 连接裁判系统，当裁判系统发布Five Second CD的时候，准备开启机器人
# 2. 测试到达终点时，脸朝着敌人
# 3. 

from py4j.java_gateway import JavaGateway
from py4j.java_gateway import java_import
import matplotlib
import time
#%matplotlib inline
import numpy as np
import matplotlib.pyplot as plt

gateway = JavaGateway() #启动py4j服务器
entrypoint = gateway.entry_point #获取服务器桥的入口

java_import(gateway.jvm,'java.util.*') #导入java中的类的方法

blue1 = entrypoint.getRoboMaster("Blue1")

plt.figure(figsize=(20,20), dpi = 80)
plt.ion()

while(True):
    # 定义x, y
    x = np.arange(0, 900,30)
    y = np.arange(0, 900,30)

    # 生成网格数据
    X, Y = np.meshgrid(y, x)

    b = []
    begintime = time.time()
    for i in range(0,900,30):
        a = []
        for j in range(0,900,30):
            if i > (900 - 849) / 2 and i < 849 + (900 - 849) / 2 and  j > (900 - 489) / 2 and  j < 489 + (900 - 489) / 2 :    
                m = int(i - (900 - 849) / 2 + 1)
                n = int(j - (900 - 489) / 2 + 1)
                if blue1.getCost(m,n) > 255:
                    a.append(255 - 128)
                else:
                    a.append(blue1.getCost(m, n) - 128)
            else:
                a.append(-128)
        ar = np.array(a)
        b.append(ar)

    # 计算Z轴的高度
    Z = np.array(b)

    ax = plt.axes(projection='3d')
    ax.plot_surface(X, Y, Z, rstride=1, cstride=1,
                    cmap="rainbow", edgecolor='none')
    ax.set_title('surface')

    plt.pause(0.2)
    
