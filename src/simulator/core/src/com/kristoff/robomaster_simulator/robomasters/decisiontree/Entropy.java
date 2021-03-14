package com.kristoff.robomaster_simulator.robomasters.decisiontree;

public class Entropy {
    //信息熵
    public static double getEntropy(int x, int total)
    {
        if (x == 0) return 0;
        double x_pi = getShang(x,total);
        return -(x_pi*Logs(x_pi));
    }

    public static double Logs(double y)
    {
        return Math.log(y) / Math.log(2);
    }


    public static double getShang(int x, int total)
    {
        return x * Double.parseDouble("1.0") / total;
    }
}
