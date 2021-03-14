package com.kristoff.robomaster_simulator.robomasters.judgement.BuffZone;

public class BuffZone {
//    private Vector2 leftUp;
//    private Vector2 leftDown;
//    private Vector2 rightUp;
//    private Vector2 rightDown;

    //F1~F6, 编号为 F1、F2、F3 的三个加成/惩罚区和编号为 F4、F5、F6 的三个加成/惩罚区互为中心对称。
    private int ZoneID;
    private String BuffCase;

//    public boolean IsPointInZone(Vector2 testingPoint){
//        return(testingPoint.x >= leftUp.x | testingPoint.x <= rightUp.x | testingPoint.y >= leftDown.y | testingPoint.y <= leftUp.y);
//    }

    public void setBuffCase(String name){
        BuffCase = name;
    }

//    public void setFourPoints(Vector2 theLeftUp, Vector2 theLeftDown, Vector2 theRightUp, Vector2 theRightDown){
//        leftUp = theLeftUp;
//        leftDown = theLeftDown;
//        rightUp = theRightUp;
//        rightDown = theRightDown;
//    }

    public void setZoneID(int ID){
        ZoneID = ID;
    }

    public int getZoneID(){
        return ZoneID;
    }





}
