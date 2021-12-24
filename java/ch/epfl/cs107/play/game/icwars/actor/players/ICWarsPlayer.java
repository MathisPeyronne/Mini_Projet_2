package ch.epfl.cs107.play.game.icwars.actor.players;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.units.Unit;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;

public abstract class ICWarsPlayer extends ICWarsActor implements Interactor {
    private float hp;
    private States currentState;
    private final ArrayList<Unit> units = new ArrayList<>();

    public ICWarsPlayer(Area area, DiscreteCoordinates coordinates, Faction faction, Unit... units) {
        super(area, coordinates, faction);
        /*ownerArea = owner;*/
        for (Unit unit : units) {
            this.units.add(unit);
            getOwnerArea().registerActor(unit);
        }

        currentState = States.IDLE;
    }

    public void setCurrentState(States currentState) {
        this.currentState = currentState;
    }

    public enum States {
        IDLE,
        NORMAL,
        SELECT_CELL,
        MOVE_UNIT,
        ACTION_SELECTION,
        ACTION
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    public void startTurn() {
        currentState = States.NORMAL;
    }

    public void resetUnits() {
        for (Unit unit : units) {
            unit.setWasUsed(false);
        }
    }


    @Override
    public void leaveArea() {
        super.leaveArea();
        for (Unit unit : units) {
            getOwnerArea().unregisterActor(unit);
        }
    }

    @Override
    public void onLeaving(List<DiscreteCoordinates> coordinates) {
        if (currentState == States.SELECT_CELL) currentState = States.NORMAL;
    }


    @Override

    public void enterArea(Area area, DiscreteCoordinates coordinates) {
        //super.enterArea(area, position);
        area.registerActor(this);
        centerCamera();
        setCurrentPosition(coordinates.toVector());
        setOwnerArea(area);
        for (Unit unit : units)
            getOwnerArea().registerActor(unit);
    }

    public States getCurrentState() {
        // there shouldn't be any issue of getter intrusif here because: https://stackoverflow.com/a/8888034
        return currentState;
    }

    public boolean isDefeated() {
        return (units.isEmpty());
    }


    public void centerCamera() {
        getOwnerArea().setViewCandidate(this);
    }


    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return null;
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor visitor) {
        visitor.interactWith(this);
    }
}
