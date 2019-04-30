package battlearena.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import battlearena.common.entity.EntityConfig;
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
