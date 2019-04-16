package battlearena.common.file;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.DataInputStream;
import java.io.IOException;

import battlearena.common.tile.Tile;
import battlearena.common.tile.Tileset;
import battlearena.common.world.Cell;
import battlearena.common.world.TileLayer;
import battlearena.common.world.TiledWorld;

public class TiledWorldImporter
{

    private FileHandle importFile;

    public TiledWorldImporter()
    {
    }

    public TiledWorldImporter(String src)
    {
        importFile = Gdx.files.absolute(src);
    }

    public void setImportLocation(String src)
    {
        importFile = Gdx.files.absolute(src);
    }

    public TiledWorld imp()
    {

        DataInputStream inputStream = new DataInputStream(importFile.read());

        try
        {
            String worldName = inputStream.readUTF();
            int worldWidth = inputStream.readInt();
            int worldHeight = inputStream.readInt();
            int layerCount = inputStream.readInt();

            TilesetImporter tilesetImporter = new TilesetImporter(inputStream);
            Tileset worldTileset = tilesetImporter.imp();

            TiledWorld resultWorld = new TiledWorld(worldName, worldTileset, worldWidth, worldHeight);

            for(int i = 0; i < layerCount; i++)
            {
                String layerName = inputStream.readUTF();
                TileLayer layer = new TileLayer(layerName, worldWidth, worldHeight);

                for(int x = 0; x < worldWidth; x++)
                {
                    for(int y = 0; y < worldHeight; y++)
                    {
                        int tileId = inputStream.readInt();
                        int tileMeta = inputStream.readInt();

                        Tile tile = worldTileset.getTile(tileId);
                        Cell c = layer.getCell(x, y);
                        c.setTile(tile);
                        c.setMeta(tileMeta);
                    }
                }

                // Layers should already be ordered correctly in the world definition file.
                resultWorld.addLayer(layer);
            }

            System.out.println("World successfully imported.");

            return resultWorld;

        }catch(IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

}
