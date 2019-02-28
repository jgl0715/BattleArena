package battlearena.common.file;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.math.Vector2;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import battlearena.editor.CollisionMask;
import battlearena.common.tile.TileDefinition;
import battlearena.common.tile.Tileset;

public class TilesetExporter
{

    private Tileset toExport;
    private FileHandle exportLocation;

    /**
     * Manages the exportation of tilesets
     *
     * @param toExport The tileset object that should be exported.
     * @param dst The export destination of the tileset as a relative path.
     */
    public TilesetExporter(Tileset toExport, String dst)
    {
        this.toExport = toExport;
        this.exportLocation = Gdx.files.local(dst);
    }

    public void setTileset(Tileset set)
    {
        toExport = set;
    }

    public void setDestination(String d)
    {
        exportLocation = Gdx.files.local(d);
    }

    public void export()
    {
        if(!exportLocation.parent().exists())
            exportLocation.parent().mkdirs();

        DataOutputStream outputStream = new DataOutputStream(exportLocation.write(false));
        TextureData tilesheetData = toExport.getTileSheet().getTextureData();
        int rgba = 0, r = 0, g = 0, b = 0, a = 0;
        Pixmap tilesheetPixmap;

        try
        {
            // File header
            outputStream.writeUTF(toExport.getName());
            outputStream.writeInt(toExport.getTilesheetWidth());
            outputStream.writeInt(toExport.getTilesheetHeight());
            outputStream.writeInt(toExport.getWidth());
            outputStream.writeInt(toExport.getHeight());
            outputStream.writeInt(toExport.getDefinitionCount());

            // Tilesheet image exported as raw bitmap into destination file
            // Get the pixmap for the tilesheet to individual pixels can be read.
            if(!tilesheetData.isPrepared())
                tilesheetData.prepare();
            tilesheetPixmap = tilesheetData.consumePixmap();

            for(int x = 0; x < toExport.getTilesheetWidth(); x++)
            {
                for(int y = 0; y < toExport.getTilesheetHeight(); y++)
                {
                    rgba = tilesheetPixmap.getPixel(x, y);
                    r = (rgba >> 24) & 0xFF;
                    g = (rgba >> 16) & 0xFF;
                    b = (rgba >> 8) & 0xFF;
                    a = (rgba >> 0) & 0xFF;

                    // Export pixel as rgba8888
                    outputStream.writeByte(r);
                    outputStream.writeByte(g);
                    outputStream.writeByte(b);
                    outputStream.writeByte(a);
                }
            }

            // Output file definitions.
            Iterator<String> definitionNames = toExport.getDefinitionNameIterator();
            while(definitionNames.hasNext())
            {
                String defName = definitionNames.next();
                TileDefinition def = toExport.getDefinition(defName);
                CollisionMask mask = def.getMask();
                List<Integer> animFrames = def.getAnimFrames();

                outputStream.writeUTF(def.getName());
                outputStream.writeByte(mask.getVertexCount());

                // Output vertex (x,y) information.
                for(int vert = 0; vert < mask.getVertexCount(); vert++)
                {
                    Vector2 v = mask.getVertex(vert);
                    outputStream.writeFloat(v.x);
                    outputStream.writeFloat(v.y);
                }

                // Output frame time for tile animation.
                outputStream.writeFloat(def.getFrameTime());

                // Output animation frame indices for tile animation.
                // This limits tile animations to 255 frames (reasonable)
                outputStream.writeByte(animFrames.size());
                for(int frame = 0; frame < animFrames.size(); frame++)
                {
                    outputStream.writeByte(animFrames.get(frame));
                }

            }

            System.out.println("Tileset has been successfully exported.");



        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

}
