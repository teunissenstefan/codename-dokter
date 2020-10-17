package nl.han.ica.game.objects.buttons;

import nl.han.ica.game.Level;
import nl.han.ica.game.Main;

import java.awt.*;

public class MainMenuButton extends Button {
    public MainMenuButton(Main main, int x, int y, int width, int height) {
        super(main, "Back", x, y, width, height, new Color(187, 26, 26), new Color(0, 0, 0));
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        if(super.isMouseOver(x,y)){
            this.getMain().getLevel().menuMain();
        }
    }
}
