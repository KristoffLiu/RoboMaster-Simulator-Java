package com.kristoff.robomaster_simulator.robomasters.robomaster.tactics;

import com.kristoff.robomaster_simulator.utils.Position;

import java.util.LinkedList;
import java.util.Queue;

/***
 * OneVsTwoCircumventionPathPlanningNode
 * 1vs2战术规避路线规划节点
 * Based on Node of a Breadth-First Search 基于广度优先算法
 */
public class OneVSTwoPPTacticNode {
    public Position position;                       //此节点的位置
    public int index;                               //此节点的层级索引
    public double cost;                             //此节点的成本消耗
    public OneVSTwoPPTacticNode parentNode;             //此节点的父节点
    public Queue<OneVSTwoPPTacticNode> childrenNodes;   //此节点的子节点队列


    //构造器
    public OneVSTwoPPTacticNode(int x, int y, int index, double cost, OneVSTwoPPTacticNode parentNode){
        this.position = new Position(x,y);
        this.index = index;
        this.cost = cost;
        this.parentNode = parentNode;
        this.childrenNodes = new LinkedList<>();
    }
}
