package battlearena.editor;

import com.badlogic.gdx.math.Vector2;

public class CollisionMaskQuad extends CollisionMask
{

	public CollisionMaskQuad()
	{
		super(4);
	}

	public void makeBox(int w, int h)
	{
		setVertex(0, new Vector2(0, 0));
		setVertex(1, new Vector2(0, h));
		setVertex(2, new Vector2(w, h));
		setVertex(3, new Vector2(w, 0));
	}

}
