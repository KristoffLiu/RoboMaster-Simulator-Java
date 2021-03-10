package com.kristoff.robomaster_simulator.view.ui.basics;

/***
 * Padding
 */
public class Padding extends Thickness{

    public Padding(float uniformLength) {
        super(uniformLength);
    }

    public Padding(float left, float right, float top, float bottom) {
        super(left, right, top, bottom);
    }
}
