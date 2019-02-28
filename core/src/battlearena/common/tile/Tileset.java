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
	private Map<String, battlearena.common.tile.TileDefinition> definitions;

	public Tileset(String name, String tilesheetPath, int width, int height)
	{
		this.name = name;
		this.width = width;
		this.height = height;

		this.tileSheet = new Texture(Gdx.files.absolute(tilesheetPath));

		this.definitions = new HashMap<String, battlearena.common.tile.TileDefinition>();
	}

	public Iterator<String> getDefinitionNameIterator()
	{
		return definitions.keySet().iterator();
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

	public TextureRegion getTileRegionForId(int id)
	{
		int col = (int) (id % width);
		int row = (int) (id / width);
		int tw = getTileWidth();
		int th = getTileHeight();

		return new TextureRegion(tileSheet, col * tw, row * th, tw, th);
	}

	public void updateTileDefinitionName(String newName, battlearena.common.tile.TileDefinition def)
	{
		definitions.remove(def.getName());
		definitions.put(newName, def);
		def.setName(newName);
	}

	public Texture getTileSheet()
	{
		return tileSheet;
	}

	public Iterator<String> getDefinitionNameItr()
	{
		return definitions.keySet().iterator();
	}

	public boolean nameTaken(String name)
	{
		return definitions.containsKey(name);
	}

	public battlearena.common.tile.TileDefinition getDefinition(String name)
	{
		return definitions.get(name);
	}

	public void addDefinition(battlearena.common.tile.TileDefinition def)
	{
		definitions.put(def.getName(), def);
	}

	public void removeDefinition(battlearena.common.tile.TileDefinition def)
	{
		definitions.remove(def.getName());
	}

	public int getDefinitionCount()
	{
		return definitions.size();
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
