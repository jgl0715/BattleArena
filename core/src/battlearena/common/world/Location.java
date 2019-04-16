package battlearena.common.world;

import com.badlogic.gdx.math.Vector2;

import java.awt.geom.Point2D;

public class Location
{

    private int tileX;
    private int tileY;

    public Location(int tx, int ty)
    {
        this.tileX = tx;
        this.tileY = ty;
    }

    public int getTileX()
    {
        return tileX;
    }

    public int getTileY()
    {
        return tileY;
    }

    public void setTileX(int tileX)
    {
        this.tileX = tileX;
    }

    public void setTileY(int tileY)
    {
        this.tileY = tileY;
    }

    @Override
    public int hashCode()
    {
        return new Point2D.Double(tileX, tileY).hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof Location))
            return false;

        Location l = (Location) o;

        if(l.tileX != tileX || l.tileY != tileY)
            return false;

        return true;
    }
}
