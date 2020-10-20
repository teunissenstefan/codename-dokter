package nl.han.ica.game.objects;

import nl.han.ica.game.Main;
import nl.han.ica.oopg.objects.Sprite;
import nl.han.ica.oopg.objects.SpriteObject;


public class Finish extends SpriteObject implements IFlyingObject {
    protected Main world;

    public Finish(Main world, Sprite sprite, int speed) {
        super(sprite);
        this.world = world;
        this.setxSpeed(speed);
    }

    @Override
    public void update() {
        this.checkIfOffScreen();
    }

    @Override
    public void hit() {
        this.world.getLevel().levelFinished();
    }

    @Override
    public void checkIfOffScreen() {
        if(this.getX() < (0 - super.width)){
            this.world.deleteGameObject(this);
        }
    }
}
