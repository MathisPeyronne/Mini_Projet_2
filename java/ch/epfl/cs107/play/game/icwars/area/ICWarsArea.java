package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;


public abstract class ICWarsArea extends Area {

    private ICWarsBehavior behavior;
    public static final float CAMERA_SCALE_FACTOR = 10.f;

    /**
     * Create the area by adding it all actors
     * called by begin method
     * Note it set the Behavior as needed !
     */
    protected abstract void createArea();

    public float getCameraScaleFactor() {
        return CAMERA_SCALE_FACTOR;
    }

    public abstract DiscreteCoordinates getPlayerSpawnPosition();

    public abstract DiscreteCoordinates getSpawnPositionEnemy();

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            // Set the behavior map
            behavior = new ICWarsBehavior(window, getTitle());
            setBehavior(behavior);
            createArea();
            return true;
        }
        return false;
    }
}

