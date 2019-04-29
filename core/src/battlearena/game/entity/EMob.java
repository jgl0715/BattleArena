package battlearena.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import battlearena.common.RenderSettings;
import battlearena.common.entity.EBox;
import battlearena.common.entity.ELight;
import battlearena.common.entity.EntityConfig;
import battlearena.common.entity.behavior.BAnimator;
import battlearena.common.entity.data.DAnimation;
import battlearena.common.entity.data.DFloat;
import battlearena.common.entity.data.DString;
import battlearena.game.BAEntityFactory;
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

    public void damage(int damage, EMob damaging)
    {
        this.inflicting = damage;

        Vector2 vel = damaging.getBody().getLinearVelocity();

        System.out.println(vel);

        getBody().applyForceToCenter(new Vector2(vel).scl(500), true);

    }

    @Override
    public void Update(float delta)
    {
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
