package battlearena.game.entity.behavior;

import com.badlogic.gdx.physics.box2d.Body;

import battlearena.common.entity.Entity;
import battlearena.common.entity.behavior.Behavior;
import battlearena.common.entity.data.DAnimation;
import battlearena.common.entity.data.DBody;
import battlearena.common.entity.data.DFloat;
import battlearena.common.entity.data.DString;
import battlearena.game.entity.BACharacter;
import battlearena.game.entity.EPlayer;

public abstract class BAttack extends Behavior
{

    private Body bod;
    private DString anim;
    private DFloat cooldown;
    private BACharacter type;
    private BController controller;

    public BAttack(String Name, Entity Parent)
    {
        super(Name, Parent);

        bod = GetParent().find(DBody.class, Entity.BODY).Value;
        anim = GetParent().find(DString.class, Entity.ANIM);
        cooldown = GetParent().find(DFloat.class, EPlayer.DATA_COOLDOWN);
        controller = GetParent().find(BController.class, "PlayerMovement");
    }

    public Body getBody()
    {
        return bod;
    }

    public DString getAnim()
    {
        return anim;
    }

    public BController getController()
    {
        return controller;
    }

    public DFloat getCooldown()
    {
        return cooldown;
    }

    public BACharacter getType()
    {
        return type;
    }

    public void setType(BACharacter type)
    {
        this.type = type;
    }

    public boolean canAttack()
    {
        if(cooldown.Value > 0.01f)
            return false;
        else
            return true;

    }

    public abstract void attack();

    @Override
    public void Update(float delta)
    {
        super.Update(delta);

    }
}
