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

	public int getVertexCount()
	{
		return verts.length;
	}

	public Vector2 getVertex(int vert)
	{
		return verts[vert];
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
