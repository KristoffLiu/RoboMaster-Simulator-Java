package com.kristoff.robomaster_simulator.view.ui.basics;

/***
 * Margin
 */
public class Margin extends Thickness{

    public Margin(float uniformLength) {
        super(uniformLength);
    }

    public Margin(float left, float right, float top, float bottom) {
        super(left, right, top, bottom);
    }
}
