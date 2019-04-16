package battlearena.common.entity.data;

import battlearena.common.entity.Entity;
import box2dLight.PointLight;

public class DPointLight extends Data
{
	
	public PointLight Value;

	public DPointLight(String Name, Entity Parent)
	{
		super(Name, Parent, false);
	}

}
