from py4j.java_gateway import JavaGateway

gateway = JavaGateway()
env = gateway.entry_point.getEnv()