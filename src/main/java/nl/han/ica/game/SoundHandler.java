package nl.han.ica.game;

import nl.han.ica.oopg.sound.Sound;

import java.util.ArrayList;

public class SoundHandler {
    private Main main;
    private String soundsLocation;
    private Sound backgroundSound;
    private Sound coinSound;
    private Sound hitSound;
    private Sound finishSound;

    public SoundHandler(Main main, String soundsLocation) {
        this.main = main;
        this.soundsLocation = soundsLocation;
    }

    /**
     * De backgroundSound opvragen
     *
     * @return
     */
    public Sound getBackgroundSound() {
        return this.backgroundSound;
    }

    /**
     * De backgroundSound instellen
     *
     * @param soundFileLocation
     */
    public void setBackgroundSound(String soundFileLocation) {
        this.backgroundSound = new Sound(this.main, soundsLocation + soundFileLocation);
    }

    /**
     * De coinSound opvragen
     *
     * @return
     */
    public Sound getCoinSound() {
        return this.coinSound;
    }

    /**
     * De coinSound instellen
     *
     * @param soundFileLocation
     */
    public void setCoinSound(String soundFileLocation) {
        this.coinSound = new Sound(this.main, soundsLocation + soundFileLocation);
    }

    /**
     * De hitSound opvragen
     * @return
     */
    public Sound getHitSound() {
        return this.hitSound;
    }

    /**
     * De hitSound instellen
     */
    public void setHitSound(String soundFileLocation) {
        this.hitSound = new Sound(this.main, soundsLocation + soundFileLocation);
    }

    /**
     * De finishSound opvragen
     * @return
     */
    public Sound getFinishSound(){
        return this.finishSound;
    }

    /**
     * De finishSound instellen
     * @param soundFileLocation
     */
    public void setFinishSound(String soundFileLocation){
        this.finishSound = new Sound(this.main, soundsLocation + soundFileLocation);
    }
}
