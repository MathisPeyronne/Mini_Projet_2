package ch.epfl.cs107.play.game.icwars.actor.units;

import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Soldier extends Unit {
    // Should I make Hp a class to have min and max ?
    // like this: https://stackoverflow.com/questions/8420668/setting-a-range-for-an-integer-in-java
    private float hp;
    private final String name;
    private float hitDamage = 2;
    private final int movementRadius = 2;

    public Soldier(Area area, DiscreteCoordinates coordinates, ICWarsActor.Faction faction) {
        super(area, coordinates, faction, 5, 2, "Soldier", ((faction.equals(Faction.ALLY) ? "icwars/friendlySoldier" : "icwars/enemySoldier")));
        /* unfortunately I can't put this code before the super
            String spriteName_;
            if(faction.equals(Faction.ALLY)){
                spriteName_ = "icwars/friendlySoldier";
            } else if(faction.equals(Faction.ENEMY)){
                spriteName_ = "icwars/enemySoldier";
            } else{
                System.out.println("invalide Faction type");
            }
             */
        this.name = getName();
        this.hp = getHp();
    }

    public int getRad() {
        return 2;
    }

    public int getDamage() {
        return (int) hitDamage;
    }
}

