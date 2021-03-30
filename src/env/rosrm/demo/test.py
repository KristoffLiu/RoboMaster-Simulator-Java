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
from roborts_msgs.msg import GameRobotHP
from roborts_msgs.msg import GameZoneArray
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
        self.cnt = 1
        self._control_rate = control_rate
        self.Red1 = entrypoint.getRoboMaster("Red1") 
        self.Red2 = entrypoint.getRoboMaster("Red2") 
        self.Blue1 = entrypoint.getRoboMaster("Blue2") 
        self.Blue2 = entrypoint.getRoboMaster("Blue1") 

        self.robots = [self.Robot(0.0, 0.0, 0.0), self.Robot(0.0, 0.0, 0.0)]

        self._decision_pub = [rospy.Publisher("/jackal0/goal", PoseStamped, queue_size=10),
                              rospy.Publisher("/jackal1/goal", PoseStamped, queue_size=10)]
        # self._robots_subscriber = [rospy.Subscriber("/jackal0/amcl_pose", PoseStamped, self.ownPositionCB0),
        #                            rospy.Subscriber("/jackal1/amcl_pose", PoseStamped, self.ownPositionCB1),
        #                            rospy.Subscriber("/jackal2/amcl_pose", PoseStamped, self.ownPositionCB2),
        #                            rospy.Subscriber("/jackal3/amcl_pose", PoseStamped, self.ownPositionCB3)]

        # self._decision_pub = [rospy.Publisher("/CAR1/move_base_simple/goal", PoseStamped, queue_size=10),
        #                 rospy.Publisher("/CAR2/move_base_simple/goal", PoseStamped, queue_size=10)]
        # self._robots_subscriber = [rospy.Subscriber("/CAR1/amcl_pose", PoseStamped, self.ownPositionCB0),
        #                            rospy.Subscriber("/CAR2/amcl_pose", PoseStamped, self.ownPositionCB1)]

        # self._enemies_subscriber = rospy.Subscriber("/obstacle_preprocessed", Obstacles, self.enemyInfo)

        self._hp_subscriber = rospy.Subscriber("/CAR1/game_robot_hp", GameRobotHP, self.robotHP)
        self._buff_zone_subscriber = rospy.Subscriber("/CAR1/game_zone_array_status", GameZoneArray, self.gameZone)
        self._vis_pub = [rospy.Publisher("/CAR1/visualization_marker", Marker, queue_size=10),
                         rospy.Publisher("/CAR2/visualization_marker", Marker, queue_size=10)]

        _thread.start_new_thread(self.get_fake_amcl, ('jackal0',))
        _thread.start_new_thread(self.get_fake_amcl, ('jackal1',))
        _thread.start_new_thread(self.get_fake_amcl, ('jackal2',))
        _thread.start_new_thread(self.get_fake_amcl, ('jackal3',))

    def get_fake_amcl(self, robot_ns):
        tfBuffer = tf2_ros.Buffer()
        listener = tf2_ros.TransformListener(tfBuffer)
        rate = rospy.Rate(10.0)
        while 1:
            try:
                rate.sleep()
                fake_amcl = tfBuffer.lookup_transform('map', robot_ns+'/base_link', rospy.Time())
                print(fake_amcl)
            except (tf2_ros.LookupException, tf2_ros.ConnectivityException, tf2_ros.ExtrapolationException, rospy.ROSInterruptException):
                continue

    def ownPositionCB0(self, msg):
        self.robots[0].x = msg.pose.position.x
        self.robots[0].y = msg.pose.position.y
        [y, p, r] = R.from_quat([msg.pose.orientation.x,
                                 msg.pose.orientation.y,
                                 msg.pose.orientation.z,
                                 msg.pose.orientation.w]).as_euler('zyx', degrees=True)
        self.robots[0].yaw = y
        self.Blue1.setPosition(int(msg.pose.position.x*1000), int(msg.pose.position.y*1000),float(y))

    def ownPositionCB1(self, msg):
        self.robots[1].x = msg.pose.position.x
        self.robots[1].y = msg.pose.position.y
        [y, p, r] = R.from_quat([msg.pose.orientation.x,
                                 msg.pose.orientation.y,
                                 msg.pose.orientation.z,
                                 msg.pose.orientation.w]).as_euler('zyx', degrees=True)
        self.robots[1].yaw = y
        self.Blue2.setPosition(int(msg.pose.position.x*1000), int(msg.pose.position.y*1000),float(y))

        def ownPositionCB2(self, msg):
            [y, p, r] = R.from_quat([msg.pose.orientation.x,
                                        msg.pose.orientation.y,
                                        msg.pose.orientation.z,
                                        msg.pose.orientation.w]).as_euler('zyx', degrees=True)
            self.Red1.setPosition(int(msg.pose.position.x*1000), int(msg.pose.position.y*1000),float(y))

    def ownPositionCB2(self, msg):
        [y, p, r] = R.from_quat([msg.pose.orientation.x,
                                msg.pose.orientation.y,
                                msg.pose.orientation.z,
                                msg.pose.orientation.w]).as_euler('zyx', degrees=True)
        self.Red1.setPosition(int(msg.pose.position.x*1000), int(msg.pose.position.y*1000),float(y))

    def ownPositionCB3(self, msg):
        [y, p, r] = R.from_quat([msg.pose.orientation.x,
                                msg.pose.orientation.y,
                                msg.pose.orientation.z,
                                msg.pose.orientation.w]).as_euler('zyx', degrees=True)
        self.Red2.setPosition(int(msg.pose.position.x*1000), int(msg.pose.position.y*1000),float(y))

    def enemyInfo(self, data):
        enemy = data.circles
        if len(enemy) == 1:
            self.Red1.setPosition(int(enemy[0].center.x*1000), int(enemy[0].center.y*1000),float(1.57))
        elif len(enemy) == 2:
            self.Red1.setPosition(int(enemy[0].center.x*1000), int(enemy[0].center.y*1000),float(1.57))
            self.Red2.setPosition(int(enemy[1].center.x*1000), int(enemy[1].center.y*1000),float(1.57))

    def robotHP(self, data):
        print(data.blue1)
        print(data.blue2)
        print(data.red1)
        print(data.red2)
        self.Blue1.setHealth(data.blue1)
        self.Blue2.setHealth(data.blue2)
        self.Red1.setHealth(data.red1)
        self.Red2.setHealth(data.red2)
    
    def gameZone(self, data):
        print("****")
        # print("0, " + str(data.zone[0]))
        # print("1, " + str(data.zone[1]))
        # print("2, " + str(data.zone[2]))
        # print("3, " + str(data.zone[3]))
        # print("4, " + str(data.zone[4]))
        # print("5, " + str(data.zone[5]))



        # for i, d in enumerate(data.zone):
        #     print(str(i) + " " + str(d.type) + " " + str(d.active))
        #     entrypoint.updateBuffZone(i, d.type, d.active)
            
    def _createQuaternionFromYaw(self, yaw):
        # input: r p y
        r = R.from_euler('zyx', [0, 0, yaw], degrees=False).as_quat()
        # output: w x y z
        return [r[3], r[2], r[1], r[0]]

    def get_next_position1(self):
        # pos = self.Blue2.getPointAvoidingFacingEnemies()
        pos = self.Blue1.getDecisionMade()
        rx = pos.getX() / 100.0
        ry = pos.getY() / 100.0

        enemy = entrypoint.getLockedEnemy()
        enemyPosition = enemy.getPointPosition()
        gx = enemyPosition.getX() / 100.0
        gy = enemyPosition.getY() / 100.0

        eDistance = math.dist([self.robots[0].old_goal_x, self.robots[0].old_goal_y], [rx, ry]) 
        if (eDistance < 0.15):
            self.robots[0].old_goal_x, self.robots[0].old_goal_y = rx, ry
            return

        print("------Blue1", self.cnt)
        self.cnt = self.cnt+1
        print("x: ", rx)
        print("y: ", ry)

        yaw_angle = math.atan2(gy - ry, gx - rx)

        goal = PoseStamped()
        goal.header.frame_id = "/map"
        goal.pose.position.x, goal.pose.position.y = rx, ry

        [goal.pose.orientation.w,
        goal.pose.orientation.x,
        goal.pose.orientation.y,
        goal.pose.orientation.z] = self._createQuaternionFromYaw(yaw_angle)

        # self._decision_pub[0].publish(goal)
        print("blue 1  -> send")

        mark = Marker()
        mark.header.frame_id = "/map"
        mark.header.stamp = rospy.Time.now()
        mark.ns = "showen_point"
        mark.id = 0
        mark.type = Marker().ARROW
        mark.action = Marker().ADD
        mark.pose = goal.pose
        mark.scale.x = 0.4
        mark.scale.y = 0.05
        mark.scale.z = 0.05
        mark.color.a = 1.0
        mark.color.r = 0.2
        mark.color.g = 1.0
        mark.color.b = 0.3;
        mark.lifetime = rospy.Duration(self._control_rate, 0)
        self._vis_pub[0].publish(mark)

    def get_next_position2(self):
        # pos = self.Blue2.getPointAvoidingFacingEnemies()
        pos = self.Blue2.getDecisionMade()
        rx = pos.getX() / 100.0
        ry = pos.getY() / 100.0

        enemy = entrypoint.getLockedEnemy()
        enemyPosition = enemy.getPointPosition()
        gx = enemyPosition.getX() / 100.0
        gy = enemyPosition.getY() / 100.0

        eDistance = math.dist([self.robots[1].old_goal_x, self.robots[1].old_goal_y], [rx, ry]) 
        if (eDistance < 0.15):
            self.robots[1].old_goal_x, self.robots[1].old_goal_y = rx, ry
            return

        print("------Blue2", self.cnt)
        self.cnt = self.cnt+1
        print("x: ", rx)
        print("y: ", ry)

        yaw_angle = math.atan2(gy - ry, gx - rx)

        goal = PoseStamped()
        goal.header.frame_id = "/map"
        goal.pose.position.x, goal.pose.position.y = rx, ry

        [goal.pose.orientation.w,
        goal.pose.orientation.x,
        goal.pose.orientation.y,
        goal.pose.orientation.z] = self._createQuaternionFromYaw(yaw_angle)
        
        # self._decision_pub[1].publish(goal)

        mark = Marker()
        mark.header.frame_id = "/map"
        mark.header.stamp = rospy.Time.now()
        mark.ns = "showen_point"
        mark.id = 1
        mark.type = Marker().ARROW
        mark.action = Marker().ADD
        mark.pose = goal.pose
        mark.scale.x = 0.4
        mark.scale.y = 0.05
        mark.scale.z = 0.05
        mark.color.a = 1.0
        mark.color.r = 0.2
        mark.color.g = 1.0
        mark.color.b = 0.3;
        mark.lifetime = rospy.Duration(self._control_rate, 0)
        self._vis_pub[1].publish(mark)



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
            brain.get_next_position2()
            rate.sleep()

    except rospy.ROSInterruptException:
        pass

