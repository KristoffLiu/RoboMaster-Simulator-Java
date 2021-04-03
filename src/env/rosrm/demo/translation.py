from py4j.java_gateway import JavaGateway
from py4j.java_gateway import java_import

gateway = JavaGateway() #启动py4j服务器
entrypoint = gateway.entry_point #获取服务器桥的入口

java_import(gateway.jvm,'java.util.*') #导入java中的类的方法


class test():
    def __init__(self):
        self.Blue1 = entrypoint.getRoboMaster("Blue1")

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
        mark.color.b = 0.3
        mark.lifetime = rospy.Duration(self._control_rate, 0)
        self._vis_pub[0].publish(mark)



