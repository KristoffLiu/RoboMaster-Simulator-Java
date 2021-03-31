# Get Started for RoboMaster Py4J Bridge

## Dependencies

```python
$ from py4j.java_gateway import JavaGateway
$ from py4j.java_gateway import java_import
```
## EntryPoint Class

EntryPoint class is the entrance to control and get information from the simulator by using python codes. It is equivalent to the RosRMLauncher class in the java simulator.

### Accessibility

We can get the entrypoint object directly from the gateway

```python
$ gateway = JavaGateway()          
$ entrypoint = gateway.entry_point 
```

### Method

- def getRoboMaster(name):
  - parameter -> name : str -> the name of the RoboMaster
  - return : RoboMaster()  -> the matched RoboMaster object

- def getMap():
  - return -> the two dimension list

## RoboMaster Class
this is the most important class, we can input the information and get analysis through it.

### Accessibility
We can get the RoboMaster object directly from the entrypoint

```python
blue1 = entrypoint.getRoboMaster("Blue1")
```
### Method - Health

- def getHealth():
  - return : int  -> current health value of the RoboMaster

- def setHealth(value):
  - parameter -> value : int -> new health value

- def loseHealth(value):
  - parameter -> value : int -> the value that the current health will decrease

- def getHealthPercent():
  - return : int  -> current health value of the RoboMaster

### Method - Location

- def getPosition()
  - return : Position ->

### Method - Strategy & Decision

- def getDecisionMade()
  - return : Position ->

## Position Class
- def getX()
  - return : int -> the x coordination of the position object.
  - 
- def getY()
  - return : int -> the y coordination of the position object.

- def distanceTo(x, y)
  - parameter
    - x : int -> the x coordination of another point.
    - y : int -> the y coordination of another point.
  - return : float -> the distance from the point to another point.

- def distanceTo(position)
  - parameter
    - position : Position -> the position of another point
  - return : float -> the distance from the point to another point.