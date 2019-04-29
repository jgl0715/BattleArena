package battlearena.game.entity;

import com.badlogic.gdx.graphics.g2d.Animation;

public enum BACharacter
{

    WARRIOR("Warrior", 40, 57, 20.0f), ARCHER("Archer", 40, 57, 15.0f), WIZARD("Wizard", 40, 57, 15.0f);

    private String name;
    private Animation walkAnim;
    private Animation attackAnim;
    private int width;
    private int height;
    private float speed;

    BACharacter(String n, int w, int h, float speed)
    {
        this.name = n;
        this.width = w;
        this.height=h;
        this.speed = speed;
    }

    public String getName()
    {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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
