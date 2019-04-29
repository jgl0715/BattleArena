package battlearena.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import battlearena.common.RenderSettings;
import battlearena.common.entity.EBox;
import battlearena.common.entity.ELight;
import battlearena.common.entity.EntityConfig;
import battlearena.common.entity.behavior.BAnimator;
import battlearena.common.entity.data.DAnimation;
import battlearena.common.entity.data.DBody;
import battlearena.common.entity.data.DFloat;
import battlearena.common.entity.data.DString;
import battlearena.common.tile.CollisionMask;
import battlearena.common.world.TiledWorld;
import battlearena.common.world.World;
import battlearena.game.BAEntityFactory;
import battlearena.game.CollisionGroup;
import battlearena.game.LayerType;
import battlearena.game.entity.behavior.BAttack;
import battlearena.game.entity.behavior.BAttackArcher;
import battlearena.game.entity.behavior.BAttackWarrior;
import battlearena.game.entity.behavior.BAttackWizard;
import battlearena.game.entity.behavior.BController;
import battlearena.common.entity.Entity;

public class EMob extends EBox
{

    public static final String DATA_WALK_ANIM = "WalkAnim";
    public static final String DATA_ATTACK_ANIM = "AttackAnim";
    public static final String DATA_COOLDOWN = "Cooldown";
    public static final String DATA_HITBOX = "Hitbox";
    public static final String DATA_SPEED = "Speed";

    protected BController movement;
    protected BACharacter character;
    protected Animation walkAnim;
    protected Animation attackAnim;
    protected BAttack attack;

    protected DString anim;
    protected DFloat animTime;
    protected DFloat attackCooldown;
    protected DFloat speed;
    protected Body hitbox;

    protected ELight attackLight;

    private float inflicting;

    public EMob(EntityConfig Config)
    {
        super(Config);

        character = Config.GetConfigItem(BACharacter.class, "Character");
        walkAnim = character.getWalkAnim();
        attackAnim = character.getAttackAnim();

        addData(DAnimation.class, DATA_WALK_ANIM).Value = walkAnim;
        addData(DAnimation.class, DATA_ATTACK_ANIM).Value = attackAnim;
        attackCooldown = addData(DFloat.class, DATA_COOLDOWN, false);
        anim = addData(DString.class, Entity.ANIM, false);
        animTime = addData(DFloat.class, Entity.ANIM_TIME, false);
        speed = addData(DFloat.class, DATA_SPEED, false);
        hitbox = createHitbox(character.getHitboxWidth(), character.getHitboxHeight());

        anim.Value = DATA_WALK_ANIM;
        speed.Value = character.getSpeed();

        renderSettings.mode = RenderSettings.RenderMode.TEXTURED;

        // Add behaviors
        movement = addBehavior(BController.class, "PlayerMovement");
        addBehavior(BAnimator.class, "Animator");

        attackLight = BAEntityFactory.createLight(Config.getWorld(), 0, 0, 1.0f, 0.0f, 1.0f, 20.0f, 0.0f);
        attackLight.getRenderSettings().visible = false;
        Config.getWorld().addEntity(LayerType.LIGHTS.getName(), attackLight);

        switch(character)
        {
            case WARRIOR:
                attack = addBehavior(BAttackWarrior.class, "Attack");
                break;
            case ARCHER:
                attack = addBehavior(BAttackArcher.class, "Attack");
                break;
            case WIZARD:
                attack = addBehavior(BAttackWizard.class, "Attack");
                break;
        }

        attack.setType(character);
        attack.setLight(attackLight);
    }

    private Body createHitbox(float w, float h)
    {
        BodyDef bDef = new BodyDef();

        bDef.active = true;
        bDef.allowSleep = false;
        bDef.angle = 0.0f;
        bDef.angularDamping = 0.0f;
        bDef.angularVelocity = 0.0f;
        bDef.awake = true;
        bDef.bullet = false;
        bDef.fixedRotation = true;
        bDef.gravityScale = 1.0f;
        bDef.linearDamping = 0.0f;
        bDef.linearVelocity.set(0, 0);
        bDef.position.set(getBody().getPosition());

        // Check for type in configuration properties.
        bDef.type = BodyDef.BodyType.DynamicBody;

        Body bod = getWorld().getPhysicsWorld().createBody(bDef);

        bod.setUserData(this);

        FixtureDef fDef = new FixtureDef();
        PolygonShape polygon = new PolygonShape();
        polygon.setAsBox((w / 2) / World.PIXELS_PER_METER, (h / 2) / World.PIXELS_PER_METER);

        fDef.density = 0.0f;
        fDef.friction = 0.0f;
        fDef.isSensor = true;
        fDef.restitution = 0.0f;
        fDef.shape = polygon;

        fDef.filter.categoryBits = CollisionGroup.HITBOX.getChannel();
        fDef.filter.groupIndex = (short) 0;
        fDef.filter.maskBits = CollisionGroup.HITBOX.getAccepted();

        bod.createFixture(fDef);

        return bod;
    }

    public Body getHitbox()
    {
        return hitbox;
    }

    public void damage(float damage, EMob damaging)
    {
        this.inflicting = damage;

        Vector2 vel = damaging.getBody().getLinearVelocity();

        System.out.println(vel);

        getBody().applyForceToCenter(new Vector2(vel).scl(500), true);

    }

    @Override
    public void Update(float delta)
    {

        // Set the hitbox sensor to the correct location.
        float hbHeight = getConfig().GetConfigNumber("HitboxHeight");
        float nbHeight = getConfig().GetConfigNumber("NavboxHeight");

        hitbox.setTransform(getBody().getPosition().x,getBody().getPosition().y + (hbHeight-nbHeight)/(2*World.PIXELS_PER_METER), 0);

        super.Update(delta);

        animTime.Value += delta;
        attackCooldown.Value -= delta;
        if(attackCooldown.Value < 0.0f)
            attackCooldown.Value = 0.0f;

        // Lower health here
        inflicting -= delta;

        if(inflicting < 0.0f)
            inflicting = 0.0f;

    }

    @Override
    public void Render(SpriteBatch batch, OrthographicCamera cam, RenderSettings.RenderMode filter)
    {
        if(inflicting >= 0.1f)
        {
            if((int) (inflicting*5) % 2 == 0)
            {
                batch.setColor(Color.RED);
            }
            else
            {
                batch.setColor(Color.WHITE);
            }
        }

        super.Render(batch, cam, filter);

        batch.setColor(Color.WHITE);
    }
}
