package nl.han.ica.game;

import nl.han.ica.game.objects.Block;
import nl.han.ica.game.objects.Coin;
import nl.han.ica.game.objects.Heart;
import nl.han.ica.oopg.alarm.Alarm;
import nl.han.ica.oopg.alarm.IAlarmListener;
import nl.han.ica.oopg.objects.GameObject;
import nl.han.ica.oopg.objects.Sprite;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

public class ObjectSpawner implements IAlarmListener {

    private float objectsPerSecond;
    private Random random;
    private Main world;
    private Alarm alarm;
    private nl.han.ica.oopg.sound.Sound popSound;
    private ArrayList<Class> objectClassArrayList;
    private ArrayList<Sprite> objectSpriteArrayList;
    private ArrayList<Integer> objectSpeedArrayList;
    private ArrayList<Integer> objectLocationXArrayList;
    private ArrayList<Float> objectChanceArrayList;

    /**
     * Constructor
     *
     * @param world            Referentie naar de wereld
     * @param popSound         Geluid dat moet klinken als een bel knapt
     * @param objectsPerSecond Aantal objecten dat per seconden gemaakt moet worden
     */
    public ObjectSpawner(Main world, nl.han.ica.oopg.sound.Sound popSound, float objectsPerSecond) {
        this.objectsPerSecond = objectsPerSecond;
        this.world = world;
        this.popSound = popSound;
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
        for(int i = 0; i < objectSpriteArrayList.size(); i++){
            try{
                Float chance = objectChanceArrayList.get(i);
                if(randomBoolean(chance)){
                    Sprite sprite = objectSpriteArrayList.get(i);
                    Class objectClass = objectClassArrayList.get(i);
                    Integer speed = objectSpeedArrayList.get(i);
                    Integer locationX = objectLocationXArrayList.get(i);
                    Constructor<?> constructor = objectClass.getConstructor(Main.class, Sprite.class, int.class);
                    GameObject newGameObject = (GameObject)constructor.newInstance(this.world, sprite, speed);
                    world.addGameObject(newGameObject, locationX, randomNumber(0, world.getHeight() - sprite.getHeight()));
                    //kijken of hij erin zit. kan beste als we engine aanpassen tbh
                    // GameObject gameObjectCollide kijken ofzo
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
     * Kijken of het object mag spawnen
     * @param chance
     * @return
     */
    private boolean randomBoolean(float chance) {
        return Math.random() < chance;
    }

    /**
     * Willekeurig nummer genereren met min en max
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
        objectSpeedArrayList.add(-10);
        objectChanceArrayList.add(0.1f);
        objectLocationXArrayList.add(world.getWidth());

        Sprite blockSprite = new Sprite(world.resourcesString + "images/block.png");
        blockSprite.resize(50, 50);
        objectClassArrayList.add(Block.class);
        objectSpriteArrayList.add(blockSprite);
        objectSpeedArrayList.add(-10);
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