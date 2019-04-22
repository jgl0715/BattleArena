package battlearena.common.file;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.DataInput;

import java.io.IOException;

import battlearena.common.entity.EntityFactory;
import battlearena.common.tile.Tile;
import battlearena.common.tile.Tileset;
import battlearena.common.world.Cell;
import battlearena.common.world.EntityLayer;
import battlearena.common.world.TileLayer;
import battlearena.common.world.TiledWorld;
import battlearena.common.entity.Entity;

public class TiledWorldImporter
{

    private FileHandle importFile;
    private EntityFactory factory;

    public TiledWorldImporter(EntityFactory factory)
    {
        this.factory = factory;
    }

    public TiledWorldImporter(String src, EntityFactory factory)
    {
        importFile = Gdx.files.absolute(src);
        this.factory = factory;
    }

    public void setImportLocation(String src)
    {
        importFile = Gdx.files.absolute(src);
    }

    public TiledWorld imp()
    {

        DataInput inputStream = new DataInput(importFile.read());

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
                TileLayer layer = new TileLayer(layerName, worldTileset, worldWidth, worldHeight);

                // No extra ordering required here,
                // layers should already be ordered correctly in the world definition file.
                resultWorld.addTileLayer(layer);

                for(int x = 0; x < worldWidth; x++)
                {
                    for(int y = 0; y < worldHeight; y++)
                    {
                        int tileId = inputStream.readInt();
                        int tileMeta = inputStream.readInt();
                        Tile tile = worldTileset.getTile(tileId);

                        resultWorld.placeTile(layerName, tile, x, y, tileMeta);
                    }
                }

            }

            int entityLayers = inputStream.readInt();

            for(int i = 0; i < entityLayers; i++)
            {
                String name = inputStream.readUTF();
                int entities = inputStream.readInt();

                EntityLayer layer = new EntityLayer(name);
                resultWorld.addEntityLayer(layer);

                for(int j = 0; j < entities; j++)
                {
                    int typeId = inputStream.readInt();
                    Entity e = factory.makeEntity(resultWorld, typeId);

                    e.deserialize(inputStream);

                    layer.addEntity(e);
                }
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
