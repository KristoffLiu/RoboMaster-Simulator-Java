package com.kristoff.robomaster_simulator.robomasters.decisiontree;

import java.util.Comparator;

public class Comparisons implements Comparator {
    public int compare(Object a, Object b) throws ClassCastException{
        String str1 = (String)a;
        String str2 = (String)b;
        return str1.compareTo(str2);
    }
}
