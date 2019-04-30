package battlearena.game.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;

import battlearena.common.entity.EBox;
import battlearena.common.entity.Entity;
import battlearena.common.entity.EntityConfig;
import battlearena.common.entity.behavior.BAnimator;
import battlearena.common.entity.data.DAnimation;
import battlearena.common.entity.data.DFloat;
import battlearena.common.entity.data.DString;
import battlearena.common.world.HitListener;

public class EProjectile extends EBox
{

    public static final String DATA_PROJECTILE_ANIM = "ProjectileAnim";

    private float damage;
    private EMob shooter;

    protected DString anim;
    protected DAnimation projectileAnim;

    public EProjectile(EntityConfig Config)
    {
        super(Config);

        addData(DFloat.class, Entity.ANIM_TIME, false);
        anim = addData(DString.class, Entity.ANIM, false);
        anim.Value = DATA_PROJECTILE_ANIM;
        projectileAnim = addData(DAnimation.class, DATA_PROJECTILE_ANIM);

        float velX = Config.GetConfigNumber("VelX");
        float velY = Config.GetConfigNumber("VelY");

        damage = Config.GetConfigNumber("Damage");
        shooter = (EMob) Config.GetConfigItem("Shooter");

        getBody().setLinearVelocity(velX, velY);

        final Entity thisEnt = this;

        Config.getWorld().addHitListener(new HitListener()
        {
            @Override
            public void beginHit(Entity e1, Entity e2, Fixture f1, Fixture f2)
            {
                Entity other = null;

                if (e1 == thisEnt)
                    other = e2;
                else if (e2 == thisEnt)
                    other = e1;

                if (other != null)
                {
                    if(other instanceof EMob && other != shooter)
                    {
                        // Inflict damage
                        EMob mob = (EMob) other;
                        Vector2 knockback = new Vector2(getBody().getLinearVelocity()).scl(80);

                        mob.damage(damage, knockback);

                        remove();

                        // Emit entity damage particles here.
                    }
                }
                else
                {
                    // We hit a tile, just remove this entity.
                    remove();

                    // Emit wall destruction particles here.
                }
            }

            @Override
            public void endHit(Entity e1, Entity e2, Fixture f1, Fixture f2)
            {

            }
        });

        addBehavior(BAnimator.class, "Animator");
    }

    public float getDamage()
    {
        return damage;
    }

    @Override
    public void Update(float delta)
    {
        super.Update(delta);
    }
}
