package battlearena.game.entity;

import com.badlogic.gdx.graphics.g2d.Animation;

public enum BACharacter
{

    WARRIOR("Warrior"), ARCHER("Archer"), WIZARD("Wizard");

    private String name;
    private Animation walkAnim;
    private Animation attackAnim;

    BACharacter(String n)
    {
        this.name = n;
    }

    public String getName()
    {
        return name;
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
