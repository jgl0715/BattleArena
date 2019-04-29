package battlearena.game.entity.behavior;

import battlearena.common.entity.Entity;
import battlearena.game.entity.EPlayer;

public class BAttackWarrior extends BAttack
{
    public BAttackWarrior(String Name, Entity Parent)
    {
        super(Name, Parent);
    }

    @Override
    public void attack()
    {
        if(canAttack())
        {
            getController().dash();
        }
    }

    @Override
    public void Update(float delta)
    {
        super.Update(delta);

        if(getController().isDashing())
        {
            getAnim().Value = EPlayer.DATA_ATTACK_ANIM;
        }
        else
        {
            getAnim().Value = EPlayer.DATA_WALK_ANIM;
        }
    }
}
