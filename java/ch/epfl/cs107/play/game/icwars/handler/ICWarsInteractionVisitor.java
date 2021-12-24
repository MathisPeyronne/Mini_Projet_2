package ch.epfl.cs107.play.game.icwars.handler;

import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.units.Unit;

public interface ICWarsInteractionVisitor extends AreaInteractionVisitor {
    void interactWith(Unit u); // make it default? Like in the MOOC/Interfaces
}
