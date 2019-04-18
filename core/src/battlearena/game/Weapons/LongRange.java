package battlearena.game.Weapons;

/**
 * Created by kunalsamant on 4/18/19.
 */

public class LongRange extends Weapons{

    private int ammo;
    private boolean reloadNeeded;

    public LongRange(int ammo, float damagePossible) {
        super(damagePossible);
        this.ammo = ammo;
        reloadNeeded = false;
    }


    @Override
    public void attack(){
        if (getAmmo() != 0) {
            super.attack();
            ammo--;
        }
    }

    public int getAmmo() {
        return ammo;
    }

    public boolean isReloadNeeded() {
        return reloadNeeded;
    }
}
