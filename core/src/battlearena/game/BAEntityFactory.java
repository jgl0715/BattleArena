package battlearena.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import battlearena.common.entity.ELight;
import battlearena.common.entity.Entity;
import battlearena.common.entity.EntityConfig;
import battlearena.common.entity.EntityFactory;
import battlearena.common.world.World;
import battlearena.game.entity.BACharacter;
import battlearena.game.entity.EArrow;
import battlearena.game.entity.EBullet;
import battlearena.game.entity.EPlayerAI;
import battlearena.game.entity.EMob;
import battlearena.game.entity.EParticle;
import battlearena.game.entity.EPlayer;

public class BAEntityFactory extends EntityFactory
{

	public static final int ENT_LIGHT = 0x00;
	public static final int ENT_PLAYER = 0x01;
	public static final int ENT_ENEMY = 0x02;

	public Entity makeEntity(World wor, int type)
	{

		if(type == ENT_LIGHT)
			return createLight(wor, 0, 0, 1, 1, 1, 10.0f, 0.1f, true);

		return null;
	}

	public static ELight createLight(World world, float x, float y, float r, float g, float b, float d, float ss, boolean pulse)
	{
		EntityConfig Config = new EntityConfig(world, ENT_LIGHT);

		Config.AddConfigItem(Entity.DATA_X, x);
		Config.AddConfigItem(Entity.DATA_Y, y);
		Config.AddConfigItem(ELight.DATA_RED, r);
		Config.AddConfigItem(ELight.DATA_GREEN, g);
		Config.AddConfigItem(ELight.DATA_BLUE, b);
		Config.AddConfigItem(ELight.DATA_DISTANCE, d);
		Config.AddConfigItem(ELight.DATA_SHADOW_SOFTNESS, ss);
		Config.AddConfigItem("Pulse", pulse);

		return new ELight(Config);
	}

	public static EPlayerAI CreateEnemy(World world, float X, float Y, BACharacter type)
	{
		EntityConfig Config = new EntityConfig(world, ENT_PLAYER);

		// Define physics body information.
		Config.AddConfigItem("Physics.BodyType", "dynamic");
		Config.AddConfigItem("Physics.Category", CollisionGroup.ENTITIES.getChannel());
		Config.AddConfigItem("Physics.Accepted", CollisionGroup.ENTITIES.getAccepted());

		// Define position.
		Config.AddConfigItem("X", X);
		Config.AddConfigItem("Y", Y);

		// Define size.
		Config.AddConfigItem("NavboxWidth", type.getNavboxWidth());
		Config.AddConfigItem("NavboxHeight", type.getNavboxHeight());
		Config.AddConfigItem("HitboxWidth", type.getHitboxWidth());
		Config.AddConfigItem("HitboxHeight", type.getHitboxHeight());

		// Define walking animation.
/*		Config.AddConfigItem("Anim.Walk.Tex", BattleArena.I.getTexture(Assets.TEXTURE_CHARACTERS));
		Config.AddConfigItem("Anim.Walk.SheetX", 0);
		Config.AddConfigItem("Anim.Walk.SheetY", 0);
		Config.AddConfigItem("Anim.Walk.SheetWidth", 5);
		Config.AddConfigItem("Anim.Walk.SheetHeight", 1);
		Config.AddConfigItem("Anim.Walk.FrameWidth", 18);
		Config.AddConfigItem("Anim.Walk.FrameHeight", 18);
		Config.AddConfigItem("Anim.Walk.Duration", 0.1f);
*/

		Config.AddConfigItem("Character", type);



		return new EPlayerAI(Config);
	}


	public static EPlayer CreatePlayer(World world, float X, float Y, BACharacter type)
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
		Config.AddConfigItem("NavboxWidth", type.getNavboxWidth());
		Config.AddConfigItem("NavboxHeight", type.getNavboxHeight());
		Config.AddConfigItem("HitboxWidth", type.getHitboxWidth());
		Config.AddConfigItem("HitboxHeight", type.getHitboxHeight());

		Config.AddConfigItem("Character", type);

		return new EPlayer(Config);
	}

	public static EParticle CreateDestructionParticle_Arrow(World world, float X, float Y, float minSpeed, float maxSpeed)
	{
		EntityConfig Config = new EntityConfig(world, ENT_PLAYER);

		// Define physics body information.
		Config.AddConfigItem("Physics.BodyType", "kinematic");
		Config.AddConfigItem("Physics.Category", CollisionGroup.PARTICLE.getChannel());
		Config.AddConfigItem("Physics.Accepted", CollisionGroup.PARTICLE.getAccepted());

		// Define position.
		Config.AddConfigItem("X", X);
		Config.AddConfigItem("Y", Y);

		// Define size.
		Config.AddConfigItem("NavboxWidth", 8);
		Config.AddConfigItem("NavboxHeight", 8);

		Config.AddConfigItem("MinLife", 0.5f);
		Config.AddConfigItem("MaxLife", 1.0f);

		Config.AddConfigItem("MinSpeed",  minSpeed);
		Config.AddConfigItem("MaxSpeed", maxSpeed);

		Config.AddConfigItem("Frame", new TextureRegion(BattleArena.I.getTexture(Assets.TEXTURE_PROJECTILES), 44, 1, 8, 8));

		return new EParticle(Config);
	}

	public static EParticle CreateDestructionParticle_Bullet(World world, float X, float Y, float minSpeed, float maxSpeed)
	{
		EntityConfig Config = new EntityConfig(world, ENT_PLAYER);

		// Define physics body information.
		Config.AddConfigItem("Physics.BodyType", "kinematic");
		Config.AddConfigItem("Physics.Category", CollisionGroup.PARTICLE.getChannel());
		Config.AddConfigItem("Physics.Accepted", CollisionGroup.PARTICLE.getAccepted());

		// Define position.
		Config.AddConfigItem("X", X);
		Config.AddConfigItem("Y", Y);

		// Define size.
		Config.AddConfigItem("NavboxWidth", 8);
		Config.AddConfigItem("NavboxHeight", 8);

		Config.AddConfigItem("MinLife", 0.5f);
		Config.AddConfigItem("MaxLife", 1.0f);

		Config.AddConfigItem("MinSpeed",  minSpeed);
		Config.AddConfigItem("MaxSpeed", maxSpeed);

		Config.AddConfigItem("Frame", new TextureRegion(BattleArena.I.getTexture(Assets.TEXTURE_PROJECTILES), 44, 10, 8, 8));

		return new EParticle(Config);
	}

	public static EArrow CreateArrow(World world, EMob shooter, float X, float Y, float velX, float velY)
	{
		EntityConfig Config = new EntityConfig(world, ENT_PLAYER);

		// Define physics body information.
		Config.AddConfigItem("Physics.BodyType", "dynamic");
		Config.AddConfigItem("Physics.Category", CollisionGroup.WEAPON.getChannel());
		Config.AddConfigItem("Physics.Accepted", CollisionGroup.WEAPON.getAccepted());
		Config.AddConfigItem("Physics.Friction", 0.0f);
		Config.AddConfigItem("Physics.LinearDamping", 0.0f);

		// Define position.
		Config.AddConfigItem("X", X);
		Config.AddConfigItem("Y", Y);

		// Define size.
		Config.AddConfigItem("NavboxWidth", 43);
		Config.AddConfigItem("NavboxHeight", 20);

		Config.AddConfigItem("VelX", velX);
		Config.AddConfigItem("VelY", velY);

		Config.AddConfigItem("Damage", 5);
		Config.AddConfigItem("Shooter", shooter);
		Config.AddConfigItem("Rotation", (float) Math.atan2(velY, velX));

		return new EArrow(Config);
	}

	public static EBullet CreateBullet(World world, EMob shooter, float X, float Y, float velX, float velY)
	{
		EntityConfig Config = new EntityConfig(world, ENT_PLAYER);

		// Define physics body information.
		Config.AddConfigItem("Physics.BodyType", "dynamic");
		Config.AddConfigItem("Physics.Category", CollisionGroup.WEAPON.getChannel());
		Config.AddConfigItem("Physics.Accepted", CollisionGroup.WEAPON.getAccepted());
		Config.AddConfigItem("Physics.Friction", 0.0f);
		Config.AddConfigItem("Physics.LinearDamping", 0.0f);

		// Define position.
		Config.AddConfigItem("X", X);
		Config.AddConfigItem("Y", Y);

		// Define size.
		Config.AddConfigItem("NavboxWidth", 8);
		Config.AddConfigItem("NavboxHeight", 8);

		Config.AddConfigItem("VelX", velX);
		Config.AddConfigItem("VelY", velY);

		Config.AddConfigItem("Damage", 5);
		Config.AddConfigItem("Shooter", shooter);
//		Config.AddConfigItem("Rotation", (float) Math.atan2(velY, velX));

		return new EBullet(Config);
	}


}
