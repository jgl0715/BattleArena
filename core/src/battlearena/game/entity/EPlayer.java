package battlearena.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;

import battlearena.common.RenderSettings;
import battlearena.common.entity.EBox;
import battlearena.common.entity.Entity;
import battlearena.common.entity.EntityConfig;
import battlearena.common.entity.data.DAnimation;
import battlearena.common.entity.behavior.BAnimator;
import battlearena.common.entity.data.DFloat;
import battlearena.common.entity.data.DString;
import battlearena.game.entity.behavior.BAttack;
import battlearena.game.entity.behavior.BAttackArcher;
import battlearena.game.entity.behavior.BAttackWarrior;
import battlearena.game.entity.behavior.BAttackWizard;
import battlearena.game.entity.behavior.BController;
import battlearena.game.input.ButtonListener;
import battlearena.game.states.StatePlay;

public class EPlayer extends EBox
{

	public static final String DATA_WALK_ANIM = "WalkAnim";
	public static final String DATA_ATTACK_ANIM = "AttackAnim";
	public static final String DATA_COOLDOWN = "Cooldown";

	private BController movement;

	private BACharacter character;
	private Animation walkAnim;
	private Animation attackAnim;
	private BAttack attack;

	private DString anim;
	private DFloat animTime;
	private DFloat attackCooldown;

	public EPlayer(EntityConfig Config)
	{
		super(Config);

		character = Config.GetConfigItem(BACharacter.class, "Character");
		walkAnim = character.getWalkAnim();
		attackAnim = character.getAttackAnim();

		addData(DAnimation.class, DATA_WALK_ANIM).Value = walkAnim;
		addData(DAnimation.class, DATA_ATTACK_ANIM).Value = attackAnim;
		addData(DAnimation.class, DATA_ATTACK_ANIM);
		attackCooldown = addData(DFloat.class, DATA_COOLDOWN, false);
		anim = addData(DString.class, Entity.ANIM, false);
		animTime = addData(DFloat.class, Entity.ANIM_TIME, false);

		anim.Value = DATA_WALK_ANIM;

		renderSettings.mode = RenderSettings.RenderMode.TEXTURED;

		// Add behaviors
		movement = addBehavior(BController.class, "PlayerMovement");
		addBehavior(BAnimator.class, "Animator");

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

		StatePlay.I.getButtonA().addListener(new ButtonListener()
		{
			@Override
			public void buttonPressed()
			{
				attack.attack();
			}
		});
	}

	@Override
	public void Update(float delta)
	{
		super.Update(delta);

		animTime.Value += delta;
		attackCooldown.Value -= delta;
		if(attackCooldown.Value < 0.0f)
			attackCooldown.Value = 0.0f;

		movement.setDirection(StatePlay.I.getStick().getJoystickInput());

		if(Gdx.input.isKeyPressed(Input.Keys.E))
		{
			attack.attack();
		}
		else
		{
			anim.Value = DATA_WALK_ANIM;
		}


	}

}
