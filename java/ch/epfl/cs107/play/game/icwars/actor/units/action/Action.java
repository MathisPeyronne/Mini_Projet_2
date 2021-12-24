package ch.epfl.cs107.play.game.icwars.actor.units.action;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.icwars.actor.units.Unit;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;


public abstract class Action implements Graphics {
    private String name;
    private int keyboardCode;
    protected Unit unit;

    /*public Action(String name, int keyboardCode, Unit unit) {
        this.name=name;
        this.keyboardCode;
        this.unit=unit;
    }*/


    public String getName() {
        return name;
    }

    public int getKeyboardCode() {
        return keyboardCode;
    }
}
