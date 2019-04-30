package battlearena.game.entity.behavior;

import com.badlogic.gdx.math.Vector2;

import battlearena.common.entity.Entity;
import battlearena.game.BAEntityFactory;
import battlearena.game.entity.EBullet;
import battlearena.game.entity.EMob;
import battlearena.game.states.StatePlay;

public class BAttackGunner extends BAttack
{

    private float shootLength;

    public BAttackGunner(String Name, Entity Parent)
    {
        super(Name, Parent);
    }

    @Override
    public void charge()
    {
        attack();
    }

    @Override
    public void attack()
    {
        super.attack();

        if(canAttack())
        {
            getCooldown().Value = 1.0f;

            EMob parent = (EMob) GetParent();

            float bulletSpeed = 100.0f;

            EBullet bullet = BAEntityFactory.CreateBullet(parent.getWorld(), parent, parent.getPos().x, parent.getPos().y+15.0f, parent.getRenderSettings().FlipX ? -bulletSpeed: bulletSpeed, 0.0f);
            parent.getWorld().addEntity(StatePlay.MOBS_LAYER, bullet);

            shootLength = 0.1f;
        }
    }

    @Override
    public void Update(float delta)
    {
        super.Update(delta);

        if(shootLength >= 0.0f)
        {
            shootLength -= delta;
            getAnim().Value = EMob.DATA_ATTACK_ANIM;
        }
        else
        {
            getAnim().Value = EMob.DATA_WALK_ANIM;
        }

    }
}
