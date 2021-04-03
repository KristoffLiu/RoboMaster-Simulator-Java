from py4j.java_gateway import JavaGateway
from py4j.java_gateway import java_import

gateway = JavaGateway() #启动py4j服务器
entrypoint = gateway.entry_point #获取服务器桥的入口

java_import(gateway.jvm,'java.util.*') #导入java中的类的方法

entrypoint.buffZoneDemoTest()
entrypoint.setAsRoamer("blue2")
entrypoint.updateBuffZone(0, 1, True)
entrypoint.updateBuffZone(2, 4, True)
# entrypoint.setAsRoamer("blue1")
entrypoint.updateBuffZone(3, 2, True)
# entrypoint.updateBuffZone(5, 3, True)