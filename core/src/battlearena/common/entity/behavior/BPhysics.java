package battlearena.common.entity.behavior;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Transform;

import battlearena.common.entity.Entity;
import battlearena.common.entity.data.DBody;
import battlearena.common.entity.data.DFloat;
import battlearena.common.entity.data.DVector2;
import battlearena.common.world.World;

public class BPhysics extends Behavior
{

	public BPhysics(String Name, Entity Parent)
	{
		super(Name, Parent);

		AddData(DVector2.class, Entity.POSITION);
		AddData(DFloat.class, Entity.ROTATION);
		AddData(DBody.class, Entity.BODY);

	}

	@Override
	public void Update(float delta)
	{
		super.Update(delta);
		
		Transform Transform = Get(Body.class, Entity.BODY).getTransform();
		float NewX = Transform.getPosition().x * World.PIXELS_PER_METER;
		float NewY = Transform.getPosition().y * World.PIXELS_PER_METER;

		// Update components
		Get(Vector2.class, Entity.POSITION).set((int) NewX, (int) NewY);
		Set(Entity.ROTATION, Transform.getRotation());
	}

}
