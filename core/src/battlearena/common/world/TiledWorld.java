package battlearena.common.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import battlearena.common.tile.Tileset;
import battlearena.editor.WorldEditor;

public class TiledWorld extends World
{

    private Tileset tileset;

    private Map<String, TileLayer> layers;
    private List<TileLayer> layersOrdered;

    private int width;
    private int height;

    private float delta;

    public TiledWorld(String name, Tileset tileset, int width, int height)
    {
        super(name);
        this.tileset = tileset;
        this.width = width;
        this.height = height;

        this.layers = new HashMap<String, TileLayer>();
        this.layersOrdered = new ArrayList<TileLayer>();
    }

    public int getPixelWidth()
    {
        return width * tileset.getTileWidth();
    }

    public int getPixelHeight()
    {
        return height * tileset.getTileHeight();
    }

    public Tileset getTileset()
    {
        return tileset;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int getLayerCount()
    {
        return layers.size();
    }

    public TileLayer getLayer(String name)
    {
        return layers.get(name);
    }

    public boolean layerExists(String name)
    {
        return layers.containsKey(name);
    }

    public void addLayer(TileLayer layer)
    {
        layers.put(layer.getName(), layer);

        layersOrdered.add(layer);
    }

    public void removeLayer(String layerName)
    {
        layers.remove(layerName);
        layersOrdered.remove(layerName);
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);

        this.delta += delta;
    }

    @Override
    public void render()
    {
        OrthographicCamera camera = WorldEditor.I.getCamera();
        SpriteBatch spriteBatch = WorldEditor.I.getBatch();

        // Render tile layers in correct order.
        spriteBatch.begin();
        {
            for(int l = 0; l < layersOrdered.size(); l++)
            {
                TileLayer layer = layersOrdered.get(l);
                layer.render(spriteBatch, delta, tileset);
            }
        }
        spriteBatch.end();

        // Render entities and lights on top of tiles.
        super.render();
    }
}
