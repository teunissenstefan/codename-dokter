package nl.han.ica.game.objects.buttons;

import nl.han.ica.game.Level;
import nl.han.ica.game.Main;

import java.awt.*;

public class LevelSelectButton extends Button {
    private int level;

    public LevelSelectButton(Main main, int level, String text, int x, int y, int width, int height) {
        super(main, text, x, y, width, height, new Color(31, 169, 213), new Color(0, 0, 0));
        this.level = level;
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        if(super.isMouseOver(x,y)){
            Level level = new Level(this.getMain(), this.level);
            level.load();
            this.getMain().setLevel(level);
        }
    }
}
