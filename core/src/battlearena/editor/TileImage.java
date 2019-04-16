package battlearena.editor;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import battlearena.common.tile.Tile;
import battlearena.common.tile.Tileset;

public class TileImage extends Image
{

	private Tile tile;
	private Tileset tileset;
	private int currentFrame;
	private float timeSinceFrameSwitch;

	public TileImage()
	{
		this.tile = null;
		this.tileset = null;
		this.currentFrame = 0;
	}

	public TileImage(Tile tile, Tileset tileset)
	{
		setTile(tile, tileset);
	}

	public void setTile(Tile tile, Tileset tileset)
	{
		this.tile = tile;
		this.tileset = tileset;
		this.currentFrame = 0;

		if (tile != null && tile.getAnimFrames().size() > 0)
		{
			TextureRegion frame = tileset.getTileRegionForId(tile.getAnimationFrame(0));
			setDrawable(new TextureRegionDrawable(frame));
		}
	}

	public void act(float delta)
	{
		timeSinceFrameSwitch += delta;

		if(tile == null)
			return;

		if(tile.getAnimFrames().size() > 0 )
		{
			if (timeSinceFrameSwitch >= tile.getFrameTime())
			{
				timeSinceFrameSwitch = 0;
				currentFrame = (currentFrame + 1) % tile.getAnimFrames().size();
				TextureRegion frame = tileset.getTileRegionForId(tile.getAnimationFrame(currentFrame));
				setDrawable(new TextureRegionDrawable(frame));
			}
		}
		else
		{
			setDrawable(null);
		}

	}

}
