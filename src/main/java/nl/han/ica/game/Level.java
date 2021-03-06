package nl.han.ica.game;

import nl.han.ica.game.objects.TextPanel;
import nl.han.ica.game.objects.buttons.*;
import nl.han.ica.game.objects.ImageObject;
import nl.han.ica.game.objects.buttons.Button;
import nl.han.ica.oopg.dashboard.Dashboard;
import nl.han.ica.oopg.objects.GameObject;
import nl.han.ica.oopg.objects.Sprite;
import nl.han.ica.oopg.view.EdgeFollowingViewport;
import nl.han.ica.oopg.view.View;
import nl.han.ica.game.objects.Finish;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static processing.core.PConstants.ARROW;

public class Level {
    private String levelDirectory;
    private int levelToLoad;
    private Main world;
    private BackgroundHandler backgroundHandler;
    private ObjectSpawner objectSpawner;
    private TextObject levelText;
    private TextObject scoreText;
    private TextObject highScoreText;
    private TextObject timeText;
    private TextObject livesText;
    private TextPanel creditsTextPanel;
    private int worldWidth = 1280;
    private int worldHeight = 720;
    private int windowWidth = 1280;
    private int windowHeight = 720;
    private int backgroundR = 52;
    private int backgroundG = 201;
    private int backgroundB = 235;
    private int score;
    private int highscore = 0;
    private int timeInSeconds = -1;
    private int difficulty = 1;
    private long lastUpdateTime = 0;
    private MenuState menuState;
    private long levelStartTime;

    /**
     * The constructor allows you to specify the filename the internal storage
     * will use.
     *
     * @param world       Main
     * @param levelToLoad int
     */
    public Level(Main world, int levelToLoad) {
        this.world = world;
        this.levelToLoad = levelToLoad;
        this.setLevelDirectory();
    }

    /**
     * De map inladen
     */
    public void load() {
        this.menuState = MenuState.Level;
        deleteAllObjects();
        score = 0;

        levelText = new TextObject("Level: ", 0, 0);
        livesText = new TextObject("Lives: 3", 0, 50);
        scoreText = new TextObject("Score : 0", 0, windowHeight - 50);
        highScoreText = new TextObject("High Score : 0", 0, windowHeight - 100);
        timeText = new TextObject("Time: 0", 0, windowHeight - 150);

        highscore = this.getHighscoreFromFile();

        createViewWithoutViewport(windowWidth, windowHeight);

        backgroundHandler = new BackgroundHandler(world, this.getBackground(), 5);
        createObjects(false);
        createObjectSpawner();
        createDashboard(worldWidth, worldHeight);
        levelStartTime = System.currentTimeMillis();
    }

    /**
     * De basis van een menu laden
     */
    public void loadMenu() {
        deleteAllObjects();
        score = 0;

        levelText = new TextObject("Level: ", 0, 0);
        livesText = new TextObject("Lives: 3", 0, 50);
        scoreText = new TextObject("Score : 0", 0, windowHeight - 50);
        highScoreText = new TextObject("High Score : 0", 0, windowHeight - 100);
        timeText = new TextObject("Time: 0", 0, windowHeight - 150);

        createViewWithoutViewport(windowWidth, windowHeight);

        backgroundHandler = new BackgroundHandler(world, this.getBackground(), 5);
        createObjects(true);
    }

    /**
     * Main menu openen
     */
    public void menuMain() {
        this.loadMenu();
        creditsTextPanel = null;
        levelToLoad = 0;
        this.menuState = MenuState.MainMenu;

        Sprite logoSprite = new Sprite(world.resourcesString + "images/logo.png");
        ImageObject logo = new ImageObject(logoSprite);
        world.addGameObject(logo, world.getLevel().worldWidth / 2 - logo.getImage().width / 2, 10);

        Button btnExitGame = new ExitButton(world, world.getWidth() / 2 - 200 / 2, world.getHeight() - (100 + 50), 200, 100);
        Button btnSelectLevel = new LevelsButton(world, world.getWidth() / 2 - 200 / 2, world.getHeight() - (100 + 200), 200, 100);
        TextPanel authors = new TextPanel(world,
                "Thomas & Stefan",
                10, this.world.getHeight() - 60, 190, 50, new Color(113, 113, 113), new Color(0, 0, 0)
        );
        world.addGameObject(btnExitGame);
        world.addGameObject(btnSelectLevel);
        world.addGameObject(authors);
    }

    /**
     * Levels menu openen
     */
    public void menuLevels() {
        this.loadMenu();
        this.menuState = MenuState.LevelSelect;

        Sprite trumpSprite = new Sprite(world.resourcesString + "images/trump.png");
        ImageObject trump = new ImageObject(trumpSprite);
        world.addGameObject(trump, world.getLevel().getWorldWidth() - trump.getImage().width, world.getLevel().getWorldHeight() / 2 - trump.getImage().height / 2);

        Button btnBack = new MainMenuButton(world, 10, 10, 200, 100);
        Button btnLevel1 = new LevelSelectButton(world, 1, "1: Toe", 1111, 666, 100, 50);
        Button btnLevel2 = new LevelSelectButton(world, 2, "2: Leg", 1111, 454, 70, 200);
        Button btnLevel3 = new LevelSelectButton(world, 3, "3: Intestines", 1050, 290, 150, 100);
        Button btnLevel4 = new LevelSelectButton(world, 4, "4: Lungs", 1070, 180, 125, 100);
        TextPanel description = new TextPanel(world,
                "TRUMP IS IN DANGER\nAND YOU HAVE TO SAVE HIM!\nHelp trump defeat the evil libtards\nby curing him of Corona!\n\n" +
                        "You are Dr. Doctor, flying through Trump's body\nto save him. Throughout his body you will find\n" +
                        "obstacles that you have to avoid. This is the\nonly way to cure him!",
                10, world.getLevel().getWorldHeight() - (290 + 10), 500, 290, new Color(113, 113, 113), new Color(0, 0, 0)
        );
        world.addGameObject(btnLevel1);
        if (getUnlockedLevels().contains(2))
            world.addGameObject(btnLevel2);
        if (getUnlockedLevels().contains(3))
            world.addGameObject(btnLevel3);
        if (getUnlockedLevels().contains(4))
            world.addGameObject(btnLevel4);
        world.addGameObject(btnBack);
        world.addGameObject(description);
    }

    /**
     * Alle objecten deleten
     */
    public void deleteAllObjects() {
        world.deleteAllGameOBjects();
        world.deleteAllDashboards();
        if (objectSpawner != null) {
            objectSpawner.stopAlarm();
        }
    }

    /**
     * Maakt het dashboard aan
     *
     * @param dashboardWidth  Gewenste breedte van dashboard
     * @param dashboardHeight Gewenste hoogte van dashboard
     */
    private void createDashboard(int dashboardWidth, int dashboardHeight) {
        Dashboard dashboard = new Dashboard(0, 0, dashboardWidth, dashboardHeight);
        dashboard.addGameObject(levelText);
        dashboard.addGameObject(livesText);
        dashboard.addGameObject(scoreText);
        dashboard.addGameObject(highScoreText);
        if (timeInSeconds > -1) {
            dashboard.addGameObject(timeText);
        }
        world.addDashboard(dashboard);
    }

    /**
     * Updaten
     */
    public void update() {
        if (backgroundHandler != null) {
            backgroundHandler.updateBackgrounds();
        }
        if (creditsTextPanel != null && creditsTextPanel.getY() + creditsTextPanel.getHeight() < 0) {
            this.menuMain();
        }
        movePlayerToLast();

        livesText.setText("Lives: " + world.getPlayer().lives);
        scoreText.setText("Score: " + score);
        highScoreText.setText("High Score: " + highscore);

        timerCountDown();
    }

    /**
     * De timer laten aftellen en kijken of de tijd voorbij is
     */
    private void timerCountDown() {
        if (timeInSeconds > 0) {
            timeText.setText("Time: " + timeInSeconds);
            if (System.currentTimeMillis() > lastUpdateTime + 1000) {
                timeInSeconds--;
                lastUpdateTime = System.currentTimeMillis();
            }
        } else if (timeInSeconds == 0 && objectSpawner != null) {
            timeText.setText("Time: " + timeInSeconds);
            timeInSeconds--;
            objectSpawner.stopAlarm();
            finishSpawner(-2);
        }
    }

    /**
     * Level finishen
     */
    public void levelFinished() {
        this.setTimeToFile(System.currentTimeMillis() - levelStartTime);
        this.world.getSoundHandler().getFinishSound().rewind();
        this.world.getSoundHandler().getFinishSound().play();
        if (isLastLevel()) {
            menuCredits();
        } else {
            unlockNextLevel();
            if (levelToLoad != 0) {
                Level level = new Level(this.world, levelToLoad + 1);
                level.load();
                this.world.setLevel(level);
            }
        }
    }

    /**
     * De Finishline spawnen
     *
     * @param xspeed
     */
    private void finishSpawner(int xspeed) {
        Sprite finishSprite = new Sprite(world.resourcesString + "images/finish.png");
        GameObject newGameObject = new Finish(this.world, finishSprite, xspeed);
        world.addGameObject(newGameObject, world.getWidth(), 0);
    }

    /**
     * Aftiteling starten
     */
    private void menuCredits() {
        this.menuState = MenuState.Credits;
        levelToLoad = 0;
        this.setLevelDirectory();
        world.deleteAllDashboards();
        world.deleteAllGameOBjects();
        world.getView().setBackground(0, 0, 0);

        long lvl1Time = getTimeFromFile(1);
        long lvl2Time = getTimeFromFile(2);
        long lvl3Time = getTimeFromFile(3);
        long lvl4Time = getTimeFromFile(4);
        long totalTime = lvl1Time+lvl2Time+lvl3Time+lvl4Time;

        int creditsWidth = 230;
        creditsTextPanel = new TextPanel(this.world, "Congratulations!\nYou cured Trump!\n\n\n" +

                "Time per level:\n"+
                "Level 1: "+calculateTime(lvl1Time)+"\n"+
                "Level 2: "+calculateTime(lvl2Time)+"\n"+
                "Level 3: "+calculateTime(lvl3Time)+"\n"+
                "Level 4: "+calculateTime(lvl4Time)+"\n\n"+
                "Total time: "+calculateTime(totalTime)+"\n\n\n"+

                "Programmers:\n" +
                "Stefan Teunissen\n" +
                "Thomas van Minnen\n\n\n"+

                "Graphic designers:\n" +
                "Stefan Teunissen\n" +
                "Thomas van Minnen\n"+
                "Shay Deeders\n\n\n"+

                "Audio designers:\n" +
                "Stefan Teunissen\n" +
                "Thomas van Minnen\n\n\n"+

                "Level designers:\n" +
                "Stefan Teunissen\n" +
                "Thomas van Minnen\n\n\n"+

                "Story designers:\n" +
                "Stefan Teunissen\n" +
                "Thomas van Minnen\n\n\n"+

                "Animation designers:\n" +
                "Stefan Teunissen\n" +
                "Thomas van Minnen\n\n\n"+

                "Directors:\n" +
                "Stefan Teunissen\n" +
                "Thomas van Minnen\n\n\n",
                this.windowWidth / 2 - creditsWidth / 2, this.windowHeight, creditsWidth, 1455, Color.black, Color.white);
        creditsTextPanel.setySpeed(-2);
        world.addGameObject(creditsTextPanel);
    }

    /**
     * De tijd van minuten en seconden als 00:00 string geven
     * @param time
     * @return
     */
    private String calculateTime(long time){
        int minutes = 0;
        int seconds = 0;

        seconds = Math.round(time / 1000);
        minutes = seconds / 60;
        seconds = seconds - (60* minutes);

        String minutesString = (String.valueOf(minutes).length() < 2) ? "0"+minutes : String.valueOf(minutes);
        String secondsString = (String.valueOf(seconds).length() < 2) ? "0"+seconds : String.valueOf(seconds);
        return minutesString+":"+secondsString;
    }

    /**
     * De player naar het einde van de gameObject lijst gooien zodat hij als laatste wordt getekend
     */
    private void movePlayerToLast() {
        for (int i = 0; i < world.getGameObjectItems().size(); i++) {
            GameObject go = world.getGameObjectItems().get(i);
            if (go instanceof Player) {
                world.deleteGameObject(go);
                world.addGameObject(go);
                break;
            }
        }
    }

    /**
     * Increase score and update highscore
     *
     * @param increment int
     */
    public void increaseScore(int increment) {
        score += increment;
        if (score > highscore) {
            highscore = score;
            this.setHighscoreToFile(highscore);
        }
    }

    /**
     * De achtergrond sprite maken
     *
     * @return Sprite
     */
    private Sprite getBackground() {
        return new Sprite(this.getBackgroundImageLocation());
    }

    /**
     * Creeërt de view zonder viewport
     *
     * @param screenWidth  Breedte van het scherm
     * @param screenHeight Hoogte van het scherm
     */
    private void createViewWithoutViewport(int screenWidth, int screenHeight) {
        this.loadWorldData();
        View view = new View(screenWidth, screenHeight);

        world.setView(view);
        world.size(screenWidth, screenHeight);
        view.setBackground(backgroundR, backgroundG, backgroundB);
    }

    /**
     * De wereld data instellen
     *
     * @param line worlddata bestand regel
     */
    private void handleWorldData(String line) {
        if (line.startsWith("backgroundR=")) {
            backgroundR = Integer.parseInt(line.replace("backgroundR=", ""));
        } else if (line.startsWith("backgroundG=")) {
            backgroundG = Integer.parseInt(line.replace("backgroundG=", ""));
        } else if (line.startsWith("backgroundB=")) {
            backgroundB = Integer.parseInt(line.replace("backgroundB=", ""));
        } else if (line.startsWith("title=")) {
            levelText.setText("Level: " + line.replace("title=", ""));
        } else if (line.startsWith("time=")) {
            timeInSeconds = Integer.parseInt(line.replace("time=", ""));
        } else if (line.startsWith("difficulty=")) {
            difficulty = Integer.parseInt(line.replace("difficulty=", ""));
        }
    }

    /**
     * De wereld data laden uit het bestand en doorgeven aan handleWorldData
     */
    private void loadWorldData() {
        File f = new File(this.getWorldDataLocation());
        if (!f.exists() || f.isDirectory())
            return;
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
     * Maakt de spelobjecten aan
     */
    private void createObjects(boolean invincible) {
        int playerSize = 50;
        Sprite playerSprite = new Sprite(world.resourcesString + "images/player.png");
        playerSprite.resize(playerSize * 2, playerSize);
        world.setPlayer(new Player(world, playerSprite, playerSize, invincible));
        world.addGameObject(world.getPlayer(), 0, 0);
    }

    /**
     * Maakt de spawner voor de objecten aan
     */
    public void createObjectSpawner() {
        objectSpawner = new ObjectSpawner(world, difficulty);
    }

    /**
     * Highscore in bestand gooien
     *
     * @param highscore
     */
    public void setHighscoreToFile(int highscore) {
        try (PrintWriter out = new PrintWriter(this.getHighscoreLocation())) {
            out.println(highscore);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Highscore laden uit bestand
     *
     * @return
     */
    public int getHighscoreFromFile() {
        File f = new File(this.getHighscoreLocation());
        if (f.exists() && !f.isDirectory()) {
            try {
                String content = Files.readString(Paths.get(this.getHighscoreLocation()), StandardCharsets.US_ASCII);
                return Integer.parseInt(content.trim());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * Tijd in bestand gooien
     *
     * @param time
     */
    public void setTimeToFile(long time) {
        try (PrintWriter out = new PrintWriter(this.getTimeLocation())) {
            out.println(time);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tijd laden uit bestand
     *
     * @return
     */
    public long getTimeFromFile(int level) {
        String timeFileLocation = this.getTimeLocation();
        if(level != 0){
            timeFileLocation = String.format(world.resourcesString + "levels/%1s/time", level);
        }
        File f = new File(timeFileLocation);
        if (f.exists() && !f.isDirectory()) {
            try {
                String content = Files.readString(Paths.get(timeFileLocation), StandardCharsets.US_ASCII);
                return Long.parseLong(content.trim());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * Wereld breedte ophalen
     *
     * @return
     */
    public int getWorldWidth() {
        return this.worldWidth;
    }

    /**
     * Wereld hoogte ophalen
     *
     * @return
     */
    public int getWorldHeight() {
        return this.worldHeight;
    }

    /**
     * De achtergrond afbeelding locatie opvragen
     *
     * @return String
     */
    private String getBackgroundImageLocation() {
        return levelDirectory.concat("background.png");
    }

    /**
     * De wereld data locatie opvragen
     *
     * @return String
     */
    private String getWorldDataLocation() {
        return levelDirectory.concat("worlddata");
    }

    /**
     * De highscore locatie opvragen
     *
     * @return String
     */
    private String getHighscoreLocation() {
        return levelDirectory.concat("highscore");
    }

    /**
     * De tijd locatie opvragen
     *
     * @return String
     */
    private String getTimeLocation() {
        return levelDirectory.concat("time");
    }

    /**
     * De vrijgespeelde levels ophalen
     *
     * @return
     */
    private ArrayList<Integer> getUnlockedLevels() {
        ArrayList<Integer> unlockedLevels = new ArrayList<>();
        unlockedLevels.add(1);

        File f = new File(this.getUnlockedLevelsLocation());
        if (f.exists() && !f.isDirectory()) {
            try {
                String content = Files.readString(Paths.get(this.getUnlockedLevelsLocation()), StandardCharsets.US_ASCII).trim();
                String[] levelStrings = content.split(",");
                for (int i = 0; i < levelStrings.length; i++) {
                    unlockedLevels.add(Integer.parseInt(levelStrings[i]));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return unlockedLevels;
    }

    /**
     * Kijken of het huidige level het laatste level is
     *
     * @return
     */
    private boolean isLastLevel() {
        File folder = new File(world.resourcesString + "levels");
        int lastLevel = 0;
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                int iLevel = Integer.parseInt(fileEntry.getName());
                lastLevel = Math.max(iLevel, lastLevel);
            }
        }
        return lastLevel == levelToLoad;
    }

    /**
     * De vrijgespeelde levels opslaan in het bestand
     */
    private void unlockNextLevel() {
        isLastLevel();
        ArrayList<Integer> alreadyUnlockedLevels = getUnlockedLevels();
        if (!alreadyUnlockedLevels.contains(levelToLoad + 1))
            alreadyUnlockedLevels.add(levelToLoad + 1);

        String putString = "";
        for (int i = 0; i < alreadyUnlockedLevels.size(); i++) {
            if (!(alreadyUnlockedLevels.get(i) == 1))
                putString = putString.concat(alreadyUnlockedLevels.get(i) + ",");
        }
        try (PrintWriter out = new PrintWriter(this.getUnlockedLevelsLocation())) {
            out.println(putString);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * De levelDirectory setten
     *
     * @return
     */
    private void setLevelDirectory() {
        levelDirectory = String.format(world.resourcesString + "levels/%1s/", levelToLoad);
    }

    /**
     * De locatie voor de vrijgespeelde levels ophalen
     *
     * @return
     */
    private String getUnlockedLevelsLocation() {
        return world.resourcesString.concat("levels/unlocked");
    }

    /**
     * De MenuState opvragen
     *
     * @return
     */
    public MenuState getMenuState() {
        return this.menuState;
    }

    /**
     * De objectSpawner ophalen
     *
     * @return
     */
    public ObjectSpawner getObjectSpawner() {
        return this.objectSpawner;
    }
}
