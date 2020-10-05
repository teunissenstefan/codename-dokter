package nl.han.ica.game.objects;

import nl.han.ica.game.Main;
import nl.han.ica.oopg.objects.AnimatedSpriteObject;
import nl.han.ica.oopg.objects.Sprite;

public abstract class AnimatedLoopObject extends AnimatedSpriteObject {
    protected long lastTime = 0;
    protected int totalAnimationTime = 500;
    protected int totalFrames;

    public AnimatedLoopObject(Sprite sprite, int totalFrames) {
        super(sprite, totalFrames);
        this.totalFrames = totalFrames;
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
