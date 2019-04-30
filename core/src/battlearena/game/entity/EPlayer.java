package battlearena.game.entity;

import com.badlogic.gdx.ai.pfa.Connection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;

import battlearena.common.RenderSettings;
import battlearena.common.entity.EBox;
import battlearena.common.entity.ELight;
import battlearena.common.entity.Entity;
import battlearena.common.entity.EntityConfig;
import battlearena.common.entity.data.DAnimation;
import battlearena.common.entity.behavior.BAnimator;
import battlearena.common.entity.data.DFloat;
import battlearena.common.entity.data.DPointLight;
import battlearena.common.entity.data.DString;
import battlearena.editor.states.StateWorldEditor;
import battlearena.game.BAEntityFactory;
import battlearena.game.LayerType;
import battlearena.game.entity.behavior.BAttack;
import battlearena.game.entity.behavior.BAttackArcher;
import battlearena.game.entity.behavior.BAttackWarrior;
import battlearena.game.entity.behavior.BAttackWizard;
import battlearena.game.entity.behavior.BController;
import battlearena.game.input.ButtonListener;
import battlearena.game.states.StatePlay;

public class EPlayer extends EMob
{

	private boolean kbCharging;

	public EPlayer(EntityConfig Config)
	{
		super(Config);

		StatePlay.I.getButtonA().addListener(new ButtonListener()
		{
			@Override
			public void buttonReleased()
			{
				attack.attack();
			}

			@Override
			public void buttonPressed()
			{
				movement.setDirection(StatePlay.I.getStick().getJoystickInput());
				attack.charge();
			}
		});
	}

	@Override
	public void Update(float delta)
	{
		super.Update(delta);

		if(!movement.isDashing())
			movement.setDirection(StatePlay.I.getStick().getJoystickInput());

		if(Gdx.input.isKeyPressed(Input.Keys.E))
		{
			kbCharging = true;
			attack.charge();
		}

		if(kbCharging  && !Gdx.input.isKeyPressed(Input.Keys.E))
		{
			attack.attack();
			kbCharging = false;
		}

	}

}
