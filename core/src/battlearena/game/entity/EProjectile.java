package battlearena.game.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;

import java.util.Stack;

import battlearena.common.entity.EBox;
import battlearena.common.entity.Entity;
import battlearena.common.entity.EntityConfig;
import battlearena.common.entity.behavior.BAnimator;
import battlearena.common.entity.data.DAnimation;
import battlearena.common.entity.data.DFloat;
import battlearena.common.entity.data.DString;
import battlearena.common.world.HitListener;
import battlearena.game.BAEntityFactory;
import battlearena.game.states.StatePlay;

public abstract class EProjectile extends EBox
{

    public static final String DATA_PROJECTILE_ANIM = "ProjectileAnim";

    private float damage;
    private EMob shooter;

    protected DString anim;
    protected DAnimation projectileAnim;

    private boolean hitWall;
    private Vector2 contactPoint;

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
            public void beginHit(Vector2 point, Entity e1, Entity e2, Fixture f1, Fixture f2)
            {
                Entity other = null;
                Entity ent = null;


                if (e1 == thisEnt)
                {
                    other = e2;
                    ent = e1;
                }
                else if (e2 == thisEnt)
                {
                    other = e1;
                    ent = e2;
                }

                if(thisEnt == ent)
                {

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

                        hitWall = true;
                        contactPoint = point;
                    }
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

    public abstract EParticle createDestructionParticle(Vector2 contactPoint, float projectileSpeed);

    @Override
    public void onKill()
    {
        super.onKill();

        if(hitWall)
        {
            for(int i = 0; i < 20; i++)
            {
                float projectileSpeed = getBody().getLinearVelocity().len();
                getWorld().addEntity(StatePlay.MOBS_LAYER, createDestructionParticle(contactPoint, projectileSpeed));
            }
        }
    }

    @Override
    public void Update(float delta)
    {
        super.Update(delta);

    }
}
