package battlearena.editor;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class TileImage extends Image
{

	private TileDefinition def;
	private Tileset tileset;
	private int currentFrame;
	private float timeSinceFrameSwitch;

	public TileImage(TileDefinition def, Tileset tileset)
	{
		this.def = def;
		this.tileset = tileset;
		this.currentFrame = 0;
	}
	
	public void act(float delta)
	{
		timeSinceFrameSwitch += delta;

		if (def.getAnimFrames().size() > 0 && timeSinceFrameSwitch >= def.getFrameTime())
		{
			currentFrame = (currentFrame + 1) % def.getAnimFrames().size();
			TextureRegion frame = tileset.getTileRegionForId(currentFrame);
			setDrawable(new TextureRegionDrawable(frame));
		}

	}

}
