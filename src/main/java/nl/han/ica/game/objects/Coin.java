package nl.han.ica.game.objects;

import nl.han.ica.game.Main;
import nl.han.ica.oopg.objects.AnimatedSpriteObject;
import nl.han.ica.oopg.objects.GameObject;
import nl.han.ica.oopg.objects.Sprite;
import processing.core.PGraphics;

public class Coin extends AnimatedSpriteObject {
    private Main world;
    private long lastTime = 0;
    private int totalAnimationTime = 500;
    private int totalFrames;

    public Coin(Main world, Sprite sprite) {
        super(sprite, 4);
        this.totalFrames = 4;
        this.world = world;
        setCurrentFrameIndex(0);
    }

    @Override
    public void update() {
        if (System.currentTimeMillis() > lastTime + (totalAnimationTime / totalFrames)) {
            setCurrentFrameIndex((getCurrentFrameIndex()+1 < totalFrames) ? getCurrentFrameIndex()+1 : 0);
            lastTime = System.currentTimeMillis();
        }
    }
}
