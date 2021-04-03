import matplotlib
import time
from mpl_toolkits import mplot3d
#%matplotlib inline
import numpy as np
import matplotlib.pyplot as plt

# 定义x, y
x = np.arange(0, 900,10)
y = np.arange(0, 900,10)

print(type(x))
print(type(y))


# 生成网格数据
X, Y = np.meshgrid(y, x)

print(type(X))
print(type(Y))

b = []

begintime = time.time()
for i in range(0,900,10):
    a = []
    for j in range(0,900,10):
        if i > (900 - 849) / 2 and i < 849 + (900 - 849) / 2 and  j > (900 - 489) / 2 and  j < 489 + (900 - 489) / 2 :    
            m = int(i - (900 - 849) / 2 + 1)
            n = int(j - (900 - 489) / 2 + 1)
            a.append(50)
        else:
            a.append(-128)
    ar = np.array(a)
    b.append(ar)
endtime = time.time()
print(endtime - begintime)


# 计算Z轴的高度
Z = np.array(b) 
print(Z)

print(type(Z))

ax = plt.axes(projection='3d')
ax.plot_surface(X, Y, Z, rstride=1, cstride=1,
                cmap="rainbow", edgecolor='none')
ax.set_title('surface')
plt.show()