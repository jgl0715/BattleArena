package battlearena.game.Weapons;

/**
 * Created by kunalsamant on 4/18/19.
 */

public class Weapons {
    protected float damagePossible;
    protected boolean isAttacking;

    public Weapons(float damagePossible) {
        this.damagePossible = damagePossible;
        isAttacking = false;
    }

    public void attack(){
        isAttacking = true;
    }

    public void use(){
        //add code but is overidden by each individual weapon
    }


    public float getDamagePossible() {
        return damagePossible;
    }
}
