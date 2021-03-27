package com.kristoff.robomaster_simulator.robomasters.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.kristoff.robomaster_simulator.robomasters.RoboMaster;
import com.kristoff.robomaster_simulator.systems.Systems;
import com.kristoff.robomaster_simulator.utils.Position;

/**
 * 输入事件监听器（包括触屏, 鼠标点击, 键盘按键 的输入）
 */
public class RendererInputListener extends InputListener implements ControllerListener {
    RoboMaster roboMaster;
    public RendererInputListener(RoboMaster roboMaster){
        super();
        this.roboMaster = roboMaster;
        Controllers.addListener(this);
    }

    private static final String TAG = "";

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
        preX = Gdx.input.getX();
        preY = Gdx.input.getY();
        return true;
    }

    float preX = 0f;
    float preY = 0f;
    /**
     * 手指/鼠标 按下后拖动时调用
     */
    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        Position current = this.roboMaster.getPosition();
        Position position = new Position(current.x, current.y);
        float currentX = Gdx.input.getX();
        float currentY = Gdx.input.getY();
        float offsetX = currentX - preX;
        float offsetY = currentY - preY;
        position.x += offsetX * 7;
        position.y -= offsetY * 7;
        preX = currentX;
        preY = currentY;
        if(position.isInsideTheMap()) this.roboMaster.setPosition(position.x , position.y);
    }

    /**
     * 手指/鼠标 抬起时调用
     */
    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
    }

    @Override
    public void connected(Controller controller) {

    }

    @Override
    public void disconnected(Controller controller) {

    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
//        if(buttonCode == XBox360Pad.BUTTON_Y)
//            sprite.setY(sprite.getY() + 1);
//        if(buttonCode == XBox360Pad.BUTTON_A)
//            sprite.setY(sprite.getY()-1);
//        if(buttonCode == XBox360Pad.BUTTON_X)
//            sprite.setX(sprite.getX() - 1);
//        if(buttonCode == XBox360Pad.BUTTON_B)
//            sprite.setX(sprite.getX() + 1);
//        if(buttonCode == XBox360Pad.BUTTON_LB)
//            sprite.scale(-0.1f);
//        if(buttonCode == XBox360Pad.BUTTON_RB)
//            sprite.scale(0.1f);
        return false;
    }
    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        return false;
    }
    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        // This is your analog stick
        // Value will be from -1 to 1 depending how far left/right, up/down the stick is
        // For the Y translation, I use a negative because I like inverted analog stick
        // Like all normal people do! 😉
        // Left Stick
        Position current = this.roboMaster.getPosition();
        Position position = new Position(current.x, current.y);
        int xOffset = 0;
        int yOffset = 0;
        if(axisCode == XBox360Pad.AXIS_RIGHT_X) {
            xOffset += value * 10;
            System.out.println("x" + xOffset);
        }
        if(axisCode == XBox360Pad.AXIS_LEFT_Y)
            yOffset += value * 10;
        System.out.println("y" + yOffset);
        this.roboMaster.setPosition(position.x + xOffset, position.y + yOffset);
        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        // This is the dpad
//        if(value == XBox360Pad.BUTTON_DPAD_LEFT)
//            sprite.translateX(-10f);
//        if(value == XBox360Pad.BUTTON_DPAD_RIGHT)
//            sprite.translateX(10f);
//        if(value == XBox360Pad.BUTTON_DPAD_UP)
//            sprite.translateY(10f);
//        if(value == XBox360Pad.BUTTON_DPAD_DOWN)
//            sprite.translateY(-10f);
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }
}
