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
        this.world.getPlayer().getsHeart();
        this.world.deleteGameObject(this);
    }

    @Override
    public void checkIfOffScreen() {
        if(this.getX() > world.getWidth()){
            this.world.deleteGameObject(this);
        }
    }
}
