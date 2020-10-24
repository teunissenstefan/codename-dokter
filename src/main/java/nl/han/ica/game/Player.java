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
import nl.han.ica.oopg.objects.AnimatedSpriteObject;
import processing.core.PVector;

import java.util.List;

public class Player extends AnimatedSpriteObject implements ICollidableWithGameObjects {
    private int size = 100;
    private final Main world;
    int rightSpeed = 0;
    int leftSpeed = 0;
    int upSpeed = 0;
    int downSpeed = 0;
    boolean blink = false;
    long blinktime;
    long lastTime;
    int blinktimeAmount = 2000;
    private final int speed = 15;
    private final int gravity = 10;
    int lives = 3;
    private final boolean invincible;

    /**
     * Constructor
     *
     * @param world Referentie naar de wereld
     */
    public Player(Main world, Sprite sprite, int size, boolean invincible) {
        super(sprite, 2);
        this.world = world;
        this.size = size;
        setFriction(0.5f);
        this.setGravity(gravity);
        this.invincible = invincible;
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
            reloadIfNotInvincible(true);
        }
        if (getX() > world.getView().getWorldWidth() - size) {//rechts
            setxSpeed(0);
            setX(world.getView().getWorldWidth() - size);
        }
        if (getY() > world.getView().getWorldHeight() - size) {//onder
            reloadIfNotInvincible(false);
        }

        animateHit();
    }

    /**
     * De invincibility frames weergeven
     */
    private void animateHit() {
        if (blink) {
            if (System.currentTimeMillis() >= lastTime + 300) {
                nextFrame();
                lastTime = System.currentTimeMillis();
            }
            if(System.currentTimeMillis() >= blinktime){
                blink = false;
                setCurrentFrameIndex(0);
            }
        }
    }

    /**
     * Alleen herladen als de player niet permanent invincible is
     * @param top
     */
    private void reloadIfNotInvincible(boolean top) {
        if (!invincible) {
            world.getLevel().getObjectSpawner().stopAlarm();
            world.getLevel().load();
            return;
        }
        if (top) {
            setY(0);
            return;
        }
        setY(world.getView().getWorldHeight() - size);
    }

    @Override
    public void keyPressed(int keyCode, char key) {
        if (keyCode == world.LEFT) {
            leftSpeed = speed;
        }
        if (keyCode == world.UP || key == ' ' || keyCode == world.DOWN) {
            this.setGravity(this.getGravity() * -1);
        }
        if (keyCode == world.RIGHT) {
            rightSpeed = speed;
        }
    }

    @Override
    public void keyReleased(int keyCode, char key) {
        if (keyCode == world.LEFT) {
            leftSpeed = 0;
        }
        if (keyCode == world.RIGHT) {
            rightSpeed = 0;
        }
    }

    /**
     * De speler heeft pijn :(
     */
    public void takeHit() {
        if (!blink) {
            world.getSoundHandler().getHitSound().rewind();
            world.getSoundHandler().getHitSound().play();
            lives--;
            characterBlink();
        }
        if (lives == 0) {
            world.getLevel().load();
        }
    }

    /**
     * De speler heeft geen pijn meer :)
     */
    public void getsHeart() {
        lives++;
    }

    @Override
    public void gameObjectCollisionOccurred(List<GameObject> collidedGameObjects) {
        for (GameObject g : collidedGameObjects) {
            if (g instanceof IFlyingObject) {
                ((IFlyingObject) g).hit();
            }
        }
    }

    /**
     * Het knipperen starten
     * De dokter knippert als hij een blok raakt
     */
    public void characterBlink() {
        blink = true;
        blinktime = System.currentTimeMillis() + blinktimeAmount;
    }
}
