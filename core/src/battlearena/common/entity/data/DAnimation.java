package battlearena.common.entity.data;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import battlearena.common.entity.Entity;

public class DAnimation extends Data
{

	public Animation<TextureRegion> Value;

	public DAnimation(String Name, Entity Parent)
	{
		super(Name, Parent, false);
	}

}
