package battlearena.common.file;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DataInput;

import java.io.DataInputStream;
import java.io.IOException;

import battlearena.common.tile.CollisionMask;
import battlearena.common.tile.Tile;
import battlearena.common.tile.Tileset;
import battlearena.game.BattleArena;

public class TilesetImporter
{

    private DataInputStream inputStream;

    public TilesetImporter()
    {
    }

    public TilesetImporter(String src, boolean internal)
    {
        setImportLocation(src, internal);
    }

    public TilesetImporter(DataInputStream inputStream)
    {
        this.inputStream = inputStream;
    }

    public void setImportLocation(String src, boolean internal)
    {
        if(internal)
        {
            inputStream = new DataInputStream(Gdx.files.internal(src).read());
        }
        else
        {
            inputStream = new DataInputStream(Gdx.files.absolute(src).read());
        }
    }

    public Tileset imp()
    {
        Tileset result = null;

        try
        {

            String name = inputStream.readUTF();
            int tilesheetWidth = inputStream.readInt();
            int tilesheetHeight = inputStream.readInt();
            int width = inputStream.readInt();
            int height = inputStream.readInt();
            int tileCount = inputStream.readInt();
            Pixmap pixelData = new Pixmap(tilesheetWidth, tilesheetHeight, Pixmap.Format.RGBA8888);

            for(int x = 0; x < tilesheetWidth; x++)
            {
                for(int y = 0; y < tilesheetHeight; y++)
                {
                    int red = inputStream.readUnsignedByte();
                    int green = inputStream.readUnsignedByte();
                    int blue = inputStream.readUnsignedByte();
                    int alpha = inputStream.readUnsignedByte();

                    int color = (red << 24) | (green << 16) | (blue << 8) | (alpha);

                    pixelData.drawPixel(x, y, color);
                }
            }

            result = new Tileset(name, width, height, new Texture(pixelData));

            // Read in the tiles.
            for(int i = 0; i < tileCount; i++)
            {
                Tile tile = new Tile();
                String tileName = inputStream.readUTF();
                int tileId = inputStream.readInt();
                int vertexCount = inputStream.read();
                CollisionMask mask = new CollisionMask(vertexCount);
                int animFrameCount;

                tile.setName(tileName);
                tile.setId(tileId);

                for(int j = 0; j < vertexCount; j++)
                {
                    float vertX = inputStream.readFloat();
                    float vertY = inputStream.readFloat();

                    mask.setVertex(j, new Vector2(vertX, vertY));
                }
                tile.setMask(mask);

                tile.setFrameTime(inputStream.readFloat());

                animFrameCount = inputStream.readUnsignedByte();
                for(int frame = 0; frame < animFrameCount; frame++)
                {
                    int frameId = inputStream.readUnsignedByte();
                    tile.addFrame(frameId);
                }

                result.addTileHasId(tile);

            }

            return result;
        } catch(IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
