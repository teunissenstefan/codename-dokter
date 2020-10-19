package nl.han.ica.game.objects;

import nl.han.ica.game.Main;
import nl.han.ica.oopg.objects.Sprite;

public class Heart extends AnimatedLoopObject implements IFlyingObject  {
    protected Main world;

    public Heart(Main world, Sprite sprite, int speed) {
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
        this.world.getLevel().increaseScore(1);
        this.world.deleteGameObject(this);
        this.world.getPlayer().getsHeart();
    }

    @Override
    public void checkIfOffScreen() {
        if(this.getX() < (0 - super.width)){
            this.world.deleteGameObject(this);
        }
    }
}
