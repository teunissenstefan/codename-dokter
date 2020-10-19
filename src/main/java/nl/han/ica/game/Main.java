package nl.han.ica.game;

import nl.han.ica.oopg.engine.GameEngine;
import processing.core.PApplet;

public class Main extends GameEngine {
    private Player player;
    int tileSize = 50;
    private Level level;
    private int levelToLoad = -1;
    String resourcesString = "resources/";

    /**
     * Entry point
     *
     * @param args
     */
    public static void main(String[] args) {
        String[] processingArgs = {"Dr. Doctor vs. Corona"};
        Main mySketch = new Main();
        mySketch.handleApplicationArguments(args);

        PApplet.runSketch(processingArgs, mySketch);
    }

    /**
     * Verwerk de applicatie argumenten
     *
     * @param args
     */
    private void handleApplicationArguments(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("--level=") || args[i].startsWith("-l=")) {
                levelToLoad = Integer.parseInt(args[i].split("=")[1]);
            } else if (args[i].equals("--dev")) {
                resourcesString = "src/main/java/nl/han/ica/game/resources/";
            }
        }
    }

    /**
     * In deze methode worden de voor het spel
     * noodzakelijke zaken geïnitialiseerd
     */
    @Override
    public void setupGame() {
        deleteAllGameOBjects();
        if(levelToLoad > 0){
            level = new Level(this, levelToLoad);
            level.load();
            return;
        }
        level = new Level(this, 0);
        level.menuMain();
    }

    @Override
    public void update() {
        level.update();
    }

    /**
     * Level opvragen
     *
     * @return Level
     */
    public Level getLevel() {
        return this.level;
    }

    /**
     * Level setten
     * @param level
     */
    public void setLevel(Level level){
        this.level = level;
    }

    /**
     * Player opvragen
     *
     * @return Player
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Player instellen
     *
     * @param player Player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }
}
