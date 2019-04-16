package battlearena.common.tile;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Tileset
{

	private int nextId;
	private String name;
	private int width;
	private int height;
	private Texture tileSheet;
	private Map<String, Tile> tilesByName;
	private Map<Integer, Tile> tilesById;

	public Tileset(String name, int width, int height, Texture tileSheet)
	{
		this.name = name;
		this.width = width;
		this.height = height;
		this.tileSheet = tileSheet;

		this.tilesByName = new HashMap<String, Tile>();
		this.tilesById = new HashMap<Integer, Tile>();

		this.nextId = -1;
	}

	public Tileset(String name, String tilesheetPath, int width, int height)
	{
		this.name = name;
		this.width = width;
		this.height = height;

		this.tileSheet = new Texture(Gdx.files.absolute(tilesheetPath));

		this.tilesByName = new HashMap<String, Tile>();
		this.tilesById = new HashMap<Integer, Tile>();

		this.nextId = -1;
	}

	public Iterator<String> getTileNameIterator()
	{
		return tilesByName.keySet().iterator();
	}

	public String getName()
	{
		return name;
	}

	public void setName(String s)
	{
		name = s;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public TextureRegion getTileRegionForId(int id)
	{
		int col = (int) (id % width);
		int row = (int) (id / width);
		int tw = getTileWidth();
		int th = getTileHeight();

		if(row >= height)
		{
			// Tile ID has gone out of bounds
			return null;
		}
		else
		{
			return new TextureRegion(tileSheet, col * tw, row * th, tw, th);
		}
	}

	public void updateTileName(String newName, Tile tile)
	{
		tilesByName.remove(tile.getName());
		tilesByName.put(newName, tile);
		tile.setName(newName);
	}

	public Texture getTileSheet()
	{
		return tileSheet;
	}

	public Iterator<String> getDefinitionNameItr()
	{
		return tilesByName.keySet().iterator();
	}

	public boolean nameTaken(String name)
	{
		return tilesByName.containsKey(name);
	}

	public Tile getTile(String name)
	{
		return tilesByName.get(name);
	}

	public Tile getTile(int id)
	{
		return tilesById.get(id);
	}

	public void addTile(Tile tile)
	{
		tile.setId(++nextId);

		tilesByName.put(tile.getName(), tile);
		tilesById.put(tile.getId(), tile);
	}

	/**
	 * This is reserved for tiles that have already been assigned an ID. Used for deserializing tile definitions. Mostly for internal usage.
	 *
	 * @param tile
	 */
	public void addTileHasId(Tile tile)
	{
		tilesByName.put(tile.getName(), tile);
		tilesById.put(tile.getId(), tile);
	}

	public void removeTile(Tile tile)
	{
		tilesByName.remove(tile.getName());
		tilesById.remove(tile.getId());

		// If the tile being removed has the current highest ID, we can decrement the ID so we don't have ID gaps.
		if(tile.getId() == nextId)
			nextId--;
	}

	public int getTileCount()
	{
		return tilesByName.size();
	}

	public int getTilesheetWidth()
	{
		return tileSheet.getWidth();
	}

	public int getTilesheetHeight()
	{
		return tileSheet.getHeight();
	}

	public int getTileWidth()
	{
		return getTilesheetWidth() / width;
	}

	public int getTileHeight()
	{
		return getTilesheetHeight() / height;
	}

}
