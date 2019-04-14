package battlearena.common.tile;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Tileset
{

	private String name;
	private int width;
	private int height;
	private Texture tileSheet;
	private Map<String, Tile> tiles;

	public Tileset(String name, int width, int height, Texture tileSheet)
	{
		this.name = name;
		this.width = width;
		this.height = height;
		this.tileSheet = tileSheet;

		this.tiles = new HashMap<String, Tile>();
	}

	public Tileset(String name, String tilesheetPath, int width, int height)
	{
		this.name = name;
		this.width = width;
		this.height = height;

		this.tileSheet = new Texture(Gdx.files.absolute(tilesheetPath));

		this.tiles = new HashMap<String, Tile>();
	}

	public Iterator<String> getTileNameIterator()
	{
		return tiles.keySet().iterator();
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

		return new TextureRegion(tileSheet, col * tw, row * th, tw, th);
	}

	public void updateTileName(String newName, Tile tile)
	{
		tiles.remove(tile.getName());
		tiles.put(newName, tile);
		tile.setName(newName);
	}

	public Texture getTileSheet()
	{
		return tileSheet;
	}

	public Iterator<String> getDefinitionNameItr()
	{
		return tiles.keySet().iterator();
	}

	public boolean nameTaken(String name)
	{
		return tiles.containsKey(name);
	}

	public Tile getTile(String name)
	{
		return tiles.get(name);
	}

	public void addTile(Tile tile)
	{
		tiles.put(tile.getName(), tile);
	}

	public void removeTile(Tile tile)
	{
		tiles.remove(tile.getName());
	}

	public int getTileCount()
	{
		return tiles.size();
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
