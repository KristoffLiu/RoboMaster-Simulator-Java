package com.kristoff.robomaster_simulator.systems.robomasters;

import com.badlogic.gdx.utils.Array;
import com.kristoff.robomaster_simulator.systems.matrixsimulation.RoboMasterPoint;
import com.kristoff.robomaster_simulator.systems.robomasters.modules.LidarObservation;
import com.kristoff.robomaster_simulator.systems.robomasters.modules.enemyobservations.EnemiesObservationSimulator;
import com.kristoff.robomaster_simulator.systems.robomasters.types.AlexanderMasterII;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RoboMasters{
    public static LidarObservation.LidarMode lidarMode = LidarObservation.LidarMode.list;

    public static RoboMasterList  all       = new RoboMasterList();
    public static RoboMasterList teamBlue   = new RoboMasterList();
    public static RoboMasterList teamRed    = new RoboMasterList();

    public RoboMasters(){
        init();
    }

    public static void init(){
        if(all.size() == 0){
            new AlexanderMasterII(teamBlue,"Blue1");
            new AlexanderMasterII(teamBlue,"Blue2");
            new AlexanderMasterII(teamRed,"Red1");
            new AlexanderMasterII(teamRed,"Red2");

            all.addAll(teamBlue);
            all.addAll(teamRed);
        }
    }

    public void start(){
        this.stepObservation();
        teamBlue.get(0).enemiesObservationSimulator.start();
    }

    public Array<RoboMasterPoint> getCurrentPoints(){
        Lock lock = new ReentrantLock();
        if(lock.tryLock()) {
            try{
                synchronized (all){
                    Array<RoboMasterPoint> currentPoints = new Array<>();
                    all.forEach(x->currentPoints.addAll(x.matrix.current));
                    return currentPoints;
                }
            }catch(Exception ex){

            }finally{
                lock.unlock();   //释放锁
            }
        }else {
            return new Array<>();
        }
        return new Array<>();
    }

    public Array<RoboMasterPoint> getPreviousPoints(){
        Lock lock = new ReentrantLock();
        if(lock.tryLock()) {
            try{
                synchronized (all){
                    Array<RoboMasterPoint> previousPoints = new Array<>();
                    all.forEach(x->previousPoints.addAll(x.matrix.previous));
                    return previousPoints;
                }
            }catch(Exception ex){

            }finally{
                lock.unlock();   //释放锁
            }
        }else {
            return new Array<>();
        }
        return new Array<>();
    }

    public void stepMatrix(){
        Lock lock = new ReentrantLock();
        if(lock.tryLock()) {
            try{
                synchronized (all){
                    all.forEach(x->x.matrix.step());
                }
            }catch(Exception ex){

            }finally{
                lock.unlock();   //释放锁
            }
        }else {
            //如果不能获取锁，则直接做其他事情
        }
    }

    public void stepObservation(){
        for(RoboMaster roboMaster : all){
            roboMaster.lidarObservation.step();
        }
        //teamBlue.get(0).observation.step();
    }

    public void stepEnemyObservationSimulation(){
        for(RoboMaster roboMaster : all){
            roboMaster.lidarObservation.step();
        }
        //teamBlue.get(0).observation.step();
    }
}
