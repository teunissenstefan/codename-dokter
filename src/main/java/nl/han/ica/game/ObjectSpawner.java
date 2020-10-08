package nl.han.ica.game;

import nl.han.ica.game.objects.Block;
import nl.han.ica.oopg.alarm.Alarm;
import nl.han.ica.oopg.alarm.IAlarmListener;
import nl.han.ica.oopg.objects.GameObject;
import nl.han.ica.oopg.objects.Sprite;

import java.util.Random;

public class ObjectSpawner implements IAlarmListener {

    private float objectsPerSecond;
    private Random random;
    private Main world;
    private nl.han.ica.oopg.sound.Sound popSound;

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
        startAlarm();
    }

    private void startAlarm() {
        Alarm alarm = new Alarm("New object", 1 / objectsPerSecond);
        alarm.addTarget(this);
        alarm.start();
    }

    @Override
    public void triggerAlarm(String alarmName) {
        Sprite blockSprite = new Sprite(world.resourcesString + "images/block.png");
        blockSprite.resize(50,50);
        GameObject block = new Block(world, blockSprite, -10);
        world.addGameObject(block, world.getLevel().worldWidth, 151);
//        System.out.println("pizza");
        startAlarm();
    }
}