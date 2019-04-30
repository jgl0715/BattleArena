package battlearena.game.entity.behavior;

import battlearena.common.entity.Entity;
import battlearena.game.BAEntityFactory;
import battlearena.game.entity.EArrow;
import battlearena.game.entity.EMob;
import battlearena.game.states.StatePlay;

public class BAttackArcher extends BAttack
{

    private float maxCharge;
    private float chargeTime;
    private float shootCharge;

    public BAttackArcher(String Name, Entity Parent)
    {
        super(Name, Parent);

        maxCharge = 1.5f;
    }

    @Override
    public void charge()
    {
        if(!isCharging())
        {
            chargeTime = maxCharge;
        }

        super.charge();
    }

    @Override
    public void attack()
    {
        super.attack();

        if(canAttack())
        {
            EMob parent = (EMob) GetParent();

            float arrowSpeed = 60.0f * shootCharge;

            EArrow arrow = BAEntityFactory.CreateArrow(parent.getWorld(), parent, parent.getPos().x, parent.getPos().y+15.0f, parent.getRenderSettings().FlipX ? -arrowSpeed: arrowSpeed, 0.0f);
            parent.getWorld().addEntity(StatePlay.MOBS_LAYER, arrow);
        }
    }

    @Override
    public void Update(float delta)
    {
        super.Update(delta);

        EMob shooter = (EMob) GetParent();

        if(isCharging())
        {
            getAnim().Value = EMob.DATA_ATTACK_ANIM;

            chargeTime -= delta;

            if(chargeTime < 0.0f)
            {
                attack();
            }

            shootCharge = maxCharge - chargeTime;

            // Slow down the more the archer charges
            shooter.setSpeedMultiplier(Math.min(Math.max(1.0f - shootCharge / maxCharge, 0.1f), 0.4f));
        }
        else
        {
            getAnim().Value = EMob.DATA_WALK_ANIM;
            shooter.setSpeedMultiplier(1.0f);
        }
    }
}
