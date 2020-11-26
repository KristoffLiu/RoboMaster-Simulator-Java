//package com.robomaster_libgdx.environment.models;
//
//import com.badlogic.gdx.math.Rectangle;
//import com.engoneassessment.game.actors.CustomActor;
//import com.engoneassessment.game.utils.CollisionDetector;
//
//
//public class CharacterCollisionDetector extends CollisionDetector {
//    private final Rectangle CollisionDetectionBounds = new Rectangle();
//    private Rectangle overlappedBounds = new Rectangle();
//    final float detectingScannerLength = 10.0f;
//
//    private void generateCollisionDetectionBounds(Rectangle bounds, Character.FacingDirection facingDirection){
//        CollisionDetectionBounds.set(bounds);
//        switch (facingDirection){
//            case UP:
//                bounds.setHeight(bounds.getHeight() + detectingScannerLength);
//                break;
//            case DOWN:
//                bounds.setHeight(bounds.getHeight() + detectingScannerLength);
//                bounds.setY(bounds.getY() - detectingScannerLength);
//                break;
//            case LEFT:
//                bounds.setWidth(bounds.getWidth() + detectingScannerLength);
//                bounds.setX(bounds.getX() - detectingScannerLength);
//                break;
//            case RIGHT:
//                bounds.setWidth(bounds.getWidth() + detectingScannerLength);
//                break;
//            default:
//                break;
//        }
//    }
//
//    /**public boolean checkCollision(Rectangle bounds, Character.FacingDirection facingDirection) {
//        Array<Actor> actors = GameScreen.currentWorld.stage.getActors();
//        generateCollisionDetectionBounds(bounds, facingDirection);
//
//        for(Actor actor : actors){
//            CustomActor customActor = (CustomActor) actor;
//            Rectangle ergodicBounds = customActor.getBounds();
//            if(ergodicBounds.overlaps(CollisionDetectionBounds)){
//                overlappedBounds = ergodicBounds;
//                return true;
//            }
//        }
//        return false;*/
//
//
//    public void handleCollisionIssue(CustomActor customActor, Character.FacingDirection facingDirection){
//        Rectangle currentBounds = customActor.getBounds();
//        float delta_x = 0;
//        float delta_y = 0;
//
//        switch (facingDirection){
//            case UP:
//                delta_y = overlappedBounds.getY() - (currentBounds.getY() + currentBounds.getHeight());
//                break;
//            case DOWN:
//                delta_y = overlappedBounds.getY() + overlappedBounds.getHeight() - currentBounds.getY();
//                break;
//            case LEFT:
//                delta_x = overlappedBounds.getX() + overlappedBounds.getWidth() - currentBounds.getX();
//                break;
//            case RIGHT:
//                delta_x = overlappedBounds.getX() - (currentBounds.getX() + currentBounds.getWidth());
//                break;
//            default:
//                break;
//        }
//
//        customActor.setX(customActor.getX()+delta_x);
//        customActor.setY(customActor.getY()+delta_y);
//    }
//}
