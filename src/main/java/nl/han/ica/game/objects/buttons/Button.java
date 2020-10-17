package nl.han.ica.game.objects.buttons;

import nl.han.ica.game.Main;
import nl.han.ica.oopg.objects.GameObject;
import nl.han.ica.oopg.userinput.IMouseInput;
import processing.core.PGraphics;

import java.awt.*;

public abstract class Button extends GameObject implements IMouseInput {
    private int x,y, width, height;
    private Main main;
    private Color bgColor;
    private Color fgColor;
    private String text;

    public Button(Main main, String text, int x, int y, int width, int height, Color bgColor, Color fgColor){
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
    public void draw(PGraphics g) {
        g.fill(bgColor.getRGB());
        g.rect(this.x, this.y, this.width, this.height);
        g.fill(fgColor.getRGB());
        g.textSize(20);
        g.text(this.text, this.x+this.width / 2 - g.textWidth(this.text) / 2, this.y+this.height / 2 + 20 / 2);
    }

    @Override
    public void update() {

    }

    @Override
    public void mousePressed(int x, int y, int button) {

    }

    @Override
    public void mouseReleased(int x, int y, int button){

    }

    @Override
    public abstract void mouseClicked(int x, int y, int button);

    /**
     * Muis hover
     * @return
     */
    public boolean isMouseOver(int x, int y){
        if(
                x > this.x
                && x < this.x+this.width
                && y > this.y
                && y < this.y+this.height
        ){
            return true;
        }
        return false;
    }

    @Override
    public void mouseMoved(int x, int y) {

    }

    @Override
    public void mouseDragged(int x, int y, int button) {

    }

    @Override
    public void mouseWheel(int direction) {

    }

    /**
     * Main ophalen
     * @return
     */
    public Main getMain() {
        return this.main;
    }
}
