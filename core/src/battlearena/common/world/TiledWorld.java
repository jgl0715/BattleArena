package battlearena.common.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import battlearena.common.CollisionGroup;
import battlearena.common.tile.CollisionMask;
import battlearena.common.tile.Tile;
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

    public void changeWidth(int amount)
    {
        if(amount < 0 && -amount >= width)
            return;

        width += amount;

        for(TileLayer layer : layersOrdered)
            layer.changeWidth(amount);
    }


    public void changeHeight(int amount)
    {
        if(amount < 0 && -amount >= height)
            return;

        height += amount;

        for(TileLayer layer : layersOrdered)
            layer.changeHeight(amount);
    }

    public boolean isLocInBounds(int tx, int ty)
    {
        return tx >= 0 && ty >= 0 && tx < width && ty < height;
    }

    public boolean isLocInBounds(Location loc)
    {
        return loc.getTileX() >= 0 && loc.getTileY() >= 0 && loc.getTileX() < width && loc.getTileY() < height;
    }

    public Set<Location> floodSearch(String layer, int tx, int ty)
    {
        Set<Location> results = new HashSet<Location>();
        Set<Location> visited = new HashSet<Location>();

        if(isLocInBounds(tx, ty))
            floodSearch(layer, getTile(layer, tx, ty), results, visited, tx, ty);

        return results;
    }

    private void floodSearch(String layer, Tile floodTile, Set<Location> results, Set<Location> visited, int tx, int ty)
    {
        Location loc = new Location(tx, ty);

        // Don't repeat visit locations.
        if(visited.contains(loc) || !isLocInBounds(loc))
            return;

        Tile currentTile = getTile(layer, loc);

        if(currentTile != floodTile)
            return;

        visited.add(loc);

        if(currentTile == floodTile)
        {
            results.add(loc);
        }

        floodSearch(layer, floodTile, results, visited, tx - 1, ty);
        floodSearch(layer, floodTile, results, visited, tx + 1, ty);
        floodSearch(layer, floodTile, results, visited, tx, ty + 1);
        floodSearch(layer, floodTile, results, visited, tx, ty - 1);
    }

    public Iterator<TileLayer> layerIterator()
    {
        return layersOrdered.iterator();
    }

    public void changeTileset(Tileset set)
    {
        this.tileset = set;
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

    public void removeTile(String layerName, Location loc)
    {
        removeTile(layerName, loc.getTileX(), loc.getTileY());
    }

    public void removeTile(String layerName, int x, int y)
    {
        TileLayer layer = layers.get(layerName);
        Cell cell = layer.getCell(x, y);
        Body prevBody = cell.getBody();
        Tile prev = layer.placeTile(null, x, y);

        // Remove previous tile body
        if(prev != null)
        {
            getPhysicsWorld().destroyBody(prevBody);
        }

    }

    public void placeTile(String layerName, Tile t, Location loc)
    {
        placeTile(layerName, t, loc.getTileX(), loc.getTileY());
    }

    public void placeTile(String layerName, Tile t, int x, int y)
    {
        TileLayer layer = layers.get(layerName);
        Cell cell = layer.getCell(x, y);
        Body prevBody = cell.getBody();
        Tile prev = layer.placeTile(t, x, y);

        // Remove previous tile body
        if(prev != null)
        {
            getPhysicsWorld().destroyBody(prevBody);
        }

        if(t != null)
        {
            Vector2[] verts = new Vector2[4];
            float tileOriginX = tileset.getTileWidth();
            float tileOriginY = tileset.getTileHeight();
            CollisionMask mask = t.getMask();

            Vector2 origin = new Vector2(tileOriginX, tileOriginY);

            verts[0] = (new Vector2(origin).sub(t.getMask().getVertex(0))).scl(1.0f / World.PIXELS_PER_METER);
            verts[1] = (new Vector2(origin).sub(t.getMask().getVertex(1))).scl(1.0f / World.PIXELS_PER_METER);
            verts[2] = (new Vector2(origin).sub(t.getMask().getVertex(2))).scl(1.0f / World.PIXELS_PER_METER);
            verts[3] = (new Vector2(origin).sub(t.getMask().getVertex(3))).scl(1.0f / World.PIXELS_PER_METER);

            cell.setBody(createQuad(BodyDef.BodyType.StaticBody, x*tileset.getTileWidth(), (height-y-1)*tileset.getTileHeight(), verts, CollisionGroup.TILES));
        }
     }

    public Tile getTile(String layer, int x, int y)
    {
        return layers.get(layer).getCell(x, y).getTile();
    }

    public Tile getTile(String layer, Location loc)
    {
        return getTile(layer, loc.getTileX(), loc.getTileY());
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
        TileLayer layer = layers.get(layerName);

        if(layer != null)
        {
            layers.remove(layerName);
            layersOrdered.remove(layer);
        }
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);

        this.delta += delta;
    }

    @Override
    public void render(SpriteBatch spriteBatch, OrthographicCamera cam)
    {

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
        super.render(spriteBatch, cam);
    }
}
