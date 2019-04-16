package battlearena.common.entity.behavior;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import battlearena.common.entity.Entity;
import battlearena.common.entity.data.DAnimation;
import battlearena.common.entity.data.DFloat;
import battlearena.common.entity.data.DTextureRegion;

public class BAnimator extends Behavior
{

	public BAnimator(String Name, Entity Parent)
	{
		super(Name, Parent);

		AddData(DFloat.class, "AnimTime");
		AddData(DAnimation.class, "WalkAnim");
		AddData(DTextureRegion.class, Entity.FRAME);
	}

	@Override
	public void Update(float delta)
	{
		super.Update(delta);

		Animation<?> Anim = Get(Animation.class, "WalkAnim");
		Object KeyFrame = Anim.getKeyFrame(Get(Float.class, "AnimTime"));

		if (KeyFrame.getClass() == TextureRegion.class)
			Set(Entity.FRAME, (TextureRegion) KeyFrame);
		else
			throw new IllegalArgumentException("Animations only support TextureRegion elements");
	
	}

}
