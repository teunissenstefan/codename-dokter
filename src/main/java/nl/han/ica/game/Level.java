package nl.han.ica.game;

import nl.han.ica.game.objects.Coin;
import nl.han.ica.oopg.objects.Sprite;
import nl.han.ica.oopg.tile.Tile;
import nl.han.ica.oopg.tile.TileMap;
import nl.han.ica.oopg.tile.TileType;
import nl.han.ica.oopg.view.EdgeFollowingViewport;
import nl.han.ica.oopg.view.View;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Level {
    private String levelDirectory;
    private Main world;
    int worldWidth = 1280;
    int worldHeight = 720;
    float zoomFactor = 1;
    int backgroundR = 52;
    int backgroundG = 201;
    int backgroundB = 235;

    /**
     * The constructor allows you to specify the filename the internal storage
     * will use.
     *
     * @param world          The Main world object
     * @param levelDirectory The name of the file that will be used for this persistance
     */
    public Level(Main world, String levelDirectory) {
        this.world = world;
        this.levelDirectory = levelDirectory;
    }

    /**
     * De tilesmap data ophalen en returnen als matrix
     *
     * @return int[][]
     */
    public int[][] getTilesMap() {
        int[][] returnArray;
        ArrayList<ArrayList<Integer>> linesArrayList = new ArrayList<>();
        int longestLine = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(this.getTileMapLocation()))) {
            String line;
            while ((line = br.readLine()) != null) {
                ArrayList<Integer> lineArrList = new ArrayList<>();
                String[] lineArr = line.split(",");
                for (int i = 0; i < lineArr.length; i++) {
                    lineArrList.add(Integer.parseInt(lineArr[i].trim()));
                }
                linesArrayList.add(lineArrList);
                longestLine = Math.max(lineArr.length, longestLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        returnArray = new int[linesArrayList.size()][longestLine];
        for (int i = 0; i < linesArrayList.size(); i++) {
            for (int x = 0; x < linesArrayList.get(i).size(); x++) {
                returnArray[i][x] = linesArrayList.get(i).get(x);
            }
        }
        return returnArray;
    }

    /**
     * De tilemap creëren
     *
     * @param tileSize
     * @param tileTypes
     * @return
     */
    public TileMap createTileMap(int tileSize, TileType[] tileTypes) {
        return new TileMap(tileSize, tileTypes, this.getTilesMap());
    }

    /**
     * Creeërt de view zonder viewport
     *
     * @param screenWidth  Breedte van het scherm
     * @param screenHeight Hoogte van het scherm
     */
    public void createViewWithoutViewport(int screenWidth, int screenHeight) {
        this.loadWorldData();
        View view = new View(screenWidth, screenHeight);
//        view.setBackground(world.loadImage("src/main/java/nl/han/ica/game/resources/images/achtergrond.png"));

        world.setView(view);
        world.size(screenWidth, screenHeight);
        view.setBackground(backgroundR, backgroundG, backgroundB);
    }

    /**
     * Creeërt de view met viewport
     *
     * @param screenWidth  Breedte van het scherm
     * @param screenHeight Hoogte van het scherm
     */
    public void createViewWithViewport(int screenWidth, int screenHeight) {
        this.loadWorldData();
        EdgeFollowingViewport viewPort = new EdgeFollowingViewport(world.player, (int) Math.ceil(screenWidth / zoomFactor), (int) Math.ceil(screenHeight / zoomFactor), 0, 0);
        viewPort.setTolerance(0, 0, 0, screenWidth / 3);
        viewPort.setY(0);
        viewPort.setX(0);
        View view = new View(viewPort, worldWidth, worldHeight);
        world.setView(view);
        world.size(screenWidth, screenHeight);
        view.setBackground(backgroundR, backgroundG, backgroundB);
//        view.setBackground(loadImage("src/main/java/nl/han/ica/game/resources/images/achtergrond.png"));//afbeelding moet even groot zijn als viewport >:(
    }

    /**
     * De wereld data instellen
     *
     * @param line
     */
    public void handleWorldData(String line) {
        if (line.startsWith("width=")) {
            worldWidth = Integer.parseInt(line.replace("width=", ""));
        } else if (line.startsWith("height=")) {
            worldHeight = Integer.parseInt(line.replace("height=", ""));
        } else if (line.startsWith("zoom=")) {
            zoomFactor = Float.parseFloat(line.replace("zoom=", ""));
        } else if (line.startsWith("backgroundR=")) {
            backgroundR = Integer.parseInt(line.replace("backgroundR=", ""));
        } else if (line.startsWith("backgroundG=")) {
            backgroundG = Integer.parseInt(line.replace("backgroundG=", ""));
        } else if (line.startsWith("backgroundB=")) {
            backgroundB = Integer.parseInt(line.replace("backgroundB=", ""));
        }
    }

    /**
     * De wereld data laden uit het bestand en doorgeven aan handleWorldData
     */
    public void loadWorldData() {
        try (BufferedReader br = new BufferedReader(new FileReader(this.getWorldDataLocation()))) {
            String line;
            while ((line = br.readLine()) != null) {
                handleWorldData(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * De objects instellen
     *
     * @param line
     */
    public void handleObjects(String line) {
        int coinSize = 50;
        Sprite coinSprite = new Sprite(world.resourcesString + "images/coin.png");
        coinSprite.resize(coinSize * 4, coinSize);
        if (line.startsWith("coin=")) {
            int[] vector = stringArrayToIntArray(line.replace("coin=", "").split(","));
            Coin coin = new Coin(world, coinSprite);
            world.addGameObject(coin, vector[0], vector[1]);
        } else if (line.startsWith("player=")) {
            int[] vector = stringArrayToIntArray(line.replace("player=", "").split(","));
            world.addGameObject(world.player, vector[0], vector[1]);
        }
    }

    /**
     * String[] overzetten naar int[]
     *
     * @param stringArray
     * @return
     */
    public int[] stringArrayToIntArray(String[] stringArray) {
        int[] intArray = new int[stringArray.length];
        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = Integer.parseInt(stringArray[i]);
        }
        return intArray;
    }

    /**
     * De object laden uit het bestand en doorgeven aan handleObjects
     */
    public void loadObjects() {
        try (BufferedReader br = new BufferedReader(new FileReader(this.getObjectsLocation()))) {
            String line;
            while ((line = br.readLine()) != null) {
                handleObjects(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * De tilemap locatie opvragen
     *
     * @return
     */
    private String getTileMapLocation() {
        return levelDirectory.concat("tilemap");
    }

    /**
     * De wereld data locatie opvragen
     *
     * @return
     */
    private String getWorldDataLocation() {
        return levelDirectory.concat("worlddata");
    }

    /**
     * De object data locatie opvragen
     *
     * @return
     */
    private String getObjectsLocation() {
        return levelDirectory.concat("objects");
    }
}
