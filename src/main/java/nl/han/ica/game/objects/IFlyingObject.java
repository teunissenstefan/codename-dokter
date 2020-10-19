package nl.han.ica.game.objects;

public interface IFlyingObject  {
    /**
     * Wat er moet gebeuren als het object aangeraakt wordt door de player
     */
    void hit();

    /**
     * De condities voor wanneer het obejct verwijderdt moet worden
     */
    void checkIfOffScreen();
}
