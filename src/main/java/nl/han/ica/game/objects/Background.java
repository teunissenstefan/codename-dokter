package nl.han.ica.game.objects;

import nl.han.ica.game.Main;
import nl.han.ica.oopg.objects.GameObject;
import nl.han.ica.oopg.objects.Sprite;
import nl.han.ica.oopg.objects.SpriteObject;
import processing.core.PGraphics;

public class Background extends SpriteObject {
    public Background(Sprite sprite, int x, int y){
        super(sprite);
        this.x = x;
        this.y = y;
    }

    @Override
    public void update() {

    }
}
