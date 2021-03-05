package com.kristoff.robomaster_simulator.robomasters.teams;

import com.kristoff.robomaster_simulator.robomasters.robomaster.RoboMaster;
import com.kristoff.robomaster_simulator.utils.LoopThread;

/***
 *
 */
public class StrategyMaker extends LoopThread {
    public Team thisTeam;
    public Team enemyTeam;

    /***
     * -1: Not Working
     * 0: 1 v 1 state
     * 1: 1 v 2 state
     * 2: 2 v 1 state
     * 3: 2 v 2 state
     */
    public int counterState = -1;

    /***
     * -1: No decision making
     * 0: countering 对抗模式
     * 1: wandering 逃跑模式
     */
    public int decisionType = -1;

    public StrategyMaker(Team team){
        this.thisTeam = team;
        this.enemyTeam = team == RoboMasters.teamBlue ? RoboMasters.teamRed : RoboMasters.teamBlue;
        this.delta = 1f;
        this.isStep = true;
    }

    @Override
    public void start(){
        super.start();
    }

    @Override
    public void step(){
//        switch (thisTeam.roboMastersLeft()){
//            case 1 ->{
//                switch (enemyTeam.roboMastersLeft()){
//                    case 1 -> counterState = 0;
//                    case 2 -> counterState = 1;
//                }
//            }
//            case 2 ->{
//                switch (enemyTeam.roboMastersLeft()){
//                    case 1 -> counterState = 2;
//                    case 2 -> counterState = 3;
//                }
//            }
//            default -> {}
//        }
        counterState = 3;

        for(RoboMaster roboMaster : this.thisTeam){
            if(roboMaster.isAlive()) roboMaster.tacticMaker.makeDecision(this.counterState);
        }
    }
}
