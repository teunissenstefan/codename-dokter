package nl.han.ica.game.objects;

import nl.han.ica.game.Main;
import nl.han.ica.oopg.objects.AnimatedSpriteObject;
import nl.han.ica.oopg.objects.Sprite;
import nl.han.ica.oopg.objects.SpriteObject;

public class Block extends SpriteObject implements IFlyingObject {
    protected Main world;

    public Block(Main world, Sprite sprite, int speed) {
        super(sprite);
        this.world = world;
        this.setxSpeed(speed);
    }

    @Override
    public void update() {

    }

    @Override
    public void hit() {
        world.getPlayer().takeHit();
        world.deleteGameObject(this);
    }
}
