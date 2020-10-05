package nl.han.ica.game;

import nl.han.ica.game.objects.Coin;
import nl.han.ica.game.objects.IFlyingObject;
import nl.han.ica.oopg.collision.CollidedTile;
import nl.han.ica.oopg.collision.CollisionSide;
import nl.han.ica.oopg.collision.ICollidableWithGameObjects;
import nl.han.ica.oopg.collision.ICollidableWithTiles;
import nl.han.ica.oopg.exceptions.TileNotFoundException;
import nl.han.ica.oopg.objects.GameObject;
import nl.han.ica.oopg.objects.Sprite;
import nl.han.ica.oopg.objects.SpriteObject;
import processing.core.PVector;

import java.util.List;

public class Player extends SpriteObject implements ICollidableWithTiles, ICollidableWithGameObjects {
    private int size = 100;
    private final Main world;
    int rightSpeed = 0;
    int leftSpeed = 0;
    int upSpeed = 0;
    int downSpeed = 0;
    private final int speed = 15;
    private final int gravity = 10;
    int lives = 3;

    /**
     * Constructor
     *
     * @param world Referentie naar de wereld
     */
    public Player(Main world, Sprite sprite, int size) {
        super(sprite);
        this.world = world;
        this.size = size;
//        setCurrentFrameIndex(1);
        setFriction(0.5f);
        this.setGravity(gravity);
    }

    @Override
    public void update() {
        float customXSpeed = 0;
        float customYSpeed = 0;
        customXSpeed = (rightSpeed == 0 && leftSpeed == 0) ? 0 : rightSpeed - leftSpeed;
        customYSpeed = (downSpeed == 0 && upSpeed == 0) ? 0 : downSpeed - upSpeed;
        setxSpeed(customXSpeed);
        setySpeed(customYSpeed);

        if (getX() < 0) {//links
            setxSpeed(0);
            setX(0);
        }
        if (getY() < 0) {//boven
//            setySpeed(0);
//            setY(0);
            world.getLevel().reload();
        }
        if (getX() > world.getView().getWorldWidth() - size) {//rechts
            setxSpeed(0);
            setX(world.getView().getWorldWidth() - size);
        }
        if (getY() > world.getView().getWorldHeight() - size) {//onder
//            setySpeed(0);
//            setY(world.getView().getWorldHeight() - size);
            world.getLevel().reload();
        }
    }

    @Override
    public void keyPressed(int keyCode, char key) {
        if (keyCode == world.LEFT) {
            leftSpeed = speed;
//            setCurrentFrameIndex(0);
        }
        if (keyCode == world.UP || key == ' ' || keyCode == world.DOWN) {
        this.setGravity((this.getGravity() == -gravity) ? gravity : -gravity);
//        if (keyCode == world.UP) {
//            upSpeed = speed;
//            setDirectionSpeed(0, speed*10);
        }
        if (keyCode == world.RIGHT) {
            rightSpeed = speed;
//            setCurrentFrameIndex(1);
        }
        if (keyCode == world.DOWN) {
//            downSpeed = speed;
            //
        }
    }

    @Override
    public void keyReleased(int keyCode, char key) {
        if (keyCode == world.LEFT) {
            leftSpeed = 0;
        }
        if(keyCode == world.RIGHT){
            rightSpeed = 0;
        }
        if (keyCode == world.UP) {
            //customYSpeed = 0;
//            upSpeed = 0;
        }
        if(keyCode == world.DOWN){
            //
//            downSpeed = 0;
        }
    }

    /**
     * De speler heeft pijn :(
     */
    public void takeHit(){
        lives--;
        if(lives==0){
            world.getLevel().reload();
        }
    }

    @Override
    public void tileCollisionOccurred(List<CollidedTile> collidedTiles) {
        PVector vector;
        int tileSize = world.tileSize;

        for (CollidedTile ct : collidedTiles) {
            if (ct.getTile() instanceof BoardTile) {
                if (CollisionSide.TOP.equals(ct.getCollisionSide())) {
                    try {
                        vector = world.getTileMap().getTilePixelLocation(ct.getTile());
                        setY(vector.y - getHeight());
                    } catch (TileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                if (CollisionSide.BOTTOM.equals(ct.getCollisionSide())) {
                    try {
                        vector = world.getTileMap().getTilePixelLocation(ct.getTile());
                        setY(vector.y + getHeight());
                    } catch (TileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                if (CollisionSide.RIGHT.equals(ct.getCollisionSide())) {
                    try {
                        vector = world.getTileMap().getTilePixelLocation(ct.getTile());
                        setX(vector.x + getWidth());
//                        world.getTileMap().setTile((int) vector.x / tileSize, (int) vector.y / tileSize, -1);
                    } catch (TileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                if (CollisionSide.LEFT.equals(ct.getCollisionSide())) {
                    try {
                        vector = world.getTileMap().getTilePixelLocation(ct.getTile());
                        setX(vector.x - getWidth());
//                        world.getTileMap().setTile((int) vector.x / tileSize, (int) vector.y / tileSize, -1);
                    } catch (TileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
//            else if(ct.getTile() instanceof CoinTile){
//                vector = world.getTileMap().getTilePixelLocation(ct.getTile());
//                world.getTileMap().setTile((int) vector.x / tileSize, (int) vector.y / tileSize, -1);
//            }
        }
    }

    @Override
    public void gameObjectCollisionOccurred(List<GameObject> collidedGameObjects) {
        for (GameObject g : collidedGameObjects) {
            if (g instanceof IFlyingObject) {
//                popSound.rewind();
//                popSound.play();
                ((IFlyingObject) g).hit();
//                world.deleteGameObject(g);
//                world.increaseBubblesPopped();
            }
        }
    }
}
