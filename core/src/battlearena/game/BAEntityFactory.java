package battlearena.game;

import battlearena.common.CollisionGroup;
import battlearena.common.entity.ELight;
import battlearena.common.entity.Entity;
import battlearena.common.entity.EntityConfig;
import battlearena.common.entity.EntityFactory;
import battlearena.common.world.World;
import battlearena.game.entity.EPlayer;

public class BAEntityFactory extends EntityFactory
{

	public static final int ENT_LIGHT = 0x00;
	public static final int ENT_PLAYER = 0x01;

	public Entity makeEntity(World wor, int type)
	{

		if(type == ENT_LIGHT)
			return createLight(wor, 0, 0, 1, 1, 1, 10.0f, 0.1f);

		return null;
	}

	public static ELight createLight(World world, float x, float y, float r, float g, float b, float d, float ss)
	{
		EntityConfig Config = new EntityConfig(world, ENT_LIGHT);

		Config.AddConfigItem(Entity.DATA_X, x);
		Config.AddConfigItem(Entity.DATA_Y, y);
		Config.AddConfigItem(ELight.DATA_RED, r);
		Config.AddConfigItem(ELight.DATA_GREEN, g);
		Config.AddConfigItem(ELight.DATA_BLUE, b);
		Config.AddConfigItem(ELight.DATA_DISTANCE, d);
		Config.AddConfigItem(ELight.DATA_SHADOW_SOFTNESS, ss);

		return new ELight(Config);
	}


	public static EPlayer CreatePlayer(World world, float X, float Y)
	{
		EntityConfig Config = new EntityConfig(world, ENT_PLAYER);

		// Define physics body information.
		Config.AddConfigItem("Physics.BodyType", "dynamic");
		Config.AddConfigItem("Physics.Category", CollisionGroup.PLAYER.getChannel());
		Config.AddConfigItem("Physics.Accepted", CollisionGroup.PLAYER.getAccepted());

		// Define position.
		Config.AddConfigItem("X", X);
		Config.AddConfigItem("Y", Y);

		// Define size.
		Config.AddConfigItem("Width", 4);
		Config.AddConfigItem("Height", 4);

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
