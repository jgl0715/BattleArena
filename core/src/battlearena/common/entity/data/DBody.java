package battlearena.common.entity.data;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import battlearena.common.entity.Entity;
import battlearena.common.entity.EntityConfig;
import battlearena.common.world.TiledWorld;
import battlearena.common.world.World;

public class DBody extends Data
{

	public Body Value;

	public DBody(String Name, Entity Parent)
	{
		super(Name, Parent, false);

		EntityConfig EntityConf = Parent.getConfig();

		BodyDef bDef = new BodyDef();

		bDef.active = true;
		bDef.allowSleep = false;
		bDef.angularDamping = 0.0f;
		bDef.angularVelocity = 0.0f;
		bDef.awake = true;
		bDef.bullet = false;
		bDef.gravityScale = 1.0f;
		bDef.linearDamping = 0.0f;
		bDef.linearVelocity.set(0, 0);
		bDef.position.set(EntityConf.GetConfigNumber("X") / TiledWorld.PIXELS_PER_METER, EntityConf.GetConfigNumber("Y") / TiledWorld.PIXELS_PER_METER);

		if(EntityConf.HasItem("Rotation"))
		{
			bDef.fixedRotation = false;
			bDef.angle = EntityConf.GetConfigNumber("Rotation");
		}
		else
		{
			bDef.fixedRotation = true;
		}
		
		// Check for type in configuration properties.
		String Type = EntityConf.GetConfigString("Physics.BodyType");
		short Category = EntityConf.GetConfigShort("Physics.Category");
		short Accepted = EntityConf.GetConfigShort("Physics.Accepted");
		
		if (Type.equalsIgnoreCase("static"))
			bDef.type = BodyType.StaticBody;
		else if (Type.equalsIgnoreCase("dynamic"))
			bDef.type = BodyType.DynamicBody;
		else if (Type.equalsIgnoreCase("kinematic"))
			bDef.type = BodyType.KinematicBody;

		Value = Parent.getWorld().getPhysicsWorld().createBody(bDef);

		Value.setUserData(GetParent());

		CreateBoxFixture(EntityConf.GetConfigNumber("NavboxWidth"), EntityConf.GetConfigNumber("NavboxHeight"), Category, Accepted);
	}

	public void CreateBoxFixture(float w, float h, short Cat, short Accepted)
	{
		FixtureDef fDef = new FixtureDef();
		PolygonShape polygon = new PolygonShape();

		polygon.setAsBox((w / 2) / World.PIXELS_PER_METER, (h / 2) / World.PIXELS_PER_METER);

		fDef.density = 0.0f;
		fDef.friction = 0.0f;
		fDef.isSensor = false;
		fDef.restitution = 0.0f;
		fDef.shape = polygon;

		fDef.filter.categoryBits = Cat;
		fDef.filter.groupIndex = (short) 0;
		fDef.filter.maskBits = Accepted;

		Value.createFixture(fDef);
	}

}
