package nl.han.ica.game.objects;

import nl.han.ica.game.Main;
import nl.han.ica.oopg.objects.AnimatedSpriteObject;
import nl.han.ica.oopg.objects.GameObject;
import nl.han.ica.oopg.objects.Sprite;
import processing.core.PGraphics;

public class Coin extends AnimatedLoopObject implements IFlyingObject {
    protected Main world;

    public Coin(Main world, Sprite sprite, int speed) {
        super(sprite, 4);
        this.world = world;
        this.totalAnimationTime = 500;
        this.setxSpeed(speed);
    }

    @Override
    public void update() {
        super.update();
        this.checkIfOffScreen();
    }

    @Override
    public void hit() {
        world.getSoundHandler().getCoinSound().rewind();
        world.getSoundHandler().getCoinSound().play();
        this.world.getLevel().increaseScore(1);
        this.world.deleteGameObject(this);
    }

    @Override
    public void checkIfOffScreen() {
        if(this.getX() < (0 - super.width)){
            this.world.deleteGameObject(this);
        }
    }
}
