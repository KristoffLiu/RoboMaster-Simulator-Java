"""
Breadth-First grid planning
author: Erwin Lejeune (@spida_rwin)
See Wikipedia article (https://en.wikipedia.org/wiki/Breadth-first_search)
"""

import math

import matplotlib.pyplot as plt

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

            if self.obmap[current.x][current.y] == 2:
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

        rx, ry = self.calc_grid_position(ngoal.x, self.minx), 
                 self.calc_grid_position(ngoal.y, self.miny)
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
    obmap = [[0 for _ in range(ywidth)]
                    for _ in range(xwidth)]
    for ix in range(xwidth):
        x = calc_grid_position(ix, minx)
        for iy in range(ywidth):
            y = calc_grid_position(iy, miny)
            for iox, ioy in zip(ox, oy):
                d = math.hypot(iox - x, ioy - y)
                if d <= rr:
                    obmap[ix][iy] = 1
                    break

def main():
    print(__file__ + " start!!")


    # set obstacle positions
    ox, oy = [], []
    for i in range(-10, 60):
        ox.append(i)
        oy.append(-10.0)
    for i in range(-10, 60):
        ox.append(60.0)
        oy.append(i)
    for i in range(-10, 61):
        ox.append(i)
        oy.append(60.0)
    for i in range(-10, 61):
        ox.append(-10.0)
        oy.append(i)
    for i in range(-10, 40):
        ox.append(20.0)
        oy.append(i)
    for i in range(0, 40):
        ox.append(40.0)
        oy.append(60.0 - i)

    if show_animation:  # pragma: no cover
        plt.plot(ox, oy, ".k")
        plt.plot(sx, sy, "og")
        plt.plot(gx, gy, "xb")
        plt.grid(True)
        plt.axis("equal")

    calc_obstacle_map(ox, oy)

    bfs = BreadthFirstSearchPlanner(obmap, grid_size, robot_radius)
    tx = bfs.calc_xyindex(gx, bfs.minx)
    ty = bfs.calc_xyindex(gy, bfs.miny)
    obmap[tx][ty] = 2
    rx, ry = bfs.planning(sx, sy)
    print("rx: ", rx)
    print("ry: ", ry)

    if show_animation:  # pragma: no cover
        plt.plot(rx, ry, "-r")
        plt.pause(0.01)
        plt.show()
    


if __name__ == '__main__':
    main()