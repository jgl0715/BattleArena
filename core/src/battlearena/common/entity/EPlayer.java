package battlearena.common.entity;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;

import box2dLight.PointLight;

public class EPlayer extends EBox
{

	private PointLight PointLight;

	public EPlayer(EntityConfig Config)
	{
		super(Config);

		Texture Tex = Config.GetConfigItem(Texture.class, "Anim.Walk.Tex");
		int SheetX = Config.GetConfigInt("Anim.Walk.SheetX");
		int SheetY = Config.GetConfigInt("Anim.Walk.SheetY");
		int SheetW = Config.GetConfigInt("Anim.Walk.SheetWidth");
		int SheetH = Config.GetConfigInt("Anim.Walk.SheetHeight");
		int FrameW = Config.GetConfigInt("Anim.Walk.FrameWidth");
		int FrameH = Config.GetConfigInt("Anim.Walk.FrameHeight");
		float Dur = Config.GetConfigNumber("Anim.Walk.Duration");

//		AddData(DAnimation.class, "WalkAnim");
//		AddData(DFloat.class, "AnimTime", false);

		// Construct the walking animation.
	//	Find(DAnimation.class, "WalkAnim").Make(Tex, SheetX, SheetY, FrameW, FrameH, SheetW, SheetH, Dur);

	//	AddBehavior(BAnimator.class, "Animator");
//		AddBehavior(BMovement.class, "Movement");

//		PointLight = ((TiledWorld) Config.getWorld()).CreatePointLight(GetPosition().x / TiledWorld.PIXELS_PER_METER, GetPosition().y / TiledWorld.PIXELS_PER_METER, 200, Color.WHITE.mul(0.6f));
//		PointLight.setSoft(true);
//		PointLight.setSoftnessLength(5f);

/*		Config.GetWorld(TiledWorld.class).AddHitListener(this, new HitListener()
		{

			@Override
			public void OnHitEnd(Entity Other)
			{
				
			}

			@Override
			public void OnHitBegin(Entity Other)
			{
				// ItemEntity Ent = (ItemEntity) Other;
				// Lev.RemoveEntity(Ent);
				//
				// Inventory.add(Ent.GetItem());
			}
		});
*/
	}

	@Override
	public void Update(float delta)
	{
		super.Update(delta);

//		Body Bod = Find(DBody.class, BODY).Value;
//		PointLight.setPosition(Bod.getPosition().x, Bod.getPosition().y);

	}

}
