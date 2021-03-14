package com.kristoff.robomaster_simulator.view.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.kristoff.robomaster_simulator.teams.RoboMasters;
import com.kristoff.robomaster_simulator.view.renderers.EnvRenderer;
import com.kristoff.robomaster_simulator.view.View;

/**
 * 输入事件监听器（包括触屏, 鼠标点击, 键盘按键 的输入）
 */
public class GlobalInputEventHandler extends InputListener {
    float MouseMovingVelocityFactor = 20;
    EnvRenderer envRenderer;
    View view;
    final String TAG = "GlobalInputEventHandler";

    boolean isLeftControlKeyDown = false;
    boolean isLeftAltKeyDown = false;

    public GlobalInputEventHandler(EnvRenderer envRenderer){
        this.envRenderer = envRenderer;
        this.view = envRenderer.view;
    }


    /**
     * 当有键盘按键被按下时调用, 参数 keycode 是被按下的按键的键值,
     * 所有键盘按键的键值常量定义在 com.badlogic.gdx.Input.Keys 类中
     */
    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Input.Keys.CONTROL_LEFT:
                isLeftControlKeyDown = true;
                break;
            case Input.Keys.ALT_LEFT:
                isLeftAltKeyDown = true;
                break;
            case Input.Keys.Z:
                RoboMasters.teamBlue.get(0).RMPhysicalSimulation.cannonRotateCCW();
                break;
            case Input.Keys.C:
                RoboMasters.teamBlue.get(0).RMPhysicalSimulation.cannonRotateCW();
                break;

            case Input.Keys.H:
                RoboMasters.setPosition("Blue1",2000,2000,0f);
                break;

            case Input.Keys.Q:
                RoboMasters.teamBlue.get(0).dynamics.rotateCCW();
                break;
            case Input.Keys.E:
                RoboMasters.teamBlue.get(0).dynamics.rotateCW();
                break;
            case Input.Keys.W:
                RoboMasters.teamBlue.get(0).dynamics.moveForward();
                break;
            case Input.Keys.A:
                RoboMasters.teamBlue.get(0).dynamics.moveLeft();
                break;
            case Input.Keys.D:
                RoboMasters.teamBlue.get(0).dynamics.moveRight();
                break;
            case Input.Keys.S:
                RoboMasters.teamBlue.get(0).dynamics.moveBehind();
                break;
            case Input.Keys.L:
                RoboMasters.teamBlue.get(0).dynamics.slowDown();
                break;
            case Input.Keys.J:
                RoboMasters.teamBlue.get(0).weapon.shoot();
                break;
        }
        return false;
    }


    /** Called when a key goes up. When true is returned, the event is {@link Event#handle() handled}. */
    public boolean keyUp (InputEvent event, int keycode) {
        switch (keycode) {
            case Input.Keys.CONTROL_LEFT:
                isLeftControlKeyDown = false;
                break;
            case Input.Keys.ALT_LEFT:
                isLeftAltKeyDown = false;
                break;

            case Input.Keys.Q:
            case Input.Keys.E:
                RoboMasters.teamBlue.get(0).dynamics.stopRotating();
                break;
            case Input.Keys.W:
            case Input.Keys.A:
            case Input.Keys.D:
            case Input.Keys.S:
                RoboMasters.teamBlue.get(0).dynamics.stopMoving();
                break;
        }
        return true;
    }


    /**
     * 手指/鼠标 按下时调用
     *
     * @param x
     *      按下时的 X 轴坐标, 相对于被触摸对象（监听器注册者）的左下角
     *
     * @param y
     * 		按下时的 Y 轴坐标, 相对于被触摸对象（监听器注册者）的左下角
     *
     * @param pointer
     *      按下手指的ID, 用于多点触控时辨别按下的是第几个手指,
     *      一般情况下第一只手指按下时 pointer 为 0, 手指未抬起前又有一只手指按下, 则后按下的手指 pointer 为 1。
     *      同一只手指的 按下（touchDown）, 拖动（touchDragged）, 抬起（touchUp）属于同一次序列动作（pointer 值相同）,
     *      pointer 的值在 按下 时被确定, 之后这只手指产生的的 拖动 和 抬起 动作将会把该已确定的 pointer 值传递给其事件方法
     *      touchDragged() 和 touchUp() 方法。
     *
     * @return
     *      返回值为 boolean 类型, 用于告诉上一级当前对象（演员/舞台）是否需要处理该次事件。 <br/><br/>
     *
     *      返回 true: 表示当前对象需要处理该次事件, 则之后这只手指产生的 拖动（touchDragged）和 抬起（touchUp）事件
     *          也会传递到当前对象。<br/><br/>
     *
     *      返回 false: 表示当前对象不处理该次事件, 既然不处理, 则之后这只手指产生的 拖动（touchDragged）和 抬起（touchUp）事件
     *          也将不会再传到到当前对象。<br/><br/>
     *
     *      PS: 当前对象是否处理一只手指的触摸事件（按下, 拖动, 抬起）只在 按下时（touchDown）确定,
     *          所以之后的 touchDragged() 和 touchUp() 方法中就不再判断, 因此返回类型为 void。
     */
    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        //Gdx.app.log(TAG, "touchDown: " + x + ", " + y + "; pointer: " + pointer);
        return true;
    }

    /**
     * 手指/鼠标 按下后拖动时调用
     */
    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        //Gdx.app.log(TAG, "touchDragged: " + x + ", " + y + "; pointer: " + pointer);
    }

    /**
     * 手指/鼠标 抬起时调用
     */
    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        //Gdx.app.log(TAG, "touchUp: " + x + ", " + y + "; pointer: " + pointer);
    }

    @Override
    /** Called when the mouse wheel has been scrolled. When true is returned, the event is {@link Event#handle() handled}. */
    public boolean scrolled (InputEvent event, float x, float y, float amountX, float amountY) {
        Gdx.app.log(TAG, String.valueOf(amountX));
        if(isLeftControlKeyDown){
            view.setZoom(amountY);
        }
        else if(isLeftAltKeyDown){
            if(amountY > 0){
                view.setHorizontalTranslation(true);
            }
            else{
                view.setHorizontalTranslation(false);
            }
        }
        else{
            if(amountY != 0){
                if(amountY > 0){
                    view.setVerticalTranslation(false);
                }
                else{
                    view.setVerticalTranslation(true);
                }
            }
        }
        return true;
    }
}
