package battlearena.common.file;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;

import battlearena.common.tile.Tileset;
import battlearena.common.world.Cell;
import battlearena.common.world.TileLayer;
import battlearena.common.world.TiledWorld;

public class TiledWorldExporter
{

    private TiledWorld toExport;
    private DataOutputStream outputStream;
    private String d;

    /**
     * Manages the exportation of tiled worlds.
     *
     * @param toExport The TiledWorld object that should be exported.
     * @param dst The export destination of the TiledWorld as a path relative to the loc directory.
     */
    public TiledWorldExporter(TiledWorld toExport, String dst)
    {
        this.toExport = toExport;
        this.d = dst;
    }

    public TiledWorldExporter(TiledWorld toExport, DataOutputStream os)
    {
        this.toExport = toExport;
        this.outputStream = os;
        this.d = null;
    }

    public String getAbsoluteLocation()
    {
        return Gdx.files.local(d).file().getAbsolutePath();
    }

    private FileHandle getLoc(String loc)
    {
        FileHandle exportLocation = Gdx.files.local(loc);

        if(!exportLocation.parent().exists())
            exportLocation.parent().mkdirs();

        return exportLocation;
    }

    public void setTiledWorld(TiledWorld world)
    {
        toExport = world;
    }

    public void setDestination(String d)
    {
        this.outputStream = new DataOutputStream(getLoc(d).write(false));
        this.d = d;
    }

    public void exp()
    {

        if(d != null)
            this.outputStream = new DataOutputStream(getLoc(d).write(false));

        try
        {
            outputStream.writeUTF(toExport.getName());
            outputStream.writeInt(toExport.getWidth());
            outputStream.writeInt(toExport.getHeight());
            outputStream.writeInt(toExport.getLayerCount());

            // Export the tileset itself into the world file.
            // This kind of static linking is fine since the user can always change the tileset later and re export the world.
            TilesetExporter tilesetExport = new TilesetExporter(toExport.getTileset(), outputStream);
            tilesetExport.exp();

            // Export world layers
            Iterator<TileLayer> layerItr = toExport.layerIterator();
            while(layerItr.hasNext())
            {
                TileLayer next = layerItr.next();
                outputStream.writeUTF(next.getName());

                for(int x = 0; x < toExport.getWidth(); x++)
                {
                    for(int y = 0; y < toExport.getHeight(); y++)
                    {
                        Cell c = next.getCell(x, y);

                        // Write out tile ID and cell meta for that particular tile.
                        if(c.getTile() == null)
                        {
                            outputStream.writeInt(-1);
                            outputStream.writeInt(0);
                        }
                        else
                        {
                            outputStream.writeInt(c.getTile().getId());
                            outputStream.writeInt(c.getMeta());
                        }
                    }
                }
            }

            // Export default entities.

            // Export default lights (maybe entities? probably not though. they could be represented as entities?)

            outputStream.close();

            System.out.println("World successfully exported.");

        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
