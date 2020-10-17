package nl.han.ica.game.objects.buttons;

import nl.han.ica.game.Main;

import java.awt.*;

public class ExitButton extends Button {
    public ExitButton(Main main, int x, int y, int width, int height) {
        super(main, "Exit", x, y, width, height, new Color(187, 26, 26), new Color(0, 0, 0));
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        if(super.isMouseOver(x,y)){
            super.getMain().exit();
        }
    }
}
