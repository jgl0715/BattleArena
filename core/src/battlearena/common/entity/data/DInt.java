package battlearena.common.entity.data;

import java.io.IOException;

import com.badlogic.gdx.utils.DataInput;
import com.badlogic.gdx.utils.DataOutput;

import battlearena.common.entity.Entity;

public class DInt extends Data
{
	
	public Integer Value;

	public DInt(String Name, Entity Parent, boolean Serialized)
	{
		super(Name, Parent, Serialized);

		Value = 0;
	}

	@Override
	public void send(DataOutput output)
	{
		try
		{
			output.writeInt(Value);
		} catch (IOException e)
		{
			System.err.println("Could not serialize int value " + GetName());
			e.printStackTrace();
		}
	}

	@Override
	public void read(DataInput input)
	{
		try
		{
			Value = input.readInt();
		} catch (IOException e)
		{
			System.err.println("Could not deserialize int value " + GetName());
			e.printStackTrace();
		}
	}

}
