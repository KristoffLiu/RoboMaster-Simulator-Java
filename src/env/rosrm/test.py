"""
Breadth-First grid planning
author: Erwin Lejeune (@spida_rwin)
See Wikipedia article (https://en.wikipedia.org/wiki/Breadth-first_search)
"""

# TODO:
# 1. 连接裁判系统，当裁判系统发布Five Second CD的时候，准备开启机器人
# 2. 测试到达终点时，脸朝着敌人
# 3. 

from py4j.java_gateway import JavaGateway
from py4j.java_gateway import java_import
import time

gateway = JavaGateway() #启动py4j服务器
entrypoint = gateway.entry_point #获取服务器桥的入口

java_import(gateway.jvm,'java.util.*') #导入java中的类的方法

import rospy
from geometry_msgs.msg import PoseStamped
from obstacle_detector.msg import Obstacles
from roborts_msgs.msg import GameRobotHP
from roborts_msgs.msg import GameZoneArray
from roborts_msgs.msg import GameStatus
from roborts_msgs.msg import GameRobotBullet
from roborts_msgs.msg import RobotDamage
from visualization_msgs.msg import Marker
from scipy.spatial.transform import Rotation as R
import threading
import math
import matplotlib.pyplot as plt
import time
import numpy as np
import _thread


class Brain:
    class Robot:
        x, y, yaw = 0.0, 0.0, 0.0
        old_goal_x, old_goal_y = 0.0, 0.0
        theta = 0.25
        def __init__(self, x, y, yaw):
            self.x, self.y, self.yaw = x, y, yaw
        def __str__(self):
            return "x: %.2f, y: %.2f" % (self.x, self.y)
        def __eq__(self, other):
            return abs(self.x - other.x) < self.theta and abs(self.y - other.y) < self.theta

        def __sub__(self, other):
            return self.x - other.x, self.y - other.y

    def __init__(self, control_rate):
        self.is_game_start = False
        self.cnt = 1
        self._control_rate = control_rate

        entrypoint.setAsRoamer("blue1")
        entrypoint.isOurTeamBlue(False)

        self.Red1 = entrypoint.getRoboMaster("Red1") 
        self.Red2 = entrypoint.getRoboMaster("Red2") 
        self.Blue1 = entrypoint.getRoboMaster("Blue1") 
        self.Blue2 = entrypoint.getRoboMaster("Blue2") 

        self.robots = [self.Robot(0.0, 0.0, 0.0), self.Robot(0.0, 0.0, 0.0)]

        self._decision_pub = [rospy.Publisher("/CAR1/move_base_simple/goal", PoseStamped, queue_size=10),
                        rospy.Publisher("/CAR2/move_base_simple/goal", PoseStamped, queue_size=10)]

            
    def _createQuaternionFromYaw(self, yaw):
        # input: r p y
        r = R.from_euler('zyx', [0, 0, yaw], degrees=False).as_quat()
        # output: w x y z
        return [r[3], r[2], r[1], r[0]]

    def get_next_position1(self):
        goal = PoseStamped()
        goal.header.frame_id = "/map"
        goal.pose.position.x, goal.pose.position.y = 1, 1

        [goal.pose.orientation.w,
        goal.pose.orientation.x,
        goal.pose.orientation.y,
        goal.pose.orientation.z] = self._createQuaternionFromYaw(1)

        self._decision_pub[0].publish(goal)
        print("blue 1  -> send")

def call_rosspin():
    rospy.spin()


if __name__ == '__main__':
    try: 
        print(__file__ + " start!!")
        rospy.init_node('decision_node', anonymous=True)
        control_rate = 2
        rate = rospy.Rate(1.0 / control_rate)
        brain = Brain(control_rate)
        print(brain)
        spin_thread = threading.Thread(target=call_rosspin).start()


        while not rospy.core.is_shutdown():
            brain.get_next_position1()
            rate.sleep()

    except rospy.ROSInterruptException:
        pass

