package ch.epfl.cs107.play.game.icwars.actor.units;

import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;

public class Tank extends Unit {
    // Should I make Hp a class to have min and max ? like this: https://stackoverflow.com/questions/8420668/setting-a-range-for-an-integer-in-java
    private final String name;
    private float hp;
    private float MaxHp = 10;
    private float hitDamage = 7;
    private final int movementRadius = 4;

    public Tank(Area area, DiscreteCoordinates coordinates, ICWarsActor.Faction faction) {
        super(area, coordinates, faction, 10, 4, "Tank", ((faction.equals(Faction.ALLY) ? "icwars/friendlyTank" : "icwars/enemyTank")));
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
        this.hp = getHp();
        this.name = getName();
    }

    @Override
    public int getDamage() {
        return 7;
    }

    @Override
    public int getRad() {
        return 4;
    }
}
