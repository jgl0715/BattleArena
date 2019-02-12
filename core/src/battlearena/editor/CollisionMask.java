package battlearena.editor;

import com.badlogic.gdx.math.Vector2;

public class CollisionMask
{

	private Vector2[] verts;

	public CollisionMask(int count)
	{
		verts = new Vector2[count];
	}

	public void setVertex(int vert, Vector2 vec)
	{
		verts[vert] = vec;
	}

}
