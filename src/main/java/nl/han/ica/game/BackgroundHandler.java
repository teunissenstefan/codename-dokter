package nl.han.ica.game;

import nl.han.ica.game.objects.Background;
import nl.han.ica.oopg.objects.Sprite;

import java.util.ArrayList;

public class BackgroundHandler {
    private Main world;
    private Sprite sprite;
    private ArrayList<Background> backgrounds = new ArrayList<>();
    private int scrollSpeed;

    public BackgroundHandler(Main world, Sprite sprite, int scrollSpeed) {
        this.world = world;
        this.sprite = sprite;
        this.setScrollSpeed(scrollSpeed);
        this.createBackgrounds();
    }

    /**
     * Achtergronden updaten
     */
    public void updateBackgrounds() {
        for (int i = 0; i < backgrounds.size(); i++) {
            Background bg = backgrounds.get(i);
            Background lastBg = backgrounds.get(backgrounds.size() - 1);
            if (bg.getX() + sprite.getWidth() < 0) {
                moveToLast(bg, lastBg, i);
                break;
            }
            bg.setxSpeed(-scrollSpeed);
        }
    }

    /**
     * De achtergrond buiten het beeld naar achteren gooien
     * @param bg
     * @param lastBg
     * @param i
     */
    private void moveToLast(Background bg, Background lastBg, int i) {
        bg.setX(lastBg.getX() + lastBg.getWidth() - scrollSpeed);
        backgrounds.remove(i);
        backgrounds.add(bg);
    }

    /**
     * De achtergronden initieren
     */
    private void createBackgrounds() {
        boolean createMoreBackgrounds = true;
        while(createMoreBackgrounds){
            int newX = backgrounds.size() * this.sprite.getWidth();
            Background bg = new Background(this.sprite, newX, 0);
            world.addGameObject(bg);
            backgrounds.add(bg);
            if(newX > world.width){
                createMoreBackgrounds = false;
            }
        }
    }

    /**
     * Scrollspeed setten
     *
     * @param scrollSpeed
     */
    public void setScrollSpeed(int scrollSpeed) {
        this.scrollSpeed = scrollSpeed;
    }

    /**
     * Scrolspeed getten
     *
     * @return
     */
    public int getScrollSpeed() {
        return this.scrollSpeed;
    }
}
