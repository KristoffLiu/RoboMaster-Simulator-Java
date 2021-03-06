"""
Breadth-First grid planning
author: Erwin Lejeune (@spida_rwin)
See Wikipedia article (https://en.wikipedia.org/wiki/Breadth-first_search)
"""

from py4j.java_gateway import JavaGateway
from py4j.java_gateway import java_import

gateway = JavaGateway() #启动py4j服务器
entrypoint = gateway.entry_point #获取服务器桥的入口

java_import(gateway.jvm,'java.util.*') #导入java中的类的方法

import rospy
from geometry_msgs.msg import PoseStamped
from obstacle_detector.msg import Obstacles
from scipy.spatial.transform import Rotation as R
import threading
import math
import matplotlib.pyplot as plt
import time
import numpy as np


class Brain:
    class Robot:
        x, y, yaw = 0.0, 0.0, 0.0
        theta = 0.25
        def __init__(self, x, y, yaw):
            self.x, self.y, self.yaw = x, y, yaw
        def __str__(self):
            return "x: %.2f, y: %.2f" % (self.x, self.y)
        def __eq__(self, other):
            return abs(self.x - other.x) < self.theta and abs(self.y - other.y) < self.theta

        def __sub__(self, other):
            return self.x - other.x, self.y - other.y

    def __init__(self):
        self.cnt = 1
        self.Red1 = entrypoint.getRoboMaster("Red1") 
        self.Red2 = entrypoint.getRoboMaster("Red2") 
        self.Blue1 = entrypoint.getRoboMaster("Blue2") 
        self.Blue2 = entrypoint.getRoboMaster("Blue1") 

        self.robots = [self.Robot(0.0, 0.0, 0.0), self.Robot(0.0, 0.0, 0.0)]

        self._decision_pub = [rospy.Publisher("/CAR1/move_base_simple/goal", PoseStamped, queue_size=10),
                              rospy.Publisher("/CAR2/move_base_simple/goal", PoseStamped, queue_size=10)]
        self._robots_subscriber = [rospy.Subscriber("/CAR1/amcl_pose", PoseStamped, self.ownPositionCB0),
                                   rospy.Subscriber("/CAR2/amcl_pose", PoseStamped, self.ownPositionCB1)]
        self._enemies_subscriber = [rospy.Subscriber("/CAR1/obstacle_filtered", Obstacles, self.ownObservationCB0),
                                    rospy.Subscriber("/CAR2/obstacle_filtered", Obstacles, self.ownObservationCB1)]
        # self._decision_pub = [rospy.Publisher("/jackal0/move_base_simple/goal", PoseStamped, queue_size=10),
        #                       rospy.Publisher("/jackal1/move_base_simple/goal", PoseStamped, queue_size=10)]
        # self._debuff_subscriber = rospy.Subscriber("/debuff", String, self.receiveDebuffSignal)
        # self._robots_subscriber = [rospy.Subscriber("/jackal0/amcl_pose", PoseStamped, self.ownPositionCB0),
        #                            rospy.Subscriber("/jackal1/amcl_pose", PoseStamped, self.ownPositionCB1)]
        # self._enemies_subscriber = [rospy.Subscriber("/jackal0/obstacle_filtered", Obstacles, self.ownObservationCB0),
        #                             rospy.Subscriber("/jackal1/obstacle_filtered", Obstacles, self.ownObservationCB1)]

    def ownPositionCB0(self, msg):
        self.robots[0].x = msg.pose.position.x
        self.robots[0].y = msg.pose.position.y
        [y, p, r] = R.from_quat([msg.pose.orientation.x,
                                 msg.pose.orientation.y,
                                 msg.pose.orientation.z,
                                 msg.pose.orientation.w]).as_euler('zyx', degrees=True)
        self.robots[0].yaw = y
        self.Blue1.setPosition(int(msg.pose.position.x*1000), int(msg.pose.position.y*1000),float(1.57))

    def ownPositionCB1(self, msg):
        self.robots[1].x = msg.pose.position.x
        self.robots[1].y = msg.pose.position.y
        [y, p, r] = R.from_quat([msg.pose.orientation.x,
                                 msg.pose.orientation.y,
                                 msg.pose.orientation.z,
                                 msg.pose.orientation.w]).as_euler('zyx', degrees=True)
        self.robots[1].yaw = y
        self.Blue2.setPosition(int(msg.pose.position.x*1000), int(msg.pose.position.y*1000),float(1.57))

    def ownObservationCB0(self, data):
        enemy = data.circles
        if len(enemy) == 1:
            self.Red1.setPosition(int(enemy[0].center.x*1000), int(enemy[0].center.y*1000),float(1.57))
        elif len(enemy) == 2:
            self.Red1.setPosition(int(enemy[0].center.x*1000), int(enemy[0].center.y*1000),float(1.57))
            self.Red2.setPosition(int(enemy[1].center.x*1000), int(enemy[1].center.y*1000),float(1.57))

    def ownObservationCB1(self, data):
        enemy = data.circles
        if len(enemy) == 1:
            self.Red1.setPosition(int(enemy[0].center.x*1000), int(enemy[0].center.y*1000),float(1.57))
        elif len(enemy) == 2:
            self.Red1.setPosition(int(enemy[0].center.x*1000), int(enemy[0].center.y*1000),float(1.57))
            self.Red2.setPosition(int(enemy[1].center.x*1000), int(enemy[1].center.y*1000),float(1.57))
        
    def get_next_position(self):
        pos = self.Blue2.getPointAvoidingFacingEnemies()
        rx = pos.getX() / 100.0
        ry = pos.getY() / 100.0
        print("------", self.cnt)
        self.cnt = self.cnt+1
        print("x: ", rx)
        print("y: ", ry)
        goal = PoseStamped()
        goal.header.frame_id = "/map"
        goal.pose.position.x, goal.pose.position.y = rx, ry
        goal.pose.orientation.w = 1
    
        # yaw_angle = math.atan2(ry - self.robots[i].y, rx - self.robots[i].x)

        # theta = 0
        # if yaw_angle >= 0:
        #     theta = yaw_angle - math.pi
        # elif yaw_angle < 0:
        #     theta = math.pi + yaw_angle
        # else:
        #     rospy.logerr("invalid yaw")
        
        # #if i == 1:
        # #    theta += math.pi
        
        # theta = theta + np.random.uniform(-self._rangeAngle / 180 * math.pi, self._rangeAngle / 180 * math.pi)
        # theta = np.clip(theta, -math.pi, math.pi)
            
        
        # target.x, target.y = target.x + self._rho * math.cos(theta), target.y + self._rho * math.sin(theta)

        # [goal.pose.orientation.w,
        #     goal.pose.orientation.x,
        #     goal.pose.orientation.y,
        #     goal.pose.orientation.z] = self._createQuaternionFromYaw(yaw_angle)


        self._decision_pub[1].publish(goal)



def call_rosspin():
    rospy.spin()


if __name__ == '__main__':
    try: 
        print(__file__ + " start!!")
        rospy.init_node('decision_node', anonymous=True)
        rate = rospy.Rate(0.5)
        brain = Brain()
        print(brain)
        spin_thread = threading.Thread(target=call_rosspin).start()

        while not rospy.core.is_shutdown():
            brain.get_next_position()
            rate.sleep()

    except rospy.ROSInterruptException:
        pass

