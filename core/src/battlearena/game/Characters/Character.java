package battlearena.game.Characters;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by kunalsamant on 4/16/19.
 */

public class Character {
    protected boolean left, right, down, up;
    protected float health;
    protected float manna;
    protected float money;
    protected boolean isAlive;
    //protected float armor;
   // public float damage;

    // use constructor as a a ase for all characters, parameters are health and manna
    public Character(float health, float manna) {
        this.health = health;
        money = 0;
        this.manna = manna;
        isAlive = true;
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

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isDown() {
        return down;
    }

    public boolean isUp() {
        return up;
    }

    public float getHealth() {
        return health;
    }
}
