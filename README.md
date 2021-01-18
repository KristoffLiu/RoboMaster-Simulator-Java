# RoboMaster Simulator
*基于Java liGDX的RoboMaster模拟器。*

## Get Started

### 1. 安装IDE

#### 1.1 Java - 安装 IntellJ IDEA Community 版本

[IntelliJ IDEA: The Capable & Ergonomic Java IDE by JetBrains](https://www.jetbrains.com/idea/)

#### 1.2 Python - 任意好用的Python IDE即可

### 2. 安装模拟器本体所需的依赖 -  JDK + JVM

[AdoptOpenJDK](https://adoptopenjdk.net/)

![](https://tcs.teambition.net/storage/3121854a587869f8b45f558873f9f1ff90e8?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9hcHBJZCI6IjU5Mzc3MGZmODM5NjMyMDAyZTAzNThmMSIsIl9vcmdhbml6YXRpb25JZCI6IjVmNGZhYTkzNzc3NGJiZTk2NWEzMjgxYSIsImV4cCI6MTYxMTU0NzM1OSwiaWF0IjoxNjEwOTQyNTU5LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzMxMjE4NTRhNTg3ODY5ZjhiNDVmNTU4ODczZjlmMWZmOTBlOCJ9.LZEnjbHEvgtBd5bC9mXMozy_HbY51hekaOKK5cC0-JM&download=image.png "")

安装页面会自动识别当前操作系统，**JDK** 优先选择 __OpenJDK 15__ 版本，**JVM** 优先选择 __HotSpot__ 版本。

### 3. 安装Python依赖 -  Python3、pip、PyTorch、Gym、RLlib、Py4J

#### 3.1 Python 3

```text
$ sudo apt-get install python3 
```

#### 3.2 pip

```text
$ sudo pip3 install torch
```

#### 3.3 RLlib - 强化学习所需依赖

```text
$ sudo pip3 install ray[rllib]
```


#### 3.4 Py4J - 连接真机所需的依赖
ROS的交互一般是用C++/Python进行的，而模拟器是用Java构建的，所以我们需要建立起Python到Java之间的桥梁，这里就要用到Py4J框架了。

需要注意的是，模拟器本身（即Java部分）已经自带Py4J框架，所以我们只需要为您的Python安装Py4J即可。

```text
$ sudo pip3 install py4j
```

### 4.克隆代码仓库

#### 4.1 克隆并部署本仓库到本地即可

```text
$ git clone https://github.com/KristoffLiu/RoboMaster-Simulator-Java.git
```

## Usage
### 1. 打开项目
1. 打开IDEA的项目选择页面
    · 如果在开始的小窗口界面，则 选择【Open or Import/打开或导入】 → 打开【Open File or Project】窗口
    · 如果已经在项目编辑页面，则 打开左上角的【File/菜单】 → 选择【Open or Import/打开或导入】 → 打开【Open File or Project】窗口
2. 找到部署到本地的项目仓库路径
3. 选择相对路径中的../src/simulator/build.gradle文件，点击OK确认
4. 在弹出的页面中，点击Open As Project
5. 现在已经成功打开项目了。
6. 第一次成功打开项目以后可能需要稍等一会儿，因为此时gradle包正在自动下载部署所需依赖包（如果下载失败的话，需要挂个代理，不过上科大的网络应该没有问题）

### 2. 运行项目
1. 右键任意用来做启动入口的java文件（e.g. RosRMLauncher），选择"Run blablabla"，即可自动配置启动项，并启动程序。

