package nl.han.ica.game;

import nl.han.ica.oopg.objects.GameObject;
import processing.core.PGraphics;

public class TextObject extends GameObject {

    private String text;
    private int fontSize;

    public TextObject(String text) {
        this.text = text;
        this.fontSize = 30;
    }

    public TextObject(String text, int x, int y) {
        this.text = text;
        this.fontSize = 30;
        this.x = x;
        this.y = y;
    }

    public TextObject(String text, int fontSize) {
        this.text = text;
        this.fontSize = fontSize;
    }

    /**
     * Tekstgrootte setten
     * @param fontSize
     */
    public void setFontSize(int fontSize){
        this.fontSize = fontSize;
    }

    /**
     * Tekst string setten
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(PGraphics g) {
        g.textAlign(g.LEFT, g.TOP);
        g.textSize(this.fontSize);
        g.text(text, getX(), getY());
    }
}
