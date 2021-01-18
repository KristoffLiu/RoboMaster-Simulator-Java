# RoboMaster Simulator
*基于Java liGDX的RoboMaster模拟器。*

## Get Started

## 部署开发环境

### 安装 IDE - IntellJ IDEA Community 版本

[IntelliJ IDEA: The Capable & Ergonomic Java IDE by JetBrains](https://www.jetbrains.com/idea/)

### 安装模拟器本体所需的依赖 -  JDK + JVM

[AdoptOpenJDK](https://adoptopenjdk.net/)

![](https://tcs.teambition.net/storage/3121854a587869f8b45f558873f9f1ff90e8?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9hcHBJZCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9vcmdhbml6YXRpb25JZCI6IjVmNGZhYTkzNzc3NGJiZTk2NWEzMjgxYSIsImV4cCI6MTYxMTU0NzM1OSwiaWF0IjoxNjEwOTQyNTU5LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzMxMjE4NTRhNTg3ODY5ZjhiNDVmNTU4ODczZjlmMWZmOTBlOCJ9.LZEnjbHEvgtBd5bC9mXMozy_HbY51hekaOKK5cC0-JM&download=image.png "")

安装页面会自动识别当前操作系统，**JDK** 优先选择 __OpenJDK 15__ 版本，**JVM** 优先选择 __HotSpot__ 版本。

### 安装Python依赖 -  Python3、pip、PyTorch、Gym、RLlib、Py4J

### 安装连接真机所需的依赖 -  Py4J

ROS的交互一般是用C++/Python进行的，而模拟器是用Java构建的，所以我们需要建立起Python到Java之间的桥梁，这里就要用到Py4J框架了。

需要注意的是，模拟器本身（即Java部分）已经自带Py4J框架，所以我们只需要为您的Python安装Py4J即可。

**Py4J** `pip install py4j`

### 安装强化学习所需的依赖 -  Py4J



