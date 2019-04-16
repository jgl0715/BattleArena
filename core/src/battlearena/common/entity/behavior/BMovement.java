package battlearena.common.entity.behavior;

import com.badlogic.gdx.physics.box2d.Body;

import battlearena.common.entity.Entity;
import battlearena.common.entity.data.DBody;
import battlearena.common.entity.data.DFloat;

public class BMovement extends Behavior
{

	/**
	 * Force output in Newtons.
	 */
	public static final float FORCE_OUTPUT = 400.0f;

	public BMovement(String Name, Entity Parent)
	{
		super(Name, Parent);

		AddData(DBody.class, Entity.BODY);
		AddData(DFloat.class, "AnimTime");
	}

	@Override
	public void Update(float Delta)
	{
		Body Body = Get(Body.class, Entity.BODY);

		boolean Moved = false;

		/*
		if (LD.GetInputManager().IsActionActivated(Inputs.MOVE_LEFT))
		{
			Body.applyForceToCenter(-FORCE_OUTPUT, 0, true);
			Moved = true;
			GetParent().GetRenderSettings().FlipX = true;
		}
		if (LD.GetInputManager().IsActionActivated(Inputs.MOVE_RIGHT))
		{
			Body.applyForceToCenter(FORCE_OUTPUT, 0, true);
			Moved = true;
			GetParent().GetRenderSettings().FlipX = false;
		}
		if (LD.GetInputManager().IsActionActivated(Inputs.MOVE_UP))
		{
			Body.applyForceToCenter(0, FORCE_OUTPUT, true);
			Moved = true;
		}
		if (LD.GetInputManager().IsActionActivated(Inputs.MOVE_DOWN))
		{
			Body.applyForceToCenter(0, -FORCE_OUTPUT, true);
			Moved = true;
		}*/

		if (Moved)
			Set("AnimTime", Get(Float.class, "AnimTime") + Delta);
		else
			Set("AnimTime", 0.0f);

	}

}
