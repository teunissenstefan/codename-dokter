package nl.han.ica.game;

import nl.han.ica.game.objects.Block;
import nl.han.ica.game.objects.Coin;
import nl.han.ica.oopg.alarm.Alarm;
import nl.han.ica.oopg.alarm.IAlarmListener;
import nl.han.ica.oopg.objects.GameObject;
import nl.han.ica.oopg.objects.Sprite;

import java.util.Random;

public class ObjectSpawner implements IAlarmListener {

    private float objectsPerSecond;
    private Random random;
    private Main world;
    private Alarm alarm;
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
        alarm = new Alarm("New object", 1 / objectsPerSecond);
        alarm.addTarget(this);
        alarm.start();
    }

    public void stopAlarm() {
        alarm.stop();
    }

    /**
     * Generates a block and coin
     */
    @Override
    public void triggerAlarm(String alarmName) {
        //Get random height for both objects
        //Makes the block and the coin not be in the same block
        int rnumber0 = random.nextInt(world.getLevel().getWorldHeight()-50);
        int rnumber1 = random.nextInt(world.getLevel().getWorldHeight()-50);
        if (rnumber0 > rnumber1 && rnumber0 < rnumber1+50 || rnumber0 < rnumber1 && rnumber0 > rnumber1-50 || rnumber0 == rnumber1){
            if (rnumber1 > 360) {
                rnumber1 -= 100;
            }else{
                rnumber1 += 100;
            }
        }

        //Generate a block and show it
        Sprite blockSprite = new Sprite(world.resourcesString + "images/block.png");
        blockSprite.resize(50,50);
        GameObject block = new Block(world, blockSprite, -10);
        world.addGameObject(block, world.getLevel().getWorldWidth(), rnumber0);

        //Generate a coin and show it
        Sprite coinSprite = new Sprite(world.resourcesString + "images/coin.png");
        coinSprite.resize(200,50);
        GameObject coin = new Coin(world, coinSprite, -10);
        world.addGameObject(coin,world.getLevel().getWorldWidth(), rnumber1);

        //Start the next call for objects
        startAlarm();
    }

}