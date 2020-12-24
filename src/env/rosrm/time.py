import time
#计算时间消耗，特别是对程序运行时间消耗


for i in range(8490):
    for j in range(4890):
        
        pass
    pass
start = time.time()

for _ in range(100000000):
    pass
end = time.time()
print("循环运行时间:%.2f秒"%(end-start))
#output:循环运行时间:5.50秒