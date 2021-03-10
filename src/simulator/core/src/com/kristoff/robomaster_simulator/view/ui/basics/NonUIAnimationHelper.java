package com.kristoff.robomaster_simulator.view.ui.basics;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.utils.Null;

/***
 * animation helper for actor objects which are not extended as ui element.
 */
public class NonUIAnimationHelper {

    Actor actor;
    float animationOriginX = 0f;
    float animationOriginY = 0f;

    /***
     * constructor
     */
    public NonUIAnimationHelper(Actor actor){
        this.actor = actor;
    }

    /***
     * set the origin of the animation
     */
    public void setAnimationOrigin(float x, float y){
        animationOriginX = x;
        animationOriginY = y;
    }

    /***
     * hide
     */
    public void hide(){
        AlphaAction uiElementAlphaAction = Actions.alpha(0f,0f);
        VisibleAction uiElementVisibleAction = Actions.visible(false);
        actor.addAction(uiElementAlphaAction);
        actor.addAction(uiElementVisibleAction);
    }

    /***
     * hide in period of time
     * @param duration the time of duration
     */
    public void hide(float duration){
        AlphaAction uiElementAlphaAction = Actions.alpha(0,duration);
        VisibleAction uiElementVisibleAction = Actions.visible(false);
        DelayAction uiElementDelayAction = Actions.delay(duration,uiElementVisibleAction);
        actor.addAction(uiElementAlphaAction);
        actor.addAction(uiElementDelayAction);
    }

    /***
     * appear
     */
    public void appear(){
        VisibleAction uiElementVisibleAction = Actions.visible(true);
        AlphaAction uiElementAlphaAction = Actions.alpha(1f,0f);
        actor.addAction(uiElementVisibleAction);
        actor.addAction(uiElementAlphaAction);
    }

    /***
     * appear in duration of time.
     * @param duration the time of duration
     */
    public void appear(float duration){
        VisibleAction uiElementVisibleAction = Actions.visible(true);
        AlphaAction uiElementAlphaAction = Actions.alpha(1f,duration);
        actor.addAction(uiElementVisibleAction);
        actor.addAction(uiElementAlphaAction);
    }

    /***
     * fade out
     * @param duration the time of duration
     */
    public void fadeOut(float duration){
        fadeOut(0f, 0f, duration, null);
    }

    /***
     * fade out
     * @param offset the value of offset.
     * @param isHorizontalShift the time of duration.
     * @param duration the duration of time.
     */
    public void fadeOut(float offset, boolean isHorizontalShift, float duration){
        if(isHorizontalShift) fadeOut(offset, 0f, duration, null); else fadeOut(0f, offset, duration, null);
    }

    /***
     * fade out
     * @param duration the time of duration
     */
    public void fadeOut(float offsetX, float offsetY, float duration){
        fadeOut(offsetX, offsetY, duration, null);
    }

    /***
     * fade out
     */
    public void fadeOut(float offsetX, float offsetY, float duration, @Null Interpolation interpolation){
        fadeOut(animationOriginX, animationOriginY, offsetX, offsetY, duration, interpolation);
    }

    /***
     * fade out
     */
    public void fadeOut(float x, float y, float offsetX, float offsetY, float duration, @Null Interpolation interpolation){
        if(actor.isVisible()){
            MoveToAction uiElementMoveToAction = Actions.moveTo(x,y,0f);
            AlphaAction uiElementAlphaAction = Actions.alpha(0f, duration, interpolation);
            MoveByAction uiElementMoveByAction = Actions.moveBy(offsetX, offsetY, duration, interpolation);
            ParallelAction parallelAction = Actions.parallel(uiElementAlphaAction, uiElementMoveByAction);
            SequenceAction sequenceAction = Actions.sequence(uiElementMoveToAction,parallelAction);
            actor.addAction(sequenceAction);
        }
    }

    /***
     * fade in
     */
    public void fadeIn(float duration){
        fadeIn(0f, 0f, duration, null);
    }

    /***
     * fade in
     */
    public void fadeIn(float value, boolean isHorizontalShift, float duration){
        if(isHorizontalShift) fadeIn(value, 0f, duration, null); else fadeIn(0f, value, duration, null);

    }

    /***
     * fade in
     */
    public void fadeIn(float offsetX, float offsetY, float duration){
        fadeIn(offsetX, offsetY, duration, null);
    }

    /***
     * fade in
     */
    public void fadeIn(float offsetX, float offsetY, float duration, @Null Interpolation interpolation){
        fadeIn(animationOriginX, animationOriginY, offsetX, offsetY, duration, interpolation);
    }

    /***
     * fade in
     */
    public void fadeIn(float x, float y, float offsetX, float offsetY, float duration, @Null Interpolation interpolation){
        if(actor.isVisible()){
            MoveToAction uiElementMoveToAction = Actions.moveTo(x - offsetX,y - offsetY,0f);
            AlphaAction uiElementAlphaAction = Actions.alpha(1f, duration, interpolation);
            MoveByAction uiElementMoveByAction = Actions.moveBy(offsetX, offsetY, duration, interpolation);
            ParallelAction parallelAction = Actions.parallel(uiElementAlphaAction, uiElementMoveByAction);
            SequenceAction sequenceAction = Actions.sequence(uiElementMoveToAction,parallelAction);
            actor.addAction(sequenceAction);
        }
    }
}
