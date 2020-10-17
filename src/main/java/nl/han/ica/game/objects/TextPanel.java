package nl.han.ica.game.objects;

import nl.han.ica.game.Main;
import nl.han.ica.oopg.objects.GameObject;
import processing.core.PGraphics;

import java.awt.*;

public class TextPanel extends GameObject {
    private int x,y, width, height;
    private Main main;
    private Color bgColor;
    private Color fgColor;
    private String text;

    public TextPanel(Main main, String text, int x, int y, int width, int height, Color bgColor, Color fgColor){
        this.main = main;
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bgColor = bgColor;
        this.fgColor = fgColor;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(PGraphics g) {
        g.fill(bgColor.getRGB());
        g.rect(this.x, this.y, this.width, this.height);
        g.fill(fgColor.getRGB());
        g.textSize(20);
        g.text(this.text, 20, this.y+30);
    }
}
