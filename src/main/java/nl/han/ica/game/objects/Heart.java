package nl.han.ica.game.objects;

import nl.han.ica.game.Main;
import nl.han.ica.oopg.objects.Sprite;
import org.tritonus.share.sampled.convert.TSynchronousFilteredAudioInputStream;

public class Heart extends AnimatedLoopObject implements IFlyingObject  {
    protected Main world;
    protected Sprite sprite;

    public Heart(Main world, Sprite sprite, int speed) {
        super(sprite, 4);
        this.world = world;
        this.sprite = sprite;
        this.totalAnimationTime = 500;
        this.setxSpeed(speed);

        setySpeed(5);
    }

    public void upAndDown(int worldHeight){
        if(this.getY()+sprite.getHeight() >= (worldHeight * 0.95)){
            setySpeed(-5);
        }

        if(this.getY() <= (worldHeight * 0.05)){
            setySpeed(5);
        }
    }

    @Override
    public void update() {
        super.update();
        this.checkIfOffScreen();
        this.upAndDown(world.getLevel().getWorldHeight());
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
