package battlearena.common.tile;

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

	public int getVertexCount()
	{
		return verts.length;
	}

	public Vector2 getVertex(int vert)
	{
		return verts[vert];
	}

	public boolean isConvex()
	{
		for(int i = 0; i < verts.length; i++)
		{
			Vector2 vert = verts[i];
			Vector2 prev = verts[i == 0 ? verts.length - 1 : i - 1];
			Vector2 next = verts[(i + 1) % verts.length];

			Vector2 rayOne = prev.cpy().sub(vert);
			Vector2 rayTwo = next.cpy().sub(vert);

			float angle = rayOne.angle(rayTwo);

			if(angle < 0)
				return true;

		}

		return false;
	}

	public void makeBox(int w, int h)
	{
		verts = new Vector2[4];
		setVertex(0, new Vector2(0, 0));
		setVertex(1, new Vector2(0, h));
		setVertex(2, new Vector2(w, h));
		setVertex(3, new Vector2(w, 0));
	}


}
