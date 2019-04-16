package battlearena.common.entity.behavior;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import battlearena.common.entity.Entity;
import battlearena.common.entity.data.Data;

public abstract class Behavior
{

	private Entity Parent;
	private String Name;

	private Map<String, Field> MappedValues;
	private Map<String, Data> MappedComps;

	private boolean Updates;

	public Behavior(String Name, Entity Parent)
	{
		this.Parent = Parent;
		this.Name = Name;
		this.MappedValues = new HashMap<String, Field>();
		this.MappedComps = new HashMap<String, Data>();

		SetUpdatable(true);
	}

	public void SetUpdatable(boolean Updatable)
	{
		this.Updates = Updatable;
	}

	public void AddData(Class<? extends Data> Class, String Name)
	{
		// O(N) search through parent game object
		Data Component = Parent.find(Class, Name);

		if (Component == null)
			throw new IllegalArgumentException("Could not find data component with the name " + Name);

		try
		{
			// Also do the reflection here since reflection is expensive to do each frame.
			Field ValueField = Component.getClass().getField("Value");
			MappedValues.put(Name, ValueField);
			MappedComps.put(Name, Component);
		} catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		} catch (NoSuchFieldException e)
		{
			e.printStackTrace();
		} catch (SecurityException e)
		{
			e.printStackTrace();
		}
	}

	public <T> T Get(Class<T> Class, String Name)
	{
		// O(1) retrieval through hash map
		Field ValueField = MappedValues.get(Name);
		Data Component = MappedComps.get(Name);

		if (ValueField == null)
			throw new IllegalStateException("Component with name " + Name + " has not been mapped");

		if (Class != ValueField.getType())
			throw new IllegalStateException("Component class contains " + ValueField.getType() + ", not " + Class);

		try
		{
			return Class.cast(ValueField.get(Component));
		} catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public void Set(String Name, Object Value)
	{
		Field ValueField = MappedValues.get(Name);
		Data Component = MappedComps.get(Name);

		if (ValueField == null)
			throw new IllegalStateException("Component with name " + Name + " has not been mapped");

		try
		{
			ValueField.set(Component, Value);
		} catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}

	public Entity GetParent()
	{
		return Parent;
	}

	public String GetName()
	{
		return Name;
	}

	public boolean DoesUpdate()
	{
		return Updates;
	}

	public void Update(float delta)
	{

	}

}
