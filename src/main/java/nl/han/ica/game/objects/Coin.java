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
    }

    @Override
    public void hit() {
        this.world.getLevel().increaseScore(1);
        this.world.deleteGameObject(this);
        System.out.println(this.world.getLevel().getScore());
        System.out.println(this.world.getLevel().getHighScore());
        System.out.println("");
    }
}
