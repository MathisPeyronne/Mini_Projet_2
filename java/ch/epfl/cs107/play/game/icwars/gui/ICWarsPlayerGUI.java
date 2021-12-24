package ch.epfl.cs107.play.game.icwars.gui;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.game.icwars.actor.units.Unit;

public class ICWarsPlayerGUI implements Graphics {
    private final ICWarsPlayer player;
    float cameraScaleFactor;
    private DiscreteCoordinates coordinates;
    private Unit selectedUnit;

    public ICWarsPlayerGUI(float newCameraScaleFactor, ICWarsPlayer _player) {
        player = _player;
        cameraScaleFactor = newCameraScaleFactor;
    }

    public void setSelectedUnit(Unit selectedUnit) {
        this.selectedUnit = selectedUnit;
    }

    @Override
    public void draw(Canvas canvas) { //can we find a way to make it not public ?
        if (selectedUnit != null)
            selectedUnit.drawRangeAndPathTo(player.getCurrentCells().get(0), canvas);
    }
}
