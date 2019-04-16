package battlearena.common.entity.data;

import com.badlogic.gdx.utils.DataInput;
import com.badlogic.gdx.utils.DataOutput;

import battlearena.common.entity.Entity;

public abstract class Data
{

	private String Name;
	private Entity Parent;
	private boolean Serialized;

	public Data(String Name, Entity Parent, boolean Serialized)
	{
		this.Name = Name;
		this.Parent = Parent;
		this.Serialized = Serialized;
	}

	public Entity GetParent()
	{
		return Parent;
	}

	public String GetName()
	{
		return Name;
	}

	public boolean IsSerialized()
	{
		return Serialized;
	}

	public void send(DataOutput output)
	{

	}

	public void read(DataInput input)
	{

	}

}
