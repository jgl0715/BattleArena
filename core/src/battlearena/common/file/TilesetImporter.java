package battlearena.common.file;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.DataInput;

import java.io.DataInputStream;
import java.io.IOException;

import battlearena.common.tile.Tileset;

public class TilesetImporter
{

    private FileHandle importFile;

    public TilesetImporter(String src)
    {
        importFile = Gdx.files.local(src);
    }

    public Tileset imp()
    {
        DataInputStream inputStream = new DataInputStream(importFile.read());
        Tileset result = null;

        try {

            String name = inputStream.readUTF();
            int tilesheetWidth = inputStream.readInt();
            int tilesheetHeight = inputStream.readInt();
            int width = inputStream.readInt();
            int height = inputStream.readInt();
            int tileCount = inputStream.readInt();
            Pixmap pixelData = new Pixmap()


        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
