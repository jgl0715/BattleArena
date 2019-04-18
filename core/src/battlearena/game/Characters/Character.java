package battlearena.game.Characters;

import com.badlogic.gdx.math.Vector2;

import battlearena.game.Controller;

/**
 * Created by kunalsamant on 4/16/19.
 */

public class Character {
    //protected boolean left, right, down, up;
    protected float health, base_health;
    protected float manna;
    protected float money;
    protected boolean isAlive;
    protected float armor;


    // use constructor as a a ase for all characters, parameters are health and manna
    public Character(float health, float manna, float armor) {
        this.base_health = health;
        this.health = health;
        money = 0;
        this.manna = manna;
        isAlive = true;
        this.armor = armor;
    }

    public void heal(){
        if (this.health == 0)
        {
            this.health += base_health/100;
        }
    }

    public void loseHealth(float damageDone) {
        if (this.health != 0)
        {
            this.health -= (damageDone - armor);
        }
    }

    public void die(){
        isAlive = false;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public float getMoney() {
        return money;
    }

    public float getManna() {
        return manna;
    }

    public float getHealth() {
        return health;
    }

    public float getArmor() {
        return armor;
    }
}
