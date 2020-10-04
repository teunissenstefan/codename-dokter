package nl.han.ica.game;

import nl.han.ica.game.objects.Background;
import nl.han.ica.oopg.engine.GameEngine;
import nl.han.ica.oopg.objects.Sprite;
import nl.han.ica.oopg.tile.TileType;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

import javax.imageio.ImageIO;
import java.applet.Applet;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Main extends GameEngine {
    protected Player player;
    int tileSize = 50;
    private Level level;
    private int levelToLoad = 1;
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
            if(args[i].startsWith("--level=") || args[i].startsWith("-l=")){
                levelToLoad = Integer.parseInt(args[i].split("=")[1]);
            }else if(args[i].equals("--dev")){
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
        String levelString = String.format(resourcesString + "levels/%1s/", levelToLoad);
        level = new Level(this, levelString);
        level.load();
    }

    @Override
    public void update() {
        level.update();
    }

    @Override
    public void keyPressed() {
        ///Tijdelijk erin houden voor level load test
        if(key == '1'){
            levelToLoad = 1;
            setupGame();
        }else if(key == '2'){
            levelToLoad = 2;
            setupGame();

        }
        super.keyPressed();
    }
}
