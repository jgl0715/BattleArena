package battlearena.game.entity;

import com.badlogic.gdx.graphics.g2d.Animation;

public enum BACharacter
{

    WARRIOR("Warrior", 40, 57, 40, 10, 20.0f), ARCHER("Archer", 37, 59, 37, 10, 15.0f), WIZARD("Wizard", 40, 57, 0, 0, 15.0f);

    private String name;
    private Animation walkAnim;
    private Animation attackAnim;
    private int hitboxWidth;
    private int hitboxHeight;
    private int navboxWidth;
    private int navboxHeight;
    private float speed;

    BACharacter(String n, int hbWidth, int hbHeight, int nmWidth, int nmHeight, float speed)
    {
        this.name = n;
        this.hitboxWidth = hbWidth;
        this.hitboxHeight = hbHeight;
        this.navboxWidth = nmWidth;
        this.navboxHeight= nmHeight;
        this.speed = speed;
    }

    public String getName()
    {
        return name;
    }

    public int getHitboxWidth()
    {
        return hitboxWidth;
    }

    public int getHitboxHeight()
    {
        return hitboxHeight;
    }

    public int getNavboxWidth()
    {
        return navboxWidth;
    }

    public int getNavboxHeight()
    {
        return navboxHeight;
    }

    public float getSpeed() {
        return speed;
    }

    public Animation getWalkAnim()
    {
        return walkAnim;
    }

    public Animation getAttackAnim()
    {
        return attackAnim;
    }

    public void setWalkAnim(Animation walkAnim)
    {
        this.walkAnim = walkAnim;
    }

    public void setAttackAnim(Animation attackAnim)
    {
        this.attackAnim = attackAnim;
    }
}
