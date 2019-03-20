package battlearena.editor;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import battlearena.common.tile.Tile;

public class TileImage extends Image
{

	private Tile def;
	private battlearena.common.tile.Tileset tileset;
	private int currentFrame;
	private float timeSinceFrameSwitch;

	public TileImage(Tile def, battlearena.common.tile.Tileset tileset) {
		this.def = def;
		this.tileset = tileset;
		this.currentFrame = 0;

		if (def.getAnimFrames().size() > 0)
		{
			TextureRegion frame = tileset.getTileRegionForId(def.getAnimationFrame(0));
			setDrawable(new TextureRegionDrawable(frame));
		}
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
