package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.math.DiscreteCoordinates;


/**
 * Specific area
 */
public class Level0 extends ICWarsArea {

    @Override
    public String getTitle() {
        return "icwars/Level0";
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));
    }

    public DiscreteCoordinates getSpawnPositionEnemy() {
        return new DiscreteCoordinates(5, 5);
    }

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition() {

        return new DiscreteCoordinates(5, 5);
    }
}
