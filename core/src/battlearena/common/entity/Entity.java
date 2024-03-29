package battlearena.common.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.DataInput;
import com.badlogic.gdx.utils.DataOutput;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import battlearena.common.RenderSettings;
import battlearena.common.entity.behavior.Behavior;
import battlearena.common.entity.data.Data;
import battlearena.common.world.World;
import sun.awt.datatransfer.DataTransferer;

public class Entity implements Comparable<Entity>
{

	// Data component names.
	public static final String POSITION = "Position";
	public static final String VELOCITY = "Velocity";
	public static final String SIZE = "Size";
	public static final String ROTATION = "Rotation";
	public static final String ANIM_TIME = "AnimTime";
	public static final String ACCELERATION = "Acceleration";
	public static final String ANIM = "Anim";
	public static final String FRAME = "Frame";
	public static final String BODY = "Body";
	public static final String DATA_X = "Pos.X";
	public static final String DATA_Y = "Pos.Y";
	public static final String HOVERED = "Hovered";
	public static final String PRESSED = "Pressed";
	public static final String EDITOR_SIZE = "Editor.Size";
	public static final String EDITOR_RADIUS = "Editor.Radius";

	private volatile static int nextId = 0;

	protected volatile int id;
	protected World world;
	protected RenderSettings renderSettings;
	private EntityConfig config;
	private Map<Integer, Data> dataComponents;
	private List<Behavior> behaviorComponents;
	private int nextDataID;
	private boolean dead;
	private int z;

	/**
	 * All game objects must contain this constructor.
	 * 
	 * @param world
	 *            The world that the game object will be spawned in.
	 * @param server
	 *            Whether or not the game instance this game object is running on is
	 *            a server.
	 */
	public Entity(EntityConfig config)
	{
		this.config = config;
		this.id = nextId++;
		this.world = config.getWorld();
		this.renderSettings = new RenderSettings();
		this.nextDataID = 0;
		this.dataComponents = new TreeMap<Integer, Data>();
		this.behaviorComponents = new ArrayList<Behavior>();
		this.dead = false;
	}

	public int getZ()
	{
		return z;
	}

	public void setZ(int z)
	{
		this.z = z;
	}

	public EntityConfig getConfig()
	{
		return config;
	}

	public boolean isDead()
	{
		return dead;
	}

	public void remove()
	{
		dead = true;
	}
	
	public RenderSettings getRenderSettings()
	{
		return renderSettings;
	}

	public int getId()
	{
		return id;
	}

	public void onKill()
	{

	}

	public World getWorld()
	{
		return world;
	}

	public <T extends Behavior> T addBehavior(Class<T> Class, String Name)
	{
		try
		{
			Constructor<?> CTor = Class.getConstructor(String.class, Entity.class);
			Behavior Component = (Behavior) CTor.newInstance(Name, this);
			addBehavior(Component);

			return Class.cast(Component);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public <T> T addData(Class<T> CompClass, String Name, boolean Serialized)
	{
		try
		{
			Constructor<?> CTor = CompClass.getConstructor(String.class, Entity.class, Boolean.TYPE);
			Data Component = (Data) CTor.newInstance(Name, this, Serialized);
			addData(Component);

			return CompClass.cast(Component);
		} catch (NoSuchMethodException e)
		{
			System.err.println("Failed to find data component constructor. Maybe this component is not serialized?");
			e.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public <T> T addData(Class<T> CompClass, String Name)
	{
		try
		{
			Constructor<?> CTor = CompClass.getConstructor(String.class, Entity.class);
			Data Component = (Data) CTor.newInstance(Name, this);
			addData(Component);

			return CompClass.cast(Component);
		} catch (NoSuchMethodException e)
		{
			System.err.println("Failed to find data component constructor. Maybe this component is serialized?");
			e.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public void addData(Data DataComponent)
	{
		dataComponents.put(nextDataID++, DataComponent);
	}

	public void addBehavior(Behavior Behavior)
	{
		behaviorComponents.add(Behavior);
	}

	public <T> T find(Class<T> Class, String Name)
	{
		if (Data.class.isAssignableFrom(Class))
			return findData(Class, Name);
		else if (Behavior.class.isAssignableFrom(Class))
			return findBehavior(Class, Name);

		return null;
	}

	public boolean hasData(String name)
	{
		return dataComponents.containsKey(name);
	}

	private <T> T findData(Class<T> Class, String Name)
	{
		Iterator<Integer> ComponentItr = dataComponents.keySet().iterator();
		while (ComponentItr.hasNext())
		{
			int ComponentID = ComponentItr.next();
			Data Component = dataComponents.get(ComponentID);

			if (Component.getClass() == Class && Component.GetName().equalsIgnoreCase(Name))
				return Class.cast(Component);
		}

		return null;
	}

	private <T> T findBehavior(Class<T> clazz, String name)
	{
		for (Behavior Component : behaviorComponents)
		{
			if (Component.getClass() == clazz && Component.GetName().equalsIgnoreCase(name))
				return clazz.cast(Component);
		}
		return null;
	}

	public Data findData(int DataID)
	{
		Iterator<Integer> ComponentItr = dataComponents.keySet().iterator();
		while (ComponentItr.hasNext())
		{
			int ComponentID = ComponentItr.next();
			if (ComponentID == DataID)
				return dataComponents.get(ComponentID);
		}

		return null;
	}

	public void serialize(DataOutput output)
	{
		Iterator<Integer> dataItr = dataComponents.keySet().iterator();
		while(dataItr.hasNext())
		{
			int compId = dataItr.next();
			Data d = dataComponents.get(compId);

			if(d.IsSerialized())
			{
				d.send(output);
			}
		}
	}

	public void deserialize(DataInput input)
	{
		Iterator<Integer> dataItr = dataComponents.keySet().iterator();
		while(dataItr.hasNext())
		{
			int compId = dataItr.next();
			Data d = dataComponents.get(compId);

			if(d.IsSerialized())
			{
				d.read(input);
			}
		}
	}


	public void Update(float delta)
	{
		for (Behavior Component : behaviorComponents)
		{
			if (Component.DoesUpdate())
				Component.Update(delta);
		}
	}

	public void Render(SpriteBatch batch, OrthographicCamera cam, RenderSettings.RenderMode filter)
	{

	}

	@Override
	public int compareTo(Entity entity)
	{
		return z - entity.z;
	}
}
