package battlearena.common.tile;

import java.util.ArrayList;
import java.util.List;

public class Tile
{

	private int id;
	private String name;
	private List<Integer> animFrames;
	private float frameTime;
	private CollisionMask mask;

	public Tile()
	{
		this.id = -1;
		this.name = "Unnamed";
		this.frameTime = 0.2f;
		animFrames = new ArrayList<Integer>();
		mask = new CollisionMask(4);
	}

	public String getName()
	{
		return name;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<Integer> getAnimFrames()
	{
		return animFrames;
	}

	public int getAnimationFrame(int index)
	{
		return animFrames.get(index);
	}

	public void addFrame(int frame)
	{
		animFrames.add(frame);
	}

	public void removeFrame(int frame)
	{
		animFrames.remove((Integer) frame);
	}

	public float getFrameTime()
	{
		return frameTime;
	}

	public void setFrameTime(float frameTime)
	{
		this.frameTime = frameTime;
	}

	public CollisionMask getMask()
	{
		return mask;
	}

	public void setMask(CollisionMask mask)
	{
		this.mask = mask;
	}

}
