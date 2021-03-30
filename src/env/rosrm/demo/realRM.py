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
import threading
import math
import matplotlib.pyplot as plt
import time
import copy

show_animation = True


minx = 0
miny = 0
maxx = 0
maxy = 0
xwidth = 0
ywidth = 0
obmap = 0
# start and goal position
sx = 10.0  # [m]
sy = 10.0  # [m]
gx = 50.0  # [m]
gy = 50.0  # [m]
grid_size = 2.0  # [m]
robot_radius = 1.0  # [m]
reso = grid_size
rr = robot_radius

class BreadthFirstSearchPlanner:

    def __init__(self, obmap, reso, rr):
        """
        Initialize grid map for bfs planning
        ox: x position list of Obstacles [m]
        oy: y position list of Obstacles [m]
        resolution: grid resolution [m]
        rr: robot radius[m]
        """
        global minx, miny, maxx, maxy, xwidth, ywidth
        self.minx   = minx
        self.miny   = miny
        self.maxx   = maxx
        self.maxy   = maxy
        self.xwidth = xwidth
        self.ywidth = ywidth
        print("init minx: ", self.minx)
        print("init miny: ", self.miny)
        print("init maxx: ", self.maxx)
        print("init maxy: ", self.maxy)
        print("init xwidth: ", self.xwidth)
        print("init ywidth: ", self.ywidth)

        self.reso = reso
        self.rr = rr
        self.obmap = obmap
        # self.calc_obstacle_map(ox, oy)
        self.motion = self.get_motion_model()

    class Node:
        def __init__(self, x, y, cost, parent_index, parent):
            self.x = x  # index of grid
            self.y = y  # index of grid
            self.cost = cost
            self.parent_index = parent_index
            self.parent = parent

        def __str__(self):
            return str(self.x) + "," + str(self.y) + "," + str(
                self.cost) + "," + str(self.parent_index)

    def planning(self, sx, sy):
        """
        Breadth First search based planning
        input:
            s_x: start x position [m]
            s_y: start y position [m]
            gx: goal x position [m]
            gy: goal y position [m]
        output:
            rx: x position list of the final path
            ry: y position list of the final path
        """

        nstart = self.Node(self.calc_xyindex(sx, self.minx),
                           self.calc_xyindex(sy, self.miny), 0.0, -1, None)
        ngoal = nstart
        # ngoal = self.Node(self.calc_xyindex(gx, self.minx),
        #                   self.calc_xyindex(gy, self.miny), 0.0, -1, None)

        open_set, closed_set = dict(), dict()
        open_set[self.calc_grid_index(nstart)] = nstart

        while 1:
            if len(open_set) == 0:
                print("Open set is empty..")
                break

            current = open_set.pop(list(open_set.keys())[0])

            c_id = self.calc_grid_index(current)

            closed_set[c_id] = current

            # show graph
            if show_animation:  # pragma: no cover
                plt.plot(self.calc_grid_position(current.x, self.minx),
                         self.calc_grid_position(current.y, self.miny), "xc")
                # for stopping simulation with the esc key.
                plt.gcf().canvas.mpl_connect('key_release_event',
                                             lambda event:
                                             [exit(0) if event.key == 'escape'
                                              else None])
                if len(closed_set.keys()) % 10 == 0:
                    plt.pause(0.001)

            if self.obmap[current.x][current.y] == 3:
                print("Find goal")
                ngoal.parent_index = current.parent_index
                ngoal.cost = current.cost
                ngoal.x = current.x
                ngoal.y = current.y
                break

            # expand_grid search grid based on motion model
            for i, _ in enumerate(self.motion):
                node = self.Node(current.x + self.motion[i][0],
                                 current.y + self.motion[i][1],
                                 current.cost + self.motion[i][2], c_id, None)
                n_id = self.calc_grid_index(node)

                # If the node is not safe, do nothing
                if not self.verify_node(node):
                    continue

                if (n_id not in closed_set) and (n_id not in open_set):
                    node.parent = current
                    open_set[n_id] = node

        rx, ry = self.calc_grid_position(ngoal.x, self.minx), self.calc_grid_position(ngoal.y, self.miny)
        return rx, ry

    def calc_final_path(self, ngoal, closedset):
        # generate final course
        rx, ry = [self.calc_grid_position(ngoal.x, self.minx)], [
            self.calc_grid_position(ngoal.y, self.miny)]
        n = closedset[ngoal.parent_index]
        while n is not None:
            rx.append(self.calc_grid_position(n.x, self.minx))
            ry.append(self.calc_grid_position(n.y, self.miny))
            n = n.parent

        return rx, ry

    def calc_grid_position(self, index, minp):
        """
        calc grid position
        :param index:
        :param minp:
        :return:
        """
        pos = index * self.reso + minp
        return pos

    def calc_xyindex(self, position, min_pos):
        return round((position - min_pos) / self.reso)

    def calc_grid_index(self, node):
        return (node.y - self.miny) * self.xwidth + (node.x - self.minx)

    def verify_node(self, node):
        px = self.calc_grid_position(node.x, self.minx)
        py = self.calc_grid_position(node.y, self.miny)

        if px < self.minx:
            return False
        elif py < self.miny:
            return False
        elif px >= self.maxx:
            return False
        elif py >= self.maxy:
            return False

        # collision check
        if self.obmap[node.x][node.y] == 1:
            return False

        return True

    @staticmethod
    def get_motion_model():
        # dx, dy, cost
        motion = [[1, 0, 1],
                  [0, 1, 1],
                  [-1, 0, 1],
                  [0, -1, 1],
                  [-1, -1, math.sqrt(2)],
                  [-1, 1, math.sqrt(2)],
                  [1, -1, math.sqrt(2)],
                  [1, 1, math.sqrt(2)]]

        return motion

def calc_grid_position(index, minp):
    """
    calc grid position
    :param index:
    :param minp:
    :return:
    """
    pos = index * reso + minp
    return pos

def calc_obstacle_map(ox, oy):
    global minx, miny, maxx, maxy, xwidth, ywidth, obmap
    minx = round(min(ox))
    miny = round(min(oy))
    maxx = round(max(ox))
    maxy = round(max(oy))
    print("min_x:", minx)
    print("min_y:", miny)
    print("max_x:", maxx)
    print("max_y:", maxy)

    xwidth = round((maxx - minx) / reso)
    ywidth = round((maxy - miny) / reso)
    print("x_width:", xwidth)
    print("y_width:", ywidth)

    # obstacle map generation
    obmap = [[False for _ in range(ywidth)]
                    for _ in range(xwidth)]
    for ix in range(xwidth):
        x = calc_grid_position(ix, minx)
        for iy in range(ywidth):
            y = calc_grid_position(iy, miny)
            for iox, ioy in zip(ox, oy):
                d = math.hypot(iox - x, ioy - y)
                if d <= rr:
                    obmap[ix][iy] = True
                    break

#!/usr/bin/env python


def callback(data):
    rospy.loginfo(rospy.get_caller_id() + "I heard %s", data.data)
    
class Brain():
    def __init__(self):
        # self._decision_pub = [rospy.Publisher("/CAR1/move_base_simple/goal", PoseStamped, queue_size=10),
        #                       rospy.Publisher("/CAR2/move_base_simple/goal", PoseStamped, queue_size=10)]
        # self._debuff_subscriber = rospy.Subscriber("/debuff", String, self.receiveDebuffSignal)
        # self._robots_subscriber = [rospy.Subscriber("/CAR1/amcl_pose", PoseStamped, self.ownPositionCB0),
        #                            rospy.Subscriber("/CAR2/amcl_pose", PoseStamped, self.ownPositionCB1)]
        # self._enemies_subscriber = [rospy.Subscriber("/CAR1/obstacle_filtered", Obstacles, self.ownObservationCB0),
        #                             rospy.Subscriber("/CAR2/obstacle_filtered", Obstacles, self.ownObservationCB1)]
        # self._decision_pub = [rospy.Publisher("/jackal0/move_base_simple/goal", PoseStamped, queue_size=10),
        #                       rospy.Publisher("/jackal1/move_base_simple/goal", PoseStamped, queue_size=10)]
        # self._debuff_subscriber = rospy.Subscriber("/debuff", String, self.receiveDebuffSignal)
        # self._robots_subscriber = [rospy.Subscriber("/jackal0/amcl_pose", PoseStamped, self.ownPositionCB0),
        #                            rospy.Subscriber("/jackal1/amcl_pose", PoseStamped, self.ownPositionCB1)]

        # self._enemies_subscriber = [rospy.Subscriber("/jackal0/obstacle_filtered", Obstacles, self.ownObservationCB0),
        #                             rospy.Subscriber("/jackal1/obstacle_filtered", Obstacles, self.ownObservationCB1)]

        self.blue1 = entrypoint.getRoboMaster("Blue1") #直接获取RoboMaster对象
        self.bfs = BreadthFirstSearchPlanner(None, 100, None)


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

    def ownPositionCB0(self, msg):
        self.robots[0].x = msg.pose.position.x
        self.robots[0].y = msg.pose.position.y
        [y, p, r] = R.from_quat([msg.pose.orientation.x,
                                 msg.pose.orientation.y,
                                 msg.pose.orientation.z,
                                 msg.pose.orientation.w]).as_euler('zyx', degrees=True)
        self.robots[0].yaw = y

    def ownPositionCB1(self, msg):
        self.robots[1].x = msg.pose.position.x
        self.robots[1].y = msg.pose.position.y
        [y, p, r] = R.from_quat([msg.pose.orientation.x,
                                 msg.pose.orientation.y,
                                 msg.pose.orientation.z,
                                 msg.pose.orientation.w]).as_euler('zyx', degrees=True)
        self.robots[1].yaw = y

    def ownObservationCB0(self, data):
        self.robot_obs[0] = []
        for enemy in data.circles:
            self.robot_obs[0].append(self.Robot(enemy.center.x, enemy.center.y, 0.0))
        self.observation = self.robot_obs[0] + self.robot_obs[1]
        # print("robot0 obs", len(self.robot_obs[0]))
        # self.merge_observation()

    def ownObservationCB1(self, data):
        self.robot_obs[1] = []
        for enemy in data.circles:
            self.robot_obs[1].append(self.Robot(enemy.center.x, enemy.center.y, 0.0))
        self.observation = self.robot_obs[0] + self.robot_obs[1]
        # print("robot1 obs", len(self.robot_obs[1]))
        # self.merge_observation()


    def update_map(self):
        self.bfs.obmap = self.blue1.getEnemiesObservationSimulationResult()
        self.bfs.minx = 0
        self.bfs.miny = 0
        self.bfs.maxx = len(self.bfs.obmap)
        self.bfs.maxy = len(self.bfs.obmap[0])
        self.bfs.xwidth = (self.bfs.maxx - self.bfs.minx) / self.bfs.reso
        self.bfs.ywidth = (self.bfs.maxy - self.bfs.miny) / self.bfs.reso
        # print(self.bfs.minx)
        # print(self.bfs.miny) 
        print(self.bfs.maxx) 
        print(self.bfs.maxy) 
        # print(self.bfs.xwidth)
        # print(self.bfs.ywidth)
        start = time.time()
        # a = copy.deepcopy(self.blue1.getEnemiesObservationSimulationResult())
        a = self.bfs.obmap
        for i in range(self.bfs.maxx):
            for j in range(self.bfs.maxy):
                a[i][j] = 6
                # print(self.bfs.obmap[i][j], end=' ')
            # print("")

        #计算时间消耗，特别是对程序运行时间消耗
        end = time.time()
        print("循环运行时间:%.2f秒"%(end-start))
        #output:循环运行时间:5.50秒
    
    def find_next_position(self):
        #0 safe
        #1 observe 1only
        #2 observe 2only
        #3 observe both
        rx, ry = self.bfs.planning(sx, sy)

        goal = PoseStamped()
        goal.header.frame_id = "/map"
        goal.pose.position.x, goal.pose.position.y = rx, ry

        self._decision_pub[0].publish(goal)


def call_rosspin():
    rospy.spin()

if __name__ == '__main__':
    try: 
        print(__file__ + " start!!")
        # rospy.init_node('decision_node', anonymous=True)
        # rate = rospy.Rate(0.001)
        brain = Brain()
        print(brain)
        # spin_thread = threading.Thread(target=call_rosspin).start()

        brain.update_map()
        # while not rospy.core.is_shutdown():
        #     brain.update_map()
        #     brain.find_next_position()
        #     rate.sleep()

    except rospy.ROSInterruptException:
        pass