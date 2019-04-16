package battlearena.common;

public class RenderSettings
{

	public boolean visible;
	public RenderMode mode;
	public boolean FlipX;
	public boolean FlipY;

	public enum RenderMode
	{
		TEXTURED, FRAME
	}

	public RenderSettings()
	{
		visible = true;
		mode = RenderMode.FRAME;
		FlipX = false;
		FlipY = false;
	}

}
