# Guide Of Connection

## launch the ROS connection

```bash
$ source ~/catkin_ws/devel/setup.bash
$ roslaunch fkie_master_discovery master_discovery.launch
```

## before connecting to the real machine

```bash
$ source ~/roborts_ws/devel/setup.bash
$ cd RoboMaster-Simulator-Java/src/env/rosrm
```

## Run the code 
```
$ python decision.py
rosrun rviz rviz -d ~/Desktop/config.rviz

```