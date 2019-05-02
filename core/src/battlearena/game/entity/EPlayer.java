package battlearena.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import battlearena.common.entity.EntityConfig;
import battlearena.game.input.Button;
import battlearena.game.input.ButtonListener;
import battlearena.game.input.Joystick;
import battlearena.game.states.StatePlay;

public class EPlayer extends EMob
{

	private boolean kbCharging;
	private Joystick stick;

	public EPlayer(EntityConfig Config)
	{
		super(Config);
	}

	@Override
	public void Update(float delta)
	{
		super.Update(delta);

		if(!movement.isDashing())
			movement.setDirection(stick.getJoystickInput());

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

	public void initInput(final Joystick stick, final Button aButton, final Button bButton)
	{
		this.stick = stick;

		aButton.addListener(new ButtonListener()
		{
			@Override
			public void buttonReleased()
			{
				attack.attack();
			}

			@Override
			public void buttonPressed()
			{
				movement.setDirection(stick.getJoystickInput());
				attack.charge();
			}
		});
	}

}
