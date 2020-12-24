package com.kristoff.robomaster_simulator.robomasters.strategies;

import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.utils.Position;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

/***
 * OneVsTwoCircumventionPathPlanningNode
 * 1vs2战术规避路线规划节点
 * Based on Node of a Breadth-First Search 基于广度优先算法
 */
public class OneVsTwoCircumventionPathPlanningNode {
    public Position position; //此节点的位置
    public int index; //此节点的层级索引
    public double cost; //此节点的成本消耗
    public OneVsTwoCircumventionPathPlanningNode parentNode; //此节点的父节点
    public Queue<OneVsTwoCircumventionPathPlanningNode> childrenNodes; //此节点的子节点队列
    public static int[][] childrenNodesFindingCost =
            new int[][]
                    {
                            { 1, 0, 1},{0, 1, 1},{-1, 0, 1},
                            { 0, -1,1},{-1,-1,2},
                            {-1, 1, 2},{1,-1, 2},{1, 1, 2}
                    }; //此节点的子节点查找路径及成本消耗

    //构造器
    public OneVsTwoCircumventionPathPlanningNode(int x, int y, int index, double cost, OneVsTwoCircumventionPathPlanningNode parentNode){
        this.position = new Position(x,y);
        this.index = index;
        this.cost = cost;
        this.parentNode = parentNode;
    }

    //查找并生成子节点，并返回队列对象
    public Queue<OneVsTwoCircumventionPathPlanningNode> generateChildrenNodes(int[][] matrix, boolean[][] nodeGrid, RoboMaster roboMaster){
        childrenNodes = new LinkedList<>();
        nodeGrid[this.position.x][this.position.y] = true;
        for(int i=0; i < childrenNodesFindingCost.length; i++){
            int x = this.position.x + childrenNodesFindingCost[i][0] ;
            int y = this.position.y + childrenNodesFindingCost[i][1] ;
            double cost = Math.sqrt(childrenNodesFindingCost[i][2]);
            if(hasThisNodeNotBeenVisited(x, y, nodeGrid) && (!Systems.matrixSimulator.isPointNotEmptyLowResolution(x,y,roboMaster.pointStatus))){
                childrenNodes.add(new OneVsTwoCircumventionPathPlanningNode(x,y,this.index + 1, cost,this));
            }
        }
        return childrenNodes;
    }

    //检查节点可访问性
    public static boolean hasThisNodeNotBeenVisited(int x, int y, boolean[][] nodeGrid){
        if(x>=0 && x<849 && y>=0 && y<489){
            if(nodeGrid[x][y]){
                return false;
            }
            else {
                nodeGrid[x][y] = true;
                return true;
            }
        }
        else {
            return true;
        }
    }
}
