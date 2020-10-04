package nl.han.ica.game;

import nl.han.ica.oopg.objects.Sprite;
import nl.han.ica.oopg.tile.Tile;

public class BoardTile extends Tile {
    /**
     * @param sprite The image which will be drawn whenever the draw method of the Tile is called.
     */
    public BoardTile(Sprite sprite) {
        super(sprite);
    }
}
