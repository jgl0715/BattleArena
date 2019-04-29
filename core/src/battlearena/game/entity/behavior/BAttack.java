package battlearena.game.entity.behavior;

import com.badlogic.gdx.physics.box2d.Body;

import battlearena.common.entity.Entity;
import battlearena.common.entity.behavior.Behavior;
import battlearena.common.entity.data.DBody;
import battlearena.game.entity.BACharacter;

public abstract class BAttack extends Behavior
{

    private Body bod;
    private BACharacter type;

    public BAttack(String Name, Entity Parent)
    {
        super(Name, Parent);

        bod = GetParent().find(DBody.class, Entity.BODY).Value;
    }

    public Body getBody()
    {
        return bod;
    }

    public BACharacter getType()
    {
        return type;
    }

    public void setType(BACharacter type)
    {
        this.type = type;
    }

    public abstract void attack();

    @Override
    public void Update(float delta)
    {
        super.Update(delta);

    }
}
