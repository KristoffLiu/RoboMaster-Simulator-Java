package com.kristoff.robomaster_simulator.robomasters.decisiontree;


public class DecisionNode {
    DecisionNode parent;
    String parentAttribute;
    String nodeName;
    String[] attributes;
    DecisionNode[] childNodes;
}
