package ch.epfl.cs107.play.game.icwars.actor.units;

import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.game.areagame.actor.Path;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.area.ICWarsRange;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;

import java.util.Queue;

abstract public class Unit extends ICWarsActor {

    private final int HPMAX;
    private final String name;
    private ICWarsRange range;
    private boolean wasUsed;
    private final Sprite sprite;
    private float hp;
    private final int radius;


    public Unit(Area area, DiscreteCoordinates coordinates, Faction faction, int _HPMAX, int rad, String _name, String spriteName) {
        super(area, coordinates, faction);
        sprite = new Sprite(spriteName, 1.5f, 1.5f, this, null, new Vector(-0.25f, -0.25f));
        range = new ICWarsRange();
        HPMAX = _HPMAX;
        radius = rad;
        name = _name;
        setRange(coordinates);
    }

    @Override
    public void draw(Canvas c) {
        sprite.draw(c);
        if (wasUsed) {
            sprite.setAlpha(0.50f);
        } else {
            sprite.setAlpha(1f);
        }
    }

    public float getHp() {
        return hp;
    }

    public String getName() {
        return name;
    }

    public void setWasUsed(boolean wasUsed) {
        this.wasUsed = wasUsed;
    }

    @Override
    public boolean changePosition(DiscreteCoordinates newPosition) {
        if (!super.changePosition(newPosition) || !range.nodeExists(newPosition)) {
            return false;
        }
        setWasUsed(true);
        range = new ICWarsRange();
        setRange(newPosition);
        return true;
    }

    /**
     * Draw the unit's range and a path from the unit position to
     * destination
     *
     * @param destination path destination
     * @param canvas      canvas
     */
    public void drawRangeAndPathTo(DiscreteCoordinates destination, Canvas canvas) {
        range.draw(canvas);
        Queue<Orientation> path = range.shortestPath(getCurrentMainCellCoordinates(), destination);
        if (path != null)
            new Path(getCurrentMainCellCoordinates().toVector(), path).draw(canvas);
    }

    private boolean isInMapHelper(int x, int y, int width, int height) {
        return (x >= 0 && x < width && y >= 0 && y < height);
    }

    private boolean isInDistanceHelper(int x, int y, int fromX, int fromY, int movementRadius) {
        return (x >= fromX - movementRadius && x <= fromX + movementRadius && y >= fromY - movementRadius && y <= fromY + movementRadius);
    }


    public void setRange(DiscreteCoordinates coordinates) {
        int fromX = coordinates.x; //unitCoordiantes.x;
        int fromY = coordinates.y; //unitCoordiantes.y;
        int movementRadius = radius;
        boolean hasLeftEdge, hasUpEdge, hasRightEdge, hasDownEdge;
        Area ownerArea = getOwnerArea();
        int width = ownerArea.getWidth();
        int height = ownerArea.getHeight();
        for (int i = fromX - movementRadius; i <= movementRadius + fromX; i++) { // i= x coord, j = y coord
            for (int j = fromY - movementRadius; j <= movementRadius + fromY; j++) {
                if (isInMapHelper(i, j, width, height)) { //ignore all the cells that are outside of the map
                    if (isInDistanceHelper(i, j, fromX, fromY, movementRadius)) { // ignore all cells not in the radius

                        hasRightEdge = isInMapHelper(i + 1, j, width, height) && isInDistanceHelper(i + 1, j, fromX, fromY, movementRadius);
                        hasLeftEdge = isInMapHelper(i - 1, j, width, height) && isInDistanceHelper(i - 1, j, fromX, fromY, movementRadius);
                        hasUpEdge = isInMapHelper(i, j + 1, width, height) && isInDistanceHelper(i, j + 1, fromX, fromY, movementRadius);
                        hasDownEdge = isInMapHelper(i, j - 1, width, height) && isInDistanceHelper(i, j - 1, fromX, fromY, movementRadius);

                        range.addNode(new DiscreteCoordinates(i, j), hasLeftEdge, hasUpEdge, hasRightEdge, hasDownEdge);
                    }
                }
                //System.out.println(i + " : " + j + " : " + hasLeftEdge +  " : " + hasUpEdge +  " : " + hasRightEdge  + " : " + hasDownEdge);
            }
        }
    }


    public abstract int getDamage();


    public abstract int getRad();

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        if (!wasUsed)
            ((ICWarsInteractionVisitor) v).interactWith(this);
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }
}
