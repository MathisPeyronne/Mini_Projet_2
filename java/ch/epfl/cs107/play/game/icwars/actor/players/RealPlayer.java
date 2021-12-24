package ch.epfl.cs107.play.game.icwars.actor.players;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icwars.actor.units.Unit;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.game.icwars.gui.ICWarsPlayerGUI;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;

public class RealPlayer extends ICWarsPlayer {

    private final ICWarsPlayerInteractionHandler handler = new ICWarsPlayerInteractionHandler();
    private final static int MOVE_DURATION = 8;
    private final ICWarsPlayerGUI playerGUI;
    private final Faction faction;
    private final Keyboard keyboard = getOwnerArea().getKeyboard();
    private final Sprite sprite;
    private Unit selectedUnit;

    public RealPlayer(Area area, DiscreteCoordinates coordinates, Faction _faction, Unit... units) {
        super(area, coordinates, _faction, units);
        faction = _faction;
        if (faction.equals(Faction.ENEMY)) {
            sprite = new Sprite("icwars/enemyCursor", 1f, 1f, this);
        }
        else {
            sprite = new Sprite("icwars/allyCursor", 1f, 1f, this);
        }

        playerGUI = new ICWarsPlayerGUI(14f, this);

        resetMotion();
    }

    public void updateStates() {

        switch (getCurrentState()) {

            case IDLE:
                // do nothing
                break;

            case NORMAL:
                move();
                centerCamera();
                // Always use isReleased
                if (keyboard.get(Keyboard.TAB).isReleased())
                    setCurrentState(States.IDLE);
                if (keyboard.get(Keyboard.ENTER).isReleased()) {
                    setCurrentState(States.SELECT_CELL);
                }
                break;

            case SELECT_CELL:
                move();

                if (selectedUnit != null) {
                    setCurrentState(States.MOVE_UNIT);
                }
                break;

            case MOVE_UNIT:
                move();

                if (keyboard.get(Keyboard.TAB).isReleased()) {
                    setCurrentState(States.NORMAL);
                }
                if (keyboard.get(Keyboard.ENTER).isReleased()) {
                    selectedUnit.changePosition(getCurrentMainCellCoordinates());
                    selectedUnit = null;
                    setCurrentState(States.NORMAL);
                }
                break;

            case ACTION_SELECTION:
                // ToDO in Step 3
                break;

            case ACTION:
                // ToDO in Step 3
                break;
        }
    }

    private void move() {
        moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
        moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
        moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
        moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
    }


    public void interactWith(Interactable other) {
        if (!isDisplacementOccurs() && getCurrentState() == States.SELECT_CELL) {
            other.acceptInteraction(handler);
        }
    }

    private class ICWarsPlayerInteractionHandler implements ICWarsInteractionVisitor {

        @Override
        public void interactWith(Unit u) {
            if ((u.getFaction() == faction) && (getCurrentState() == States.SELECT_CELL))
                selectedUnit = u;
            playerGUI.setSelectedUnit(selectedUnit);
        }

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        updateStates();
    }

    @Override
    public void draw(Canvas canvas) {
        if (getCurrentState() != States.IDLE) {
            sprite.draw(canvas);
            if (getCurrentState() == States.MOVE_UNIT && selectedUnit != null) {
                playerGUI.draw(canvas);
            }
        }
    }

    private void moveIfPressed(Orientation orientation, Button button) {
        if (button.isDown()) {
            if (!isDisplacementOccurs()) {
                orientate(orientation);
                move(MOVE_DURATION);
            }
        }
    }
}
