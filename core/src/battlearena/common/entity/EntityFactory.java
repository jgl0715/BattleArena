package battlearena.common.entity;

import com.badlogic.gdx.graphics.Texture;

import battlearena.common.CollisionGroup;
import battlearena.common.world.World;

public class EntityFactory
{

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
