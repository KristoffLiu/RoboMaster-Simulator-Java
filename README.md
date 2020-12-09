# RoboMaster-libGDX-Simulator
这是一个船新的RoboMaster AI对抗赛特供模拟器，在后续更新中或许会增加业务范围
内置精确到毫米级的地图和车辆模型，搭配完善的物理引擎和裁判系统，用最小的计算量模拟到每一颗子弹的弹道
由于使用2D模拟，不会使用过量计算量计算无用的Z轴方向运动，在不变动结果的同时增加模拟速率
模拟器部分使用JAVA编写，core中分出四个包，分别运行裁判系统，车辆属性，模拟器以及其他
模拟器由五个Layer分别渲染，最小化无意义的重复渲染计算量
# Get Started
完全下载后进入RoboMaster-Simulator-Java/src/simulator/launcher/src/com/kristoff/robomaster_simulator/launcher运行DesktopLauncher.java即可
#依赖项
python3
pip
PyTorch
Gym
RLlib
py4j
# Simulator
依赖项都已集成在gradle里面了
下载后建议使用IDEA打开，因为这是libgdx项目，需要进行配置
在IDEA中打开File→open，进入Open File or Project界面
找到下载的RoboMaster-libGDX-Simulator文件，不要直接选OK，先进去
找到src/META-INF/build.gradle,选择此文件，点ok，然后在弹出的页面选Open as Project
恭喜你可以开始使用啦
