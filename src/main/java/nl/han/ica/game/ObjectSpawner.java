package nl.han.ica.game;

import nl.han.ica.game.objects.Block;
import nl.han.ica.game.objects.Coin;
import nl.han.ica.game.objects.Heart;
import nl.han.ica.game.objects.IFlyingObject;
import nl.han.ica.oopg.alarm.Alarm;
import nl.han.ica.oopg.alarm.IAlarmListener;
import nl.han.ica.oopg.objects.GameObject;
import nl.han.ica.oopg.objects.Sprite;
import processing.core.PVector;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

public class ObjectSpawner implements IAlarmListener {

    private float objectsPerSecond;
    public int defaultSpeed = -10;
    private Random random;
    private Main world;
    private Alarm alarm;
    private ArrayList<Class> objectClassArrayList;
    private ArrayList<Sprite> objectSpriteArrayList;
    private ArrayList<Integer> objectSpeedArrayList;
    private ArrayList<Integer> objectLocationXArrayList;
    private ArrayList<Float> objectChanceArrayList;

    /**
     * Constructor
     *
     * @param world            Referentie naar de wereld
     * @param objectsPerSecond Aantal objecten dat per seconden gemaakt moet worden
     */
    public ObjectSpawner(Main world, float objectsPerSecond) {
        this.objectsPerSecond = objectsPerSecond;
        this.world = world;
        random = new Random();
        setupObjectList();
        startAlarm();
    }

    /**
     * Start het alarm
     */
    private void startAlarm() {
        alarm = new Alarm("New object", 1 / objectsPerSecond);
        alarm.addTarget(this);
        alarm.start();
    }

    /**
     * Stop het alarm
     */
    public void stopAlarm() {
        alarm.stop();
    }

    /**
     * Generates a block and coin
     */
    @Override
    public void triggerAlarm(String alarmName) {
        for (int i = 0; i < objectSpriteArrayList.size(); i++) {
            try {
                Float chance = objectChanceArrayList.get(i);
                if (randomBoolean(chance)) {
                    Sprite sprite = objectSpriteArrayList.get(i);
                    Class objectClass = objectClassArrayList.get(i);
                    Integer speed = objectSpeedArrayList.get(i);
                    Integer locationX = objectLocationXArrayList.get(i);
                    Constructor<?> constructor = objectClass.getConstructor(Main.class, Sprite.class, int.class);
                    GameObject newGameObject = (GameObject) constructor.newInstance(this.world, sprite, speed);
                    world.addGameObject(newGameObject, locationX, 0);
                    checkIfObjectIsInsideOtherObject(newGameObject, sprite);
                }
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
                world.exit();
            }
        }

        //Start the next call for objects
        startAlarm();
    }

    /**
     * Kijken of het nieuwe object in een ander object zit:
     *  zoja: een nieuwe Y genereren
     * @param newGameObject
     * @param sprite
     */
    private void checkIfObjectIsInsideOtherObject(GameObject newGameObject, Sprite sprite) {
        boolean isInside = true;
        int maxTries = 50;
        int curTry = 0;
        while(isInside && curTry < maxTries){
            newGameObject.setY(randomNumber(0, world.getHeight() - sprite.getHeight()));
            boolean found = false;
            for (int i = 0; i < world.getGameObjectItems().size(); i++) {
                GameObject go = world.getGameObjectItems().get(i);
                if (go != newGameObject && go instanceof IFlyingObject && newGameObject.getDistanceFrom(go) == 0.0) {
                    found = true;
                    break;
                }
            }
            isInside = found;
            curTry++;
        }
    }

    /**
     * Kijken of het object mag spawnen
     *
     * @param chance
     * @return
     */
    private boolean randomBoolean(float chance) {
        return Math.random() < chance;
    }

    /**
     * Willekeurig nummer genereren met min en max
     *
     * @param min
     * @param max
     * @return
     */
    private int randomNumber(int min, int max) {
        return random.nextInt(max) + min;
    }

    /**
     * De te spawnen objecten aanmaken
     */
    private void setupObjectList() {
        objectClassArrayList = new ArrayList<>();
        objectSpriteArrayList = new ArrayList<>();
        objectSpeedArrayList = new ArrayList<>();
        objectChanceArrayList = new ArrayList<>();
        objectLocationXArrayList = new ArrayList<>();

        Sprite coinSprite = new Sprite(world.resourcesString + "images/coin.png");
        coinSprite.resize(200, 50);
        objectClassArrayList.add(Coin.class);
        objectSpriteArrayList.add(coinSprite);
        objectSpeedArrayList.add(defaultSpeed);
        objectChanceArrayList.add(0.1f);
        objectLocationXArrayList.add(world.getWidth());

        Sprite blockSprite = new Sprite(world.resourcesString + "images/block.png");
        blockSprite.resize(50, 50);
        objectClassArrayList.add(Block.class);
        objectSpriteArrayList.add(blockSprite);
        objectSpeedArrayList.add(defaultSpeed);
        objectChanceArrayList.add(0.9f);
        objectLocationXArrayList.add(world.getWidth());

        Sprite heartSprite = new Sprite(world.resourcesString + "images/hardcore.png");
        heartSprite.resize(200, 50);
        objectClassArrayList.add(Heart.class);
        objectSpriteArrayList.add(heartSprite);
        objectSpeedArrayList.add(5);
        objectChanceArrayList.add(0.05f);
        objectLocationXArrayList.add(-50);
    }
}