package battlearena.common.entity.data;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import battlearena.common.entity.Entity;

public class DTextureRegion extends Data
{

	public TextureRegion Value;

	public DTextureRegion(String Name, Entity Parent)
	{
		super(Name, Parent, false);
	}

}
