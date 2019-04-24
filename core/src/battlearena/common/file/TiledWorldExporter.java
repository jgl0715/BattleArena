package battlearena.common.file;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.DataOutput;

import java.io.IOException;
import java.util.Iterator;

import battlearena.common.entity.Entity;
import battlearena.common.world.Cell;
import battlearena.common.world.EntityLayer;
import battlearena.common.world.TileLayer;
import battlearena.common.world.TiledWorld;

public class TiledWorldExporter
{

    private TiledWorld toExport;
    private DataOutput outputStream;
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

    public TiledWorldExporter(TiledWorld toExport, DataOutput os)
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
        this.outputStream = new DataOutput(getLoc(d).write(false));
        this.d = d;
    }

    public void exp()
    {

        if(d != null)
            this.outputStream = new DataOutput(getLoc(d).write(false));

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
            Iterator<TileLayer> layerItr = toExport.tileLayerIterator();
            while(layerItr.hasNext())
            {
                TileLayer next = layerItr.next();
                outputStream.writeUTF(next.getName());
                outputStream.writeBoolean(next.isCollisionEnabled());

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
            Iterator<EntityLayer> entLayerItr = toExport.entityLayerIterator();
            outputStream.writeInt(toExport.getEntityLayerCount());

            while(entLayerItr.hasNext())
            {
                EntityLayer next = entLayerItr.next();
                Iterator<Entity> entityItr = next.iterator();

                outputStream.writeUTF(next.getName());
                outputStream.writeInt(next.getEntityCount());

                while(entityItr.hasNext())
                {
                    Entity e = entityItr.next();
                    outputStream.writeInt(e.getConfig().getTypeId());
                    e.serialize(outputStream);
                }
            }

            outputStream.close();

            System.out.println("World successfully exported.");

        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
