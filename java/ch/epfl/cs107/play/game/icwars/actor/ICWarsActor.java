package ch.epfl.cs107.play.game.icwars.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.List;
import java.util.Collections;

public abstract class ICWarsActor extends MovableAreaEntity {
    private final Faction faction;

    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    public enum Faction {
        ALLY, ENEMY
    }

    public void enterArea(Area area, DiscreteCoordinates coordinates) {
        area.registerActor(this);
        setCurrentPosition(coordinates.toVector());
        setOwnerArea(area);
    }

    public ICWarsActor(Area area, DiscreteCoordinates coordinates, Faction _faction) {
        super(area, Orientation.UP, coordinates);
        faction = _faction;
    }

    public Faction getFaction() {
        // there shouldn't be any issue of getter intrusif here because: https://stackoverflow.com/a/8888034
        return faction;
    }

    public void leaveArea() {
        getOwnerArea().unregisterActor(this);
    }

}
