from py4j.java_gateway import JavaGateway

class entrypoint:
    def __init__(self):
        self.gateway = JavaGateway()

    def seed(self):
        self.env.seed()
    
    def reset():
        self.env.reset

ep = entrypoint()
ep.gateway.entry_point.setA("hello world")
print(ep.gateway.entry_point.getA())

