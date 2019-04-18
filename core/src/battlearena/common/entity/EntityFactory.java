package battlearena.common.entity;

import com.badlogic.gdx.graphics.Texture;

import battlearena.common.CollisionGroup;
import battlearena.common.world.World;

public class EntityFactory
{

	public static ELight createLight(World world, float x, float y, float r, float g, float b, float d, float ss)
	{
		EntityConfig Config = new EntityConfig(world);

		// Define physics body information.
		Config.AddConfigItem("Pos.X", x);
		Config.AddConfigItem("Pos.Y", y);

		Config.AddConfigItem("Size.X", 20);
		Config.AddConfigItem("Size.Y", 20);

		Config.AddConfigItem(ELight.DATA_RED, r);
		Config.AddConfigItem(ELight.DATA_GREEN, g);
		Config.AddConfigItem(ELight.DATA_BLUE, b);
		Config.AddConfigItem(ELight.DATA_DISTANCE, d);
		Config.AddConfigItem(ELight.DATA_SHADOW_SOFTNESS, d);

		return new ELight(Config);
	}


	public static EPlayer CreatePlayer(World world, float X, float Y)
	{
		EntityConfig Config = new EntityConfig(world);

		// Define physics body information.
		Config.AddConfigItem("Physics.BodyType", "dynamic");
		Config.AddConfigItem("Physics.Category", CollisionGroup.PLAYER.getChannel());
		Config.AddConfigItem("Physics.Accepted", CollisionGroup.PLAYER.getAccepted());

		// Define position.
		Config.AddConfigItem("X", X);
		Config.AddConfigItem("Y", Y);

		// Define size.
		Config.AddConfigItem("Width", 18);
		Config.AddConfigItem("Height", 18);

		// Define walking animation.
	/*	Config.AddConfigItem("Anim.Walk.Tex", LD.GetTexture(Assets.CHARACTER));
		Config.AddConfigItem("Anim.Walk.SheetX", 0);
		Config.AddConfigItem("Anim.Walk.SheetY", 0);
		Config.AddConfigItem("Anim.Walk.SheetWidth", 5);
		Config.AddConfigItem("Anim.Walk.SheetHeight", 1);
		Config.AddConfigItem("Anim.Walk.FrameWidth", 18);
		Config.AddConfigItem("Anim.Walk.FrameHeight", 18);
		Config.AddConfigItem("Anim.Walk.Duration", 0.1f);
*/
		return new EPlayer(Config);
	}

}
