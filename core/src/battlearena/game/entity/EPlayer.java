package battlearena.game.entity;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;

import battlearena.common.RenderSettings;
import battlearena.common.entity.EBox;
import battlearena.common.entity.EntityConfig;
import battlearena.game.entity.behavior.BMovement;
import box2dLight.PointLight;

public class EPlayer extends EBox
{

	public EPlayer(EntityConfig Config)
	{
		super(Config);

		addBehavior(BMovement.class, "PlayerMovement");
		renderSettings.mode = RenderSettings.RenderMode.FRAME;
	}

	@Override
	public void Update(float delta)
	{
		super.Update(delta);
	}

}
