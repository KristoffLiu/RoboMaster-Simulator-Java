package com.kristoff.robomaster_simulator.robomasters.Strategy;

import com.kristoff.robomaster_simulator.utils.Position;

import java.util.LinkedList;
import java.util.Queue;

/***
 * OneVsTwoCircumventionPathPlanningNode
 * 1vs2战术规避路线规划节点
 * Based on Node of a Breadth-First Search 基于广度优先算法
 */
public class SearchNode {
    public Position position;                           //此节点的位置
    public int index;                                   //此节点的层级索引
    public double cost;                                 //此节点的成本消耗
    public SearchNode parentNode;             //此节点的父节点
    public Queue<SearchNode> childrenNodes;   //此节点的子节点队列

    public SearchNode(){
        this.position = new Position(0, 0);
        cost = -999;
    }

    public SearchNode(int x, int y){
        this.position = new Position(x, y);
    }

    //构造器
    public SearchNode(int x, int y, int index, double cost, SearchNode parentNode){
        this.position = new Position(x,y);
        this.index = index;
        this.cost = cost;
        this.parentNode = parentNode;
        this.childrenNodes = new LinkedList<>();
    }

    public int getX(){
        return this.position.x;
    }

    public int getY(){
        return this.position.y;
    }

    //此节点的子节点查找路径及成本消耗
    public static int[][] childrenNodesFindingCost =
            new int[][]
                    {
                            { 1, 0, 1},{0, 1, 1},{-1, 0, 1},
                            { 0, -1, 1},          {-1, -1, 2},
                            {-1, 1, 2},{1, -1, 2},{1, 1, 2}
                    };
}
