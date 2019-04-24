package battlearena.common.world;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import battlearena.common.tile.Tile;
import battlearena.common.tile.Tileset;

public class Cell
{

    private Body bod;
    private Tile tile;
    private int meta;

    public Cell()
    {
        this.meta = 0;
    }

    public Cell(Body bod, Tile tile)
    {
        this.bod = bod;
        this.tile = tile;
        this.meta = 0;
    }

    public void metaOrWith(int bits)
    {
        meta |= bits;
    }

    public Body getBody()
    {
        return bod;
    }

    public Tile getTile()
    {
        return tile;
    }

    public int getMeta()
    {
        return meta;
    }

    public void setTile(Tile tile)
    {
        this.tile = tile;
    }

    public void setBody(Body bod)
    {
        this.bod = bod;
    }

    public void setMeta(int meta)
    {
        this.meta = meta;
    }

    public TextureRegion getFrame(float delta, Tileset set)
    {

        if(tile == null || tile.getAnimFrames().size() < 1)
            return null;

        int animFrameIndex = ((int)(delta / tile.getFrameTime())) % tile.getAnimFrames().size();
        int animFrame = tile.getAnimationFrame(animFrameIndex);

        return set.getTileRegionForId(animFrame);
    }
}
