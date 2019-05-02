package battlearena.common.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

import battlearena.common.world.path.Node;
import battlearena.game.CollisionGroup;
import battlearena.common.tile.CollisionMask;
import battlearena.common.tile.Tile;
import battlearena.common.tile.Tileset;

public class TiledWorld extends World
{
    public static final String BG_LAYER = "Background";
    public static final String FG_LAYER = "Foreground";
    public static final int TILE_SIZE = 48;

    private Tileset tileset;

    private Map<String, TileLayer> layers;
    private List<TileLayer> layersOrdered;

    private Node[][] pathNodes;
    private int width;
    private int height;

    private float delta;

    private Set<Location> results;
    private Set<Location> visited;

    private TextureRegion miniMap;

    public TiledWorld(String name, Tileset tileset, int width, int height)
    {
        super(name);
        this.tileset = tileset;
        this.width = width;
        this.height = height;

        this.layers = new HashMap<String, TileLayer>();
        this.layersOrdered = new ArrayList<TileLayer>();

        results = new HashSet<Location>();
        visited = new HashSet<Location>();

        pathNodes = new Node[height][width];
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                pathNodes[y][x] = new Node(this, x, y);
            }
        }
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

    public Node getNodeAt(int x, int y)
    {
        return pathNodes[y][x];
    }

    public boolean isLocInBounds(int tx, int ty)
    {
        return tx >= 0 && ty >= 0 && tx < width && ty < height;
    }

    public boolean isLocInBounds(Location loc)
    {
        return loc.getTileX() >= 0 && loc.getTileY() >= 0 && loc.getTileX() < width && loc.getTileY() < height;
    }

    /**
     * Checks if there is a tile on the specified location on the foreground tile layer.
     *
     * @param tx
     * @param ty
     * @return
     */
    public boolean isWall(int tx, int ty)
    {
        return getTile(FG_LAYER, tx, ty) != null;
    }

    public boolean isWall(Location loc)
    {
        if(!isLocInBounds(loc))
            return false;
        
        return isWall(loc.getTileX(), loc.getTileY());
    }

    public Location entityLocationToTileLocation(Vector2 pos)
    {
        int tx = (int) (pos.x / TILE_SIZE);
        int ty = (int) (pos.y / TILE_SIZE);

        return new Location(tx, ty);
    }

    public Set<Location> floodSearch(String layer, int tx, int ty)
    {

        results.clear();
        visited.clear();

        if(isLocInBounds(tx, ty))
            floodSearch(layer, getTile(layer, tx, ty), tx, ty);

        return results;
    }

    private void floodSearch(String layer, Tile floodTile, int tx, int ty)
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

        floodSearch(layer, floodTile, tx - 1, ty);
        floodSearch(layer, floodTile, tx + 1, ty);
        floodSearch(layer, floodTile, tx, ty + 1);
        floodSearch(layer, floodTile, tx, ty - 1);
    }

    public Iterator<TileLayer> tileLayerIterator()
    {
        return layersOrdered.iterator();
    }

    public void changeTileset(Tileset set)
    {
        this.tileset = set;
    }

    public int getTileWidth()
    {
        return TILE_SIZE;
    }

    public int getTileHeight()
    {
        return TILE_SIZE;
    }

    public int getPixelWidth()
    {
        return width * TILE_SIZE;
    }

    public int getPixelHeight()
    {
        return height * TILE_SIZE;
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

        // TODO: if world is not static, check path nodes here. May need to remove wall.

        // Remove previous tile body
        if(prevBody != null)
        {
            getPhysicsWorld().destroyBody(prevBody);
            cell.setBody(null);
        }

    }

    /**
     * Places a collision mask into this world. This object will be present in the physics world as
     * a static body and will be used for shadow casting and entity collision.
     *
     * @param mask
     * @param group
     * @param originX
     * @param originY
     * @param x
     * @param y
     * @return
     */
    public Body placeMask(CollisionMask mask, CollisionGroup group, float originX, float originY, float x, float y)
    {
        Vector2[] verts = new Vector2[4];
        Vector2 origin = new Vector2(originX, originY);

        float scaleX = 1.0f / World.PIXELS_PER_METER * (TILE_SIZE / tileset.getTileWidth());
        float scaleY = 1.0f / World.PIXELS_PER_METER * (TILE_SIZE / tileset.getTileHeight());

        verts[0] = (new Vector2(origin).add(mask.getVertex(3))).scl(scaleX, scaleY);
        verts[1] = (new Vector2(origin).add(mask.getVertex(2))).scl(scaleX, scaleY);
        verts[2] = (new Vector2(origin).add(mask.getVertex(1))).scl(scaleX, scaleY);
        verts[3] = (new Vector2(origin).add(mask.getVertex(0))).scl(scaleX, scaleY);

        return createQuad(BodyDef.BodyType.StaticBody, x, y, verts, group);
    }

    public void placeTile(String layerName, Tile t, Location loc)
    {
        placeTile(layerName, t, loc.getTileX(), loc.getTileY());
    }

    public void placeTile(String layerName, Tile t, int x, int y, int meta)
    {
        TileLayer layer = layers.get(layerName);
        Cell cell = layer.getCell(x, y);
        Body prevBody = cell.getBody();
        Tile prev = layer.placeTile(t, x, y, meta);

        // Remove previous tile body
        if(prevBody != null)
        {
            getPhysicsWorld().destroyBody(prevBody);
        }

        if(t != null && layer.isCollisionEnabled())
        {
            pathNodes[y][x].isWall = true;

            CollisionMask mask = t.getMask();
            float tileOriginX = 0;//tileset.getTileWidth();
            float tileOriginY = 0;//tileset.getTileHeight();
            float tileX = x*TILE_SIZE;
            float tileY = (height-y-1)*TILE_SIZE;
            cell.setBody(placeMask(mask, CollisionGroup.TILES, tileOriginX, tileOriginY, tileX, tileY));
        }
    }

    public Set<Location> findLocationsMatchingMeta(int meta)
    {
        Set<Location> locs = new HashSet<Location>();
        for(TileLayer layer : layersOrdered)
        {
            for(int x = 0; x < width; x++)
            {
                for(int y = 0; y < height; y++)
                {
                    Cell c = layer.getCell(x, y);
                    if(c.getMeta() == meta)
                    {
                        locs.add(new Location(x, y));
                    }
                }
            }
        }

        return locs;
    }

    public void placeTile(String layerName, Tile t, int x, int y)
    {
        // Use zero for the meta.
        placeTile(layerName, t, x, y, 0);
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

    public void addTileLayer(TileLayer layer)
    {
        layers.put(layer.getName(), layer);

        layersOrdered.add(layer);
    }

    public void removeTileLayer(String layerName)
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
                layer.render(spriteBatch, cam);
            }
        }
        spriteBatch.end();

        // Render entities and lights on top of tiles.
        super.render(spriteBatch, cam);
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                stringBuilder.append(pathNodes[y][x].isWall ? "#" : " ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

}
