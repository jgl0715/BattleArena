package battlearena.common.entity.behavior;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import battlearena.common.entity.Entity;
import battlearena.common.entity.behavior.Behavior;
import battlearena.common.entity.data.DAnimation;
import battlearena.common.entity.data.DFloat;
import battlearena.common.entity.data.DString;
import battlearena.common.entity.data.DTextureRegion;

public class BAnimator extends Behavior
{

	public BAnimator(String Name, Entity Parent)
	{
		super(Name, Parent);

		AddData(DFloat.class, Entity.ANIM_TIME);
		AddData(DString.class, Entity.ANIM);
		AddData(DTextureRegion.class, Entity.FRAME);
	}

	@Override
	public void Update(float delta)
	{
		super.Update(delta);

		Animation<?> Anim = GetParent().find(DAnimation.class, Get(String.class, Entity.ANIM)).Value;
		Object KeyFrame = Anim.getKeyFrame(Get(Float.class, Entity.ANIM_TIME));

		if (KeyFrame.getClass() == TextureRegion.class)
			Set(Entity.FRAME, KeyFrame);
		else
			throw new IllegalArgumentException("Animations only support TextureRegion elements");
	
	}

}
