package battlearena.editor;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class TileImage extends Image
{

	private battlearena.common.tile.TileDefinition def;
	private battlearena.common.tile.Tileset tileset;
	private int currentFrame;
	private float timeSinceFrameSwitch;

	public TileImage(battlearena.common.tile.TileDefinition def, battlearena.common.tile.Tileset tileset)
	{
		this.def = def;
		this.tileset = tileset;
		this.currentFrame = 0;
	}
	
	public void act(float delta)
	{
		timeSinceFrameSwitch += delta;

		if(def == null)
			return;

		if(def.getAnimFrames().size() > 0 )
		{
			if (timeSinceFrameSwitch >= def.getFrameTime())
			{
				timeSinceFrameSwitch = 0;
				currentFrame = (currentFrame + 1) % def.getAnimFrames().size();
				TextureRegion frame = tileset.getTileRegionForId(def.getAnimationFrame(currentFrame));
				setDrawable(new TextureRegionDrawable(frame));
			}
		}
		else
		{
			setDrawable(null);
		}

	}

}
