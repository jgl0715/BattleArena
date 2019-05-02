package battlearena.game.entity.behavior;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import battlearena.common.entity.ELight;
import battlearena.common.entity.Entity;
import battlearena.common.world.HitListener;
import battlearena.game.Assets;
import battlearena.game.BattleArena;
import battlearena.game.CollisionGroup;
import battlearena.game.entity.EMob;
import battlearena.game.entity.EPlayer;

public class BAttackWarrior extends BAttack
{

    private Fixture swordSensorLeft;
    private Fixture swordSensorRight;

    public BAttackWarrior(String Name, Entity Parent)
    {
        super(Name, Parent);
//
//        swordSensorLeft = createSwordFixture(vertsLeft);
//        swordSensorRight = createSwordFixture(vertsRight);

        Parent.getWorld().addHitListener(new HitListener()
        {
            @Override
            public void beginHit(Vector2 point, Entity e1, Entity e2, Fixture f1, Fixture f2)
            {
                // Check if the player is current attacking.
                if(getController().isDashing())
                {
                    Entity other = null;
                    Fixture otherFixture = null;
                    Fixture thisFixture = null;

                    if (e1 == GetParent()) {
                        other = e2;
                        otherFixture = f2;
                        thisFixture = f1;
                    } else if (e2 == GetParent()) {
                        other = e1;
                        otherFixture = f1;
                        thisFixture = f2;
                    }

                    if (other != null && other instanceof EMob)
                    {
                        EMob mob = (EMob) other;

                        if(mob.getTeam() != ((EMob)GetParent()).getTeam())
                        {
                            Vector2 vel = ((EMob)GetParent()).getBody().getLinearVelocity();
                            Vector2 knockback = new Vector2(vel).scl(500);

                            if (thisFixture == swordSensorLeft && GetParent().getRenderSettings().FlipX) {

                                // Do damage here
                                mob.damage(3, knockback);
                            } else if (thisFixture == swordSensorRight && !GetParent().getRenderSettings().FlipX) {

                                // Do damage here
                                mob.damage(3, knockback);
                            }
                        }
                    }
                }
            }

            @Override
            public void endHit(Entity e1, Entity e2, Fixture f1, Fixture f2)
            {

            }
        });
    }

    @Override
    public void charge()
    {
        super.charge();
        // Charging does nothing for warrior.
        attack();

    }

    private Fixture createSwordFixture(Vector2[] verts)
    {
        Body bod = getBody();
        FixtureDef fDef = new FixtureDef();
        PolygonShape box= new PolygonShape();

        box.set(verts);

        fDef.density = 1.0f;
        fDef.friction = 0.0f;
        fDef.isSensor = true;
        fDef.restitution = 0.2f;
        fDef.shape = box;

        fDef.filter.categoryBits = CollisionGroup.WEAPON.getChannel();
        fDef.filter.groupIndex = (short) 0;
        fDef.filter.maskBits = CollisionGroup.WEAPON.getAccepted();

        return bod.createFixture(fDef);
    }

    @Override
    public void attack()
    {
        super.attack();

        if(canAttack())
        {

            BattleArena.I.playSound(Assets.AUDIO_SWORD);

            getCooldown().Value = 1.0f;
            getController().dash();

            Vector2[] vertsRight = new Vector2[]{new Vector2(2, -2),new Vector2(2, -0.5f),new Vector2(5.5f, 0.0f),new Vector2(5.5f, -2.8f)};
            Vector2[] vertsLeft = new Vector2[]{new Vector2(-2, -2),new Vector2(-2, -0.5f),new Vector2(-5.5f, 0.0f),new Vector2(-5.5f, -2.8f)};

            swordSensorLeft = createSwordFixture(vertsLeft);
            swordSensorRight = createSwordFixture(vertsRight);

        }
    }

    @Override
    public void Update(float delta)
    {
        super.Update(delta);

        ELight light = getLight();

        if(getController().isDashing())
        {
            getAnim().Value = EPlayer.DATA_ATTACK_ANIM;

            if(light != null)
            {
                Vector2 lightPos = light.getPos();
                Vector2 parentPos = getPos();
                Vector2 offset = new Vector2(50.0f*(GetParent().getRenderSettings().FlipX ? -1.0f: 1.0f), -10.0f);
                light.getRenderSettings().visible = true;
                lightPos.set(offset.add(parentPos));
                light.setColor(0.2f, 0.0f, 0.2f);
            }
        }
        else
        {
            if(light != null)
            {
                light.getRenderSettings().visible = false;
            }

            if(swordSensorLeft != null || swordSensorRight != null)
            {
                swordSensorLeft.getBody().destroyFixture(swordSensorLeft);
                swordSensorRight.getBody().destroyFixture(swordSensorRight);

                swordSensorLeft = null;
                swordSensorRight = null;
            }

            getAnim().Value = EPlayer.DATA_WALK_ANIM;
        }
    }
}
