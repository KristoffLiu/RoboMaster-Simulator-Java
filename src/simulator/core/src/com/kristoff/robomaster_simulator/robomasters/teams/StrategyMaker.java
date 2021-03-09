package com.kristoff.robomaster_simulator.robomasters.teams;

import com.kristoff.robomaster_simulator.robomasters.robomaster.RoboMaster;
import com.kristoff.robomaster_simulator.robomasters.robomaster.tactics.SearchNode;
import com.kristoff.robomaster_simulator.utils.LoopThread;

/***
 *
 */
public class StrategyMaker extends LoopThread {
    public Team thisTeam;
    public Team enemyTeam;

    public RoboMaster blue1;
    public RoboMaster blue2;



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

        this.delta = 1f;
        this.isStep = true;
    }

    @Override
    public void start(){
        super.start();
        this.enemyTeam = RoboMasters.teamBlue;
        blue1 = RoboMasters.getRoboMaster("Blue1");
        blue2 = RoboMasters.getRoboMaster("Blue2");
        blue1.tacticMaker.start();
        blue2.tacticMaker.start();
    }

    @Override
    public void step(){
        switch (thisTeam.roboMastersLeft()){
            case 1 ->{
                switch (enemyTeam.roboMastersLeft()){
                    case 1 -> counterState = 0;
                    case 2 -> counterState = 1;
                }
            }
            case 2 ->{
                switch (enemyTeam.roboMastersLeft()){
                    case 1 -> counterState = 2;
                    case 2 -> counterState = 3;
                }
            }
            default -> {}
        }

        for(RoboMaster roboMaster : this.thisTeam){
            if(roboMaster.isAlive()) roboMaster.tacticMaker.updateCounterState(counterState);
        }

        switch (counterState){
            case 3 ->{
                RoboMaster target = RoboMasters.getRoboMaster("Red1");
                RoboMaster notTargeted = RoboMasters.getRoboMaster("Red2");
                SearchNode node1;
                SearchNode node2;
                boolean stop = false;
                for(int i = 0; i < blue1.tacticMaker.resultNodes.size(); i++){
                    for(int j = 0; j < blue2.tacticMaker.resultNodes.size(); j++){
                        SearchNode temp1 = blue1.tacticMaker.resultNodes.get(i);
                        SearchNode temp2 = blue2.tacticMaker.resultNodes.get(j);
                        float distance = temp1.position.distanceTo(temp2.position);
                        if(distance < 400 && distance > 150
                           && temp1.position.distanceTo(notTargeted.getPosition()) > 200
                           && temp2.position.distanceTo(notTargeted.getPosition()) > 200){
                            blue1.tacticMaker.setDecisionNode(temp1);
                            blue2.tacticMaker.setDecisionNode(temp2);
                            stop = true;
                            break;
                        }
                    }
                    if(stop) break;
                }
            }
        }
    }
}
