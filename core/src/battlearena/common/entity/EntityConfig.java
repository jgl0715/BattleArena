package battlearena.common.entity;

import java.util.HashMap;
import java.util.Map;

import battlearena.common.world.World;

public class EntityConfig
{

	private World world;
	private Map<String, Object> Configuration;

	public EntityConfig(World World)
	{
		this.world = World;
		this.Configuration = new HashMap<String, Object>();
	}

	public World getWorld()
	{
		return world;
	}

	public <T extends World> T GetWorld(Class<T> Class)
	{
		return Class.cast(world);
	}

	public boolean HasItem(String Name)
	{
		return Configuration.containsKey(Name);
	}

	public float GetConfigNumber(String Name)
	{
		Object ConfigItem = GetConfigItem(Name);

		if (ConfigItem.getClass() == Integer.class)
			return (Integer) ConfigItem;
		else if (ConfigItem.getClass() == Float.class)
			return (Float) ConfigItem;
		else
			throw new IllegalStateException("Item with name " + Name + " does not exist");
	}

	public float GetConfigFloat(String Name)
	{
		return GetConfigItem(Float.class, Name);
	}

	public short GetConfigShort(String Name)
	{
		return GetConfigItem(Short.class, Name);
	}

	public int GetConfigInt(String Name)
	{
		return GetConfigItem(Integer.class, Name);
	}

	public String GetConfigString(String Name)
	{
		return GetConfigItem(String.class, Name);
	}

	public <T> T GetConfigItem(Class<T> Class, String Name)
	{
		Object Item = Configuration.get(Name);
		if (Item == null)
			throw new IllegalStateException("Item with name " + Name + " does not exist");
		return Class.cast(Item);
	}

	public void AddConfigItem(String Name, Object Value)
	{
		Configuration.put(Name, Value);
	}

	public Object GetConfigItem(String Name)
	{
		return Configuration.get(Name);
	}

}
